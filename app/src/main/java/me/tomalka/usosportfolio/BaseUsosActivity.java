package me.tomalka.usosportfolio;

import com.trello.navi.NaviComponent;
import com.trello.navi.component.NaviActivity;
import com.trello.navi.component.support.NaviAppCompatActivity;
import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.navi.NaviLifecycle;

import me.tomalka.usosdroid.Usos;
import me.tomalka.usosdroid.UsosService;

/**
 * Created by Ritave on 2016-01-09.
 */
public class BaseUsosActivity extends NaviAppCompatActivity {
    private static final String PROVIDER = "https://usosapps.uw.edu.pl/services/";
    private final Usos usos = new Usos(PROVIDER);

    protected final NaviComponent naviComponent = this;
    protected final ActivityLifecycleProvider lifecycleProvider = NaviLifecycle.createActivityLifecycleProvider(this);

    protected UsosService getUsosService() {
        return usos.getService();
    }
}
