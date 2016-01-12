package me.tomalka.usosportfolio;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import me.tomalka.usosdroid.jsonapis.FacultyInfo;
import me.tomalka.usosdroid.jsonapis.InstallationInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseUsosActivity implements RootInfoFragment.RootInfoFragmentListener {
    private final String ROOT_FAC_ID = "00000000";

    private ImageView toolbarImage;
    private List<FacultyInfo> childrenData = new ArrayList<>();
    private FacultyInfoAdapter childrenAdapter = new FacultyInfoAdapter(childrenData);
    private FacultyInfo rootFaculty;
    RootInfoFragment infoFragment;

    @Override
    public void RequestClose() {
        getSupportFragmentManager().popBackStack();
        infoFragment = null;
    }

    private void loadFaculty(String facultyId)
    {
        Observable<FacultyInfo> facObservable =  getUsos().getFaculty(facultyId)
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).cache();

        facObservable.subscribe(
                faculty -> rootFaculty = faculty,
                ex -> Log.e(LOGTAG, Log.getStackTraceString(ex)));

        setToolbarTitle(facObservable);
        loadCoverImage(facObservable);
        loadChildren(facObservable);
    }

    private void setToolbarTitle(Observable<FacultyInfo> obs)
    {
        obs.subscribe(
                info -> getSupportActionBar().setTitle(info.getFacName().get("pl")),
                ex -> Log.e(LOGTAG, Log.getStackTraceString(ex))
        );
    }

    private void loadCoverImage(Observable<FacultyInfo> obs)
    {
        obs.subscribe(
                info ->
                {
                    Picasso.with(this).load(info.getCoverPhotoUrl()).into(
                            toolbarImage,
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                    View bottomScrim = findViewById(R.id.toolbar_scrim_bottom);
                                    View settingScrim = findViewById(R.id.toolbar_scrim_settings);
                                    bottomScrim.setVisibility(View.VISIBLE);
                                    //settingScrim.setVisibility(View.VISIBLE);
                                    Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein);
                                    bottomScrim.startAnimation(anim);
                                    //settingScrim.startAnimation(anim);
                                }

                                @Override
                                public void onError() {
                                    Log.e(LOGTAG, "Failed to load cover photo");
                                }
                            }
                    );
                },
                ex -> Log.e(LOGTAG, Log.getStackTraceString(ex))
        );
    }

    private void loadChildren(Observable<FacultyInfo> faculty)
    {
        Observable<FacultyInfo> obs = getUsos()
                .getFacultyChildren(faculty);

        if (DEMO_MODE == true) // Nice hack huh ;)
            obs = obs.filter(info -> info.gotAnyCoverPhoto() &&
                    (info.getPhoneNumbers().size() != 0 || (info.getPostalAddress() != null && !info.getPostalAddress().isEmpty())));

        obs.observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                            childrenData.add(data);
                            childrenAdapter.notifyItemInserted(childrenData.size() - 1);
                        },
                        ex -> Log.e(LOGTAG, Log.getStackTraceString(ex))
                );
    }

    private void onFabPressed(View view)
    {
        if (rootFaculty == null) {
            Snackbar.make(view, "Please wait while the info loads", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;
            infoFragment = RootInfoFragment.newInstance(rootFaculty, cx, cy);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.main_layout, infoFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (infoFragment != null) {
            infoFragment.onBackPressed();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbarImage = (ImageView)findViewById(R.id.toolbar_image);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> onFabPressed(view));

        RecyclerView childrenList = (RecyclerView)findViewById(R.id.children_list);
        childrenList.setLayoutManager(new LinearLayoutManager(this));
        childrenList.setAdapter(childrenAdapter);
        childrenList.setItemAnimator(new SlideInLeftAnimator(new DecelerateInterpolator()));

        loadFaculty(ROOT_FAC_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
         //   return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
