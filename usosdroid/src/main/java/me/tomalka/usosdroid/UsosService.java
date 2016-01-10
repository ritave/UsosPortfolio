package me.tomalka.usosdroid;

import me.tomalka.usosdroid.jsonapis.FacultyInfo;
import me.tomalka.usosdroid.jsonapis.InstallationInfo;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ritave on 2016-01-09.
 */
public interface UsosService {
    @POST("apisrv/installation")
    Observable<InstallationInfo> loadInstallationInfo();

    @GET("fac/faculty?fields=id|name|cover_urls[screen]")
    Observable<FacultyInfo> loadFacultyInfo(@Query("fac_id") String fac_id);
}
