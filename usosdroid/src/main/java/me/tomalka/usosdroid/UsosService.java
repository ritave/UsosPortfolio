package me.tomalka.usosdroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GET("fac/faculty?fields=id|name|cover_urls[screen]|is_public|homepage_url|postal_address|phone_numbers|static_map_urls[800x400]")
    Observable<FacultyInfo> loadFacultyInfo(@Query("fac_id") String facId);

    @GET("fac/subfaculties_deep")
    Observable<ArrayList<String>> loadFacultyChildrenIds(@Query("fac_id") String facId);

    @GET("fac/faculties?fields=id|name|cover_urls[screen]|is_public|homepage_url|postal_address|phone_numbers|static_map_urls[800x400]")
    Observable<Map<String, FacultyInfo>> loadFaculties(@Query("fac_ids") String piped_facIds);

}
