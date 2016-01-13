package me.tomalka.usosdroid;

import android.util.Log;

import me.tomalka.usosdroid.jsonapis.FacultyInfo;
import retrofit2.Retrofit;
import retrofit2.GsonConverterFactory;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class Usos {
    private static final int MAX_FACULTIES_PER_REQUEST = 500;
    private String provider;
    private Retrofit retrofit;
    private UsosService service;

    private Observable<FacultyInfo> mergeFacultyRequests(Observable<FacultyInfo> result, StringBuilder piped)
    {
        piped.setLength(piped.length() - 1);
        return result.concatWith(
                service
                        .loadFaculties(piped.toString())
                        .flatMap(facsMap -> Observable.from(facsMap.values()))
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
        );
    }

    public Usos(String provider) {
        this.provider = provider;
        retrofit = new Retrofit.Builder()
                .baseUrl(provider)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(UsosService.class);
    }

    public String getProvider() {
        return provider;
    }

    public Observable<FacultyInfo> getFaculty(String facId)
    {
        return service.loadFacultyInfo(facId);
    }

    public Observable<FacultyInfo> getFaculties(Iterable<String> facIds) {
        int bunched = 0;
        StringBuilder builder = new StringBuilder();
        Observable<FacultyInfo> result = Observable.empty();
        for (String facId : facIds) {
            builder.append(facId).append('|');
            bunched++;
            if (bunched == MAX_FACULTIES_PER_REQUEST) {
                bunched = 0;
                result = mergeFacultyRequests(result, builder);
                builder = new StringBuilder();
            }
        }
        if (bunched > 0)
            result = mergeFacultyRequests(result, builder);

        return result;
    }

    public Observable<FacultyInfo> getFacultyChildren(FacultyInfo faculty)
    {
        // Load a list of children ids, and then load info about each one
        return service
                .loadFacultyChildrenIds(faculty.getFacultyId())
                .flatMap(idsList -> getFaculties(idsList))
                .filter(facInfo -> facInfo.isPublic());
    }
}
