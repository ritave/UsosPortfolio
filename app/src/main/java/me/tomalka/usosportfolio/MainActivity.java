package me.tomalka.usosportfolio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v8.renderscript.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import me.tomalka.usosdroid.jsonapis.FacultyInfo;
import me.tomalka.usosdroid.jsonapis.InstallationInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseUsosActivity {
    private final String ROOT_FAC_ID = "00000000";

    private RenderScript rs;
    private ImageView toolbarImage;
    private Toolbar toolbar;

    private void loadFaculty(String facultyId)
    {
        Observable<FacultyInfo> facObservable =  getUsosService().loadFacultyInfo(facultyId)
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).cache();

        setToolbarTitle(facObservable);
        loadCoverImage(facObservable);
    }

    private void setToolbarTitle(Observable<FacultyInfo> obs)
    {
        obs.subscribe(
                info -> toolbar.setTitle(info.getFacName().get("en"))
        );
    }

    private void loadCoverImage(Observable<FacultyInfo> obs)
    {
        obs.subscribe(
                info ->
                {
                    Picasso.with(this).load(info.getCoverUrls().get("screen")).into(toolbarImage);
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbarImage = (ImageView)findViewById(R.id.toolbar_image);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
        );

        rs = RenderScript.create(this);

        /*
        getUsosService().loadInstallationInfo()
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        info -> {
                            toolbar.setTitle(((InstallationInfo) info).getInstitutionName().get("en"));
                        },
                        ex -> {
                            Toast.makeText(this, String.format("Failed to load website: %s", ex.getMessage()), Toast.LENGTH_LONG).show();
                            Log.e("UsosApi", ex.getMessage());
                            Log.e("UsosApi", Log.getStackTraceString(ex));

                        }
                );*/

        loadFaculty(ROOT_FAC_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
