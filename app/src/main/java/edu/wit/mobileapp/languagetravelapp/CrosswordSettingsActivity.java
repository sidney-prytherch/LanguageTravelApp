package edu.wit.mobileapp.languagetravelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Objects;

public class CrosswordSettingsActivity extends AppCompatActivity {

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer, getApplicationContext(), this));
        navigationView.getMenu().getItem(5).setChecked(true);

        RadioGroup sizeRadioGroup = findViewById(R.id.crossword_size);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs != null) {
            switch (Objects.requireNonNull(prefs.getString("crossword_size", "9x9"))) {
                case "5":
                    sizeRadioGroup.check(R.id.size5x5);
                    break;
                case "7":
                    sizeRadioGroup.check(R.id.size7x7);
                    break;
                case "11":
                    sizeRadioGroup.check(R.id.size11x11);
                    break;
                case "13":
                    sizeRadioGroup.check(R.id.size13x13);
                    break;
                case "15":
                    sizeRadioGroup.check(R.id.size15x15);
                    break;
                default:
                    sizeRadioGroup.check(R.id.size9x9);
                    break;
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.getMenu().getItem(5).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void goToCrossword(View view) {
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        mainLayout.removeAllViews();

        ConstraintLayout loadingLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.loading_icon_image, mainLayout, false);
        mainLayout.addView(loadingLayout);

        final AnimatedVectorDrawableCompat av = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animated_logo);

        ImageView icon = (ImageView) loadingLayout.getChildAt(0);
        icon.setImageDrawable(av);
        if (av != null) {
            av.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                private final Handler fHandler = new Handler(Looper.getMainLooper());

                @Override
                public void onAnimationEnd(Drawable drawable) {
                    AnimatedVectorDrawable avd = (AnimatedVectorDrawable) drawable;
                    fHandler.post(avd::start);
                }
            });
            final Animatable animatable = (Animatable) icon.getDrawable();
            animatable.start();
        }
        loadCrossword();
    }

    public void loadCrossword() {
        Intent intent = new Intent(CrosswordSettingsActivity.this, CrosswordActivity.class);
        startActivity(intent);
    }

    private static class AsyncCrossword extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
