package edu.wit.mobileapp.languagetravelapp;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LoadingFragment extends Fragment {

    public LoadingFragment() {}

    public static LoadingFragment newInstance() {
        LoadingFragment fragment = new LoadingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        final AnimatedVectorDrawableCompat av = AnimatedVectorDrawableCompat.create(view.getContext(), R.drawable.animated_logo);

        ImageView icon = view.findViewById(R.id.loading_icon);
        icon.setImageDrawable(av);
        if (av != null) {
            av.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                private final Handler fHandler = new Handler(Looper.getMainLooper());

                @Override
                public void onAnimationEnd(Drawable drawable) {
                    AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) drawable;
                    fHandler.post(avd::start);
                }
            });
            final Animatable animatable = (Animatable) icon.getDrawable();
            animatable.start();
        }
        return view;
    }
}
