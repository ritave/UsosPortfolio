package me.tomalka.usosdroid;

import me.tomalka.usosdroid.jsonapis.InstallationInfo;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Ritave on 2016-01-09.
 */
public interface UsosService {
    @GET("apisrv/installation")
    Observable<InstallationInfo> loadInstallationInfo();
}
