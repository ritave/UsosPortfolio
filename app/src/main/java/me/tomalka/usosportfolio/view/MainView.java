package me.tomalka.usosportfolio.view;

import java.util.List;

import me.tomalka.usosdroid.jsonapis.FacultyInfo;

public interface MainView extends MVPView {
    void setRootFaculty(FacultyInfo faculty);
    void setChildrenFaculties(List<FacultyInfo> faculties);
    void setErrorMessage(String message);
    void setProgressIndicator(boolean visible);
}
