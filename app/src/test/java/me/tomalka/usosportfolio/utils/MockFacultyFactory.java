package me.tomalka.usosportfolio.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.tomalka.usosdroid.jsonapis.FacultyInfo;

import static org.mockito.Mockito.*;

public class MockFacultyFactory {
    public static FacultyInfo create() {
        Random r = new Random();

        String id = Integer.toString(r.nextInt(10000));
        FacultyInfo mockedFaculty = mock(FacultyInfo.class);
        when(mockedFaculty.getFacultyId()).thenReturn(id);
        when(mockedFaculty.getCoverPhotoUrl()).thenReturn("http://example.com/coverPhoto" + id + ".png");

        Map mockMap = mock(Map.class);
        when(mockMap.get("pl")).thenReturn("Faculty " + id);
        verify(mockMap, never()).get("en");
        when(mockedFaculty.getFacName()).thenReturn(mockMap);

        List<String> telephones = new ArrayList<>();
        telephones.add("123-456-789");
        when(mockedFaculty.getPhoneNumbers()).thenReturn(telephones);

        when(mockedFaculty.getPostalAddress()).thenReturn("Warsaw 00-123 Banacha st. 2/3");
        when(mockedFaculty.gotAnyCoverPhoto()).thenReturn(true);
        when(mockedFaculty.isPublic()).thenReturn(true);

        return mockedFaculty;
    }

    public static List<FacultyInfo> create(int count) {
        List<FacultyInfo> result = new ArrayList<>();
        for (int i = 0; i < count; i++)
            result.add(create());
        return result;
    }
}
