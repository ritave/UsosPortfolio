package me.tomalka.usosdroid.jsonapis;

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
    String facId;
    @SerializedName("name")
    Map<String, String> facName = new HashMap<>();
    @SerializedName("cover_urls")
    Map<String, String> coverUrls = new HashMap<>();
    @SerializedName("is_public")
    boolean isPublic;
    @SerializedName("homepage_url")
    String homepageUrl;
    @SerializedName("phone_numbers")
    List<String> phoneNumbers = new ArrayList<>();
    @SerializedName("postal_address")
    String postalAddress;

    public String getFacultyId() {
        return facId;
    }
    public Map<String, String> getFacName() {
        return facName;
    }
    public Map<String, String> getCoverUrls() {
        return coverUrls;
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
}
