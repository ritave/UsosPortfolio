package me.tomalka.usosdroid.jsonapis;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class InstallationInfo {
    @SerializedName("version")
    private String usosVersion;
    @SerializedName("institution_name")
    private Map<String, String> institutionName = new HashMap<>();

    public Map<String, String> getInstitutionName() {
        return institutionName;
    }

    public String getUsosVersion() {
        return usosVersion;
    }

}
