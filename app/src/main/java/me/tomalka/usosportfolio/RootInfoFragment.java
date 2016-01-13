package me.tomalka.usosportfolio;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import me.tomalka.usosdroid.jsonapis.FacultyInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class RootInfoFragment extends Fragment {
    private static final String ARG_FACULTY = "faculty";
    private static final String ARG_CX = "cx";
    private static final String ARG_CY = "cy";

    private FacultyInfo facultyInfo;
    private FacultyCard facultyCard;
    private int cx;
    private int cy;
    private boolean animationInProgress = false;

    private RootInfoFragmentListener mListener;

    public RootInfoFragment() {
    }

    public static RootInfoFragment newInstance(FacultyInfo facultyInfo, int cx, int cy) {
        RootInfoFragment fragment = new RootInfoFragment();
        Bundle args = new Bundle();

        args.putString(ARG_FACULTY, new Gson().toJson(facultyInfo));
        args.putInt(ARG_CX, cx);
        args.putInt(ARG_CY, cy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            facultyInfo = new Gson().fromJson(getArguments().getString(ARG_FACULTY), FacultyInfo.class);
            cx = getArguments().getInt(ARG_CX);
            cy = getArguments().getInt(ARG_CY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_root_info, container, false);
        facultyCard = (FacultyCard)v.findViewById(R.id.root_info_card);
        facultyCard.fromFacultyInfo(facultyInfo);
        v.findViewById(R.id.root_info_black).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                animate(true, (int) event.getX(), (int) event.getY());
            }
            return true;
        });

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        v.post(() -> {
            if (savedInstanceState == null)
                animate(false, -1, -1);
        });/*
        if (!isDetached() && v.post())
            animate(false, -1, -1);*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RootInfoFragmentListener) {
            mListener = (RootInfoFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public boolean onBackPressed() {
        animate(true, -1, -1);
        return true;
    }

    private void animate(boolean inverse, int cx, int cy) {
        if (animationInProgress || getView() == null)
            return;

        animationInProgress = true;

        if (cx == -1 || cy == -1) {
            cx = this.cx;
            cy = this.cy;
        }

        View root = getView();
        int dx = Math.max(cx, root.getWidth() - cx);
        int dy = Math.max(cy, root.getHeight() - cy);
        float startRadius = 0;
        float finalRadius = (float) Math.hypot(dx, dy) * 2;

        if (inverse) {
            startRadius = finalRadius;
            finalRadius = 0;
        }
        SupportAnimator animator = ViewAnimationUtils
                .createCircularReveal(root.findViewById(R.id.fragment_anim_layout), cx, cy, startRadius, finalRadius);
        animator.setDuration(500);

        // So supportanimator has some problems with finished callbacks...;
        Observable
                .just(null)
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(n -> {
                            animationInProgress = false;
                            if (inverse && mListener != null)
                                mListener.RequestClose();
                        },
                        ex -> Log.e(BaseUsosActivity.LOGTAG, Log.getStackTraceString(ex))
                );

        animator.start();
    }

    public static interface RootInfoFragmentListener {
        void RequestClose();
    }
}
