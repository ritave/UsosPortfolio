package me.tomalka.usosportfolio.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import me.tomalka.usosdroid.jsonapis.FacultyInfo;
import me.tomalka.usosportfolio.BaseUsosActivity;
import me.tomalka.usosportfolio.FacultyInfoAdapter;
import me.tomalka.usosportfolio.R;
import me.tomalka.usosportfolio.presenter.MainPresenter;

public class MainActivity extends BaseUsosActivity implements RootInfoFragment.RootInfoFragmentListener, MainView {
    private final static String TAG = "MainActivity";

    private ImageView toolbarImage;
    private FacultyInfo rootFaculty;
    private RootInfoFragment infoFragment;
    private MainPresenter presenter;
    private RecyclerView childrenList;
    private TextView errorText;
    private View bottomScrim;
    private SwipeRefreshLayout refreshLayout;
    private FacultyInfoAdapter adapter;
    private List<FacultyInfo> faculties = new ArrayList<>();

    @Override
    public void RequestClose() {
        getSupportFragmentManager().popBackStack();
        infoFragment = null;
    }

    @Override
    public void setRootFaculty(FacultyInfo faculty) {
        rootFaculty = faculty;
        if (faculty != null) {
            getSupportActionBar().setTitle(faculty.getFacName().get("pl"));
        }

        loadCoverImage(faculty);
    }

    @Override
    public void setErrorMessage(String message) {
        errorText.clearAnimation();
        if (message != null && !message.isEmpty()) {
            if (errorText.getVisibility() != View.VISIBLE) {
                Animation fadeAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein);
                errorText.setVisibility(View.VISIBLE);
                errorText.startAnimation(fadeAnim);
            }
            errorText.setText(message);
        } else if (errorText.getVisibility() == View.VISIBLE){
            Animation fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout);
            fadeAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    errorText.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            errorText.startAnimation(fadeAnim);
        }
    }

    @Override
    public void setProgressIndicator(boolean visible) {
        if (refreshLayout.isRefreshing() != visible)
            refreshLayout.setRefreshing(visible);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setChildrenFaculties(List<FacultyInfo> faculties) {
        childrenList.clearAnimation();
        if (faculties != null) {
            int i = this.faculties.size();
            this.faculties.addAll(faculties);
            for (; i < this.faculties.size(); i++)
                adapter.notifyItemInserted(i);
        } else {
            for (int i = this.faculties.size(); i >= 0; i--)
                adapter.notifyItemRemoved(i);
            this.faculties.clear();
        }
    }

    private void loadCoverImage(FacultyInfo faculty)
    {
        if (faculty == null && bottomScrim.getVisibility() == View.VISIBLE) {
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
            bottomScrim.clearAnimation();
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bottomScrim.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            bottomScrim.startAnimation(fadeOut);
            Picasso.with(this).load((String)null).into(toolbarImage);
        } else if (faculty != null) {
            Picasso.with(this).load(faculty.getCoverPhotoUrl()).into(
                    toolbarImage,
                    new Callback() {
                        @Override
                        public void onSuccess() {
                            Animation fadeAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein);
                            bottomScrim.clearAnimation();
                            bottomScrim.setVisibility(View.VISIBLE);
                            bottomScrim.startAnimation(fadeAnim);

                            //View settingScrim = findViewById(R.id.toolbar_scrim_settings);
                            //settingScrim.setVisibility(View.VISIBLE);
                            //settingScrim.startAnimation(anim);
                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, "Failed to load cover photo");
                        }
                    }
            );
        }
    }

    private void onFabPressed(View view)
    {
        if (rootFaculty == null) {
            Snackbar.make(view, getString(R.string.snackbar_wait), Snackbar.LENGTH_LONG)
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

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("");

        toolbarImage = (ImageView)findViewById(R.id.toolbar_image);
        errorText = (TextView)findViewById(R.id.error_text);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        childrenList = (RecyclerView)findViewById(R.id.children_list);
        bottomScrim = findViewById(R.id.toolbar_scrim_bottom);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        RecyclerView childrenList = (RecyclerView)findViewById(R.id.children_list);

        refreshLayout.setOnRefreshListener(() -> {
            presenter.loadFaculties();
        });

        fab.setOnClickListener(view -> onFabPressed(view));

        childrenList.setLayoutManager(new LinearLayoutManager(this));
        childrenList.setItemAnimator(new SlideInLeftAnimator(new DecelerateInterpolator()));

        adapter = new FacultyInfoAdapter(faculties);
        childrenList.setAdapter(adapter);

        if (presenter == null)
        {
            presenter = new MainPresenter();
        }
        presenter.onAttach(this);
        refreshLayout.post(() ->
        presenter.loadFaculties());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
