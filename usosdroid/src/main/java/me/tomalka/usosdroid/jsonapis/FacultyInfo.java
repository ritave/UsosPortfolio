package me.tomalka.usosdroid.jsonapis;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ritave on 2016-01-09.
 */
public class FacultyInfo {
    @SerializedName("id")
    private String facId;
    @SerializedName("name")
    private Map<String, String> facName = new HashMap<>();
    @SerializedName("cover_urls")
    private Map<String, String> coverUrls = new HashMap<>();
    @SerializedName("is_public")
    private boolean isPublic;
    @SerializedName("homepage_url")
    private String homepageUrl;
    @SerializedName("phone_numbers")
    private List<String> phoneNumbers = new ArrayList<>();
    @SerializedName("postal_address")
    private String postalAddress;
    @SerializedName("static_map_urls")
    private Map<String, String> mapUrls = new HashMap<>();

    public String getFacultyId() {
        return facId;
    }
    public Map<String, String> getFacName() {
        return facName;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public String getHomepageUrl() {
        return homepageUrl;
    }
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }
    public String getPostalAddress() {
        return postalAddress;
    }

    public boolean gotAnyCoverPhoto()
    {
        return mapUrls.get("800x400") != null || coverUrls.size() != 0;
    }

    public String getCoverPhotoUrl()
    {
        if (coverUrls.get("screen") != null)
            return coverUrls.get("screen");
        if (mapUrls.get("800x400") != null)
            return mapUrls.get("800x400");
        return null;
    }
}
