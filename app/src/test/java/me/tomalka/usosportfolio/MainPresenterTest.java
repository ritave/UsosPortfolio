package me.tomalka.usosportfolio;

import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import me.tomalka.usosdroid.Usos;
import me.tomalka.usosdroid.jsonapis.FacultyInfo;
import me.tomalka.usosportfolio.presenter.MainPresenter;
import me.tomalka.usosportfolio.utils.MockFacultyFactory;
import me.tomalka.usosportfolio.view.MainView;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainPresenterTest {
    private UsosApplication app;
    private Usos usos;
    private MainPresenter mainPresenter;
    private MainView mainView;

    @Before
    public void setUp() {
        app = (UsosApplication) RuntimeEnvironment.application;
        app.setScheduler(Schedulers.immediate());

        mainPresenter = new MainPresenter();

        mainView = mock(MainView.class);
        usos = mock(Usos.class);

        when(mainView.getContext()).thenReturn(app);

        mainPresenter.setUsos(usos);
        mainPresenter.onAttach(mainView);
    }

    @After
    public void tearDown() {
        mainPresenter.onDetach();
    }

    @Test
    public void testNoConnection() {
        when(usos.getFaculty(anyString())).thenReturn(Observable.error(new Exception("No internets")));

        mainPresenter.loadFaculties();

        verify(mainView).setRootFaculty(null);
        verify(mainView).setChildrenFaculties(null);
        verify(mainView).setProgressIndicator(true);
        verify(mainView).setProgressIndicator(false);
        verify(mainView).setErrorMessage(app.getString(R.string.error_load_faculties));
    }

    @Test
    public void testErrorInChildren() {
        FacultyInfo mockFaculty = MockFacultyFactory.create();
        when(usos.getFaculty(anyString())).thenReturn(Observable.just(mockFaculty));
        when(usos.getFaculties(any())).thenReturn(Observable.error(new Exception("No internets")));

        mainPresenter.loadFaculties();

        verify(usos).getFacultyChildren(mockFaculty);

        verify(mainView).setRootFaculty(null);
        verify(mainView).setChildrenFaculties(null);
        verify(mainView).setProgressIndicator(false);
        verify(mainView).setErrorMessage(app.getString(R.string.error_load_faculties));
    }

    @Test
    public void testNoChildren() {
        FacultyInfo rootFaculty = MockFacultyFactory.create();
        when(usos.getFaculty(MainPresenter.ROOT_FAC_ID)).thenReturn(Observable.just(rootFaculty));

        when(usos.getFacultyChildren(any())).thenReturn(Observable.empty());

        mainPresenter.loadFaculties();

        verify(mainView).setRootFaculty(rootFaculty);
        ArgumentCaptor<List> childrenCaptor = ArgumentCaptor.forClass(List.class);
        verify(mainView, times(2)).setChildrenFaculties(childrenCaptor.capture());
        assertNull("Children list not reset", childrenCaptor.getAllValues().get(0));
        assertEquals("Children list is not empty", childrenCaptor.getValue(), new ArrayList());

        verify(mainView).setProgressIndicator(false);
        verify(mainView).setErrorMessage("");
    }

    @Test
    public void testEverythingNormal() {
        FacultyInfo rootFaculty = MockFacultyFactory.create();
        when(usos.getFaculty(MainPresenter.ROOT_FAC_ID)).thenReturn(Observable.just(rootFaculty));
        List<FacultyInfo> children = MockFacultyFactory.create(10);
        when(usos.getFacultyChildren(any())).thenReturn(Observable.from(children));

        mainPresenter.loadFaculties();

        verify(mainView).setRootFaculty(rootFaculty);
        verify(mainView).setProgressIndicator(false);
        verify(mainView).setErrorMessage("");

        ArgumentCaptor<List> childrenCaptor = ArgumentCaptor.forClass(List.class);
        verify(mainView, times(2)).setChildrenFaculties(childrenCaptor.capture());
        assertNull("Children list not reset", childrenCaptor.getAllValues().get(0));
        assertEquals("Children lists are not equal", childrenCaptor.getValue(), children);
    }
}
