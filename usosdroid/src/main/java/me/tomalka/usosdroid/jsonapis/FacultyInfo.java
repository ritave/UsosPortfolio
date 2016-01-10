package me.tomalka.usosdroid.jsonapis;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ritave on 2016-01-09.
 */
public class FacultyInfo {
    @SerializedName("id")
    String facId;
    @SerializedName("name")
    Map<String, String> facName = new HashMap<>();
    @SerializedName("cover_urls")
    Map<String, String> coverUrls = new HashMap<>();

    public String getFacId() {
        return facId;
    }

    public Map<String, String> getFacName() {
        return facName;
    }

    public Map<String, String> getCoverUrls() {
        return coverUrls;
    }

}
