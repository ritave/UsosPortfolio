package me.tomalka.usosportfolio.presenter;

import android.content.Context;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.tomalka.usosdroid.Usos;
import me.tomalka.usosdroid.jsonapis.FacultyInfo;
import me.tomalka.usosportfolio.R;
import me.tomalka.usosportfolio.UsosApplication;
import me.tomalka.usosportfolio.view.MainView;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MainPresenter implements MVPPresenter<MainView> {
    public static final String TAG = "MainPresenter";
    public static final boolean DEMO_MODE = true;

    private static final String PROVIDER = "https://usosapps.uw.edu.pl/services/";
    private final String ROOT_FAC_ID = "00000000";

    private Usos usos = new Usos(PROVIDER);
    private MainView view;
    private CompositeSubscription subscription = new CompositeSubscription();
    private FacultyInfo rootFaculty;

    @Override
    public void onAttach(MainView view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        subscription.clear();
        rootFaculty = null;
        view = null;
    }

    public void loadFaculties() {
        rootFaculty = null;
        view.setProgressIndicator(true);
        view.setRootFaculty(null);
        view.setChildrenFaculties(null);
        subscription.add(
            usos
                    .getFaculty(ROOT_FAC_ID)
                    .subscribeOn(getScheduler())
                    .observeOn(getScheduler())
                    .flatMap(facultyInfo -> {
                        rootFaculty = facultyInfo;
                        return loadChildrenFaculties(rootFaculty);
                    })
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(faculties -> {
                                view.setProgressIndicator(false);
                                view.setRootFaculty(rootFaculty);
                                view.setChildrenFaculties(faculties);
                                view.setErrorMessage("");
                            },
                            ex -> {
                                view.setProgressIndicator(false);
                                setFacultyError(ex);
                            }
                    )
        );
    }

    // For testing
    public void setUsos(Usos usos) {
        this.usos = usos;
    }

    private Scheduler getScheduler() {
        return UsosApplication.get(view.getContext()).getScheduler();
    }

    private Observable<FacultyInfo> loadChildrenFaculties(FacultyInfo faculty) {
        Observable<FacultyInfo> obs = usos
                .getFacultyChildren(faculty);

        if (DEMO_MODE == true) // Nice hack huh ;)
            obs = obs.filter(info -> info.gotAnyCoverPhoto() &&
                    (info.getPhoneNumbers().size() != 0 || (info.getPostalAddress() != null && !info.getPostalAddress().isEmpty())));

        return obs;
    }

    private void setFacultyError(Throwable ex) {
        Log.e(TAG, Log.getStackTraceString(ex));
        view.setErrorMessage(view.getContext().getString(R.string.error_load_faculties));
    }
}
