package me.tomalka.usosportfolio;

import android.support.v7.app.AppCompatActivity;

import me.tomalka.usosdroid.Usos;
import me.tomalka.usosdroid.UsosService;

public class BaseUsosActivity extends AppCompatActivity {
    private static final String PROVIDER = "https://usosapps.uw.edu.pl/services/";
    public static final String LOGTAG = "usosportfolio";
    public static final boolean DEMO_MODE = true;
    private final Usos usos = new Usos(PROVIDER);

    protected Usos getUsos() { return usos; }
}
