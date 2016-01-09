package me.tomalka.usosportfolio;

import android.support.v7.app.AppCompatActivity;

import me.tomalka.usosdroid.Usos;
import me.tomalka.usosdroid.UsosService;

/**
 * Created by Ritave on 2016-01-09.
 */
public class BaseUsosActivity extends AppCompatActivity {
    private static final String PROVIDER = "https://usosapps.uw.edu.pl/services/";
    private Usos usos = new Usos(PROVIDER);

    /*
    @Override
    protected void onStart() {
        super.onStart();
        usos.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        usos.onStop();
    }*/

    protected UsosService getUsosService() {
        return usos.getService();
    }
}
