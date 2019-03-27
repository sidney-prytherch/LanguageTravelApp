package edu.wit.mobileapp.languagetravelapp;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

//    final AnimatedVectorDrawableCompat av = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.animated_logo);
//
//    ImageView icon = findViewById(R.id.travel_icon);
//        icon.setImageDrawable(av);
//        av.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
//        private final Handler fHandler = new Handler(Looper.getMainLooper());
//
//        @Override
//        public void onAnimationEnd(Drawable drawable) {
//            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) drawable;
//            fHandler.post(avd::start);
//        }
//    });
//    final Animatable animatable = (Animatable) icon.getDrawable();
//        animatable.start();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer, getApplicationContext(), this));
        navigationView.getMenu().getItem(0).setChecked(true);


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

    public void goToLanguageHome(View view) {
        Intent intent = new Intent(HomeActivity.this, LanguageHomeActivity.class);
        startActivity(intent);
    }

    public void goToTravel(View view) {
        Intent intent = new Intent(HomeActivity.this, TravelActivity.class);
        startActivity(intent);
    }

    public void goToUserSelectedScreen(View view) {
        Intent intent = new Intent(HomeActivity.this, LanguageHomeActivity.class);
        startActivity(intent);
    }
}
