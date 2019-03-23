package edu.wit.mobileapp.languagetravelapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LanguageHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer));
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

    public void goToLearnNew(View view) {
        Intent intent = new Intent(LanguageHomeActivity.this, FlashcardActivity.class);
        startActivity(intent);
    }

    public void goToReview(View view) {
        Intent intent = new Intent(LanguageHomeActivity.this, ReviewSettingsActivity.class);
        startActivity(intent);
    }

    public void goToTest(View view) {
        Intent intent = new Intent(LanguageHomeActivity.this, VocabTestSettingsActivity.class);
        startActivity(intent);
    }

    public void goToVerbConjugation(View view) {
        Intent intent = new Intent(LanguageHomeActivity.this, VerbConjugationSettingsActivity.class);
        startActivity(intent);
    }

    public void goToCrossword(View view) {
        Intent intent = new Intent(LanguageHomeActivity.this, CrosswordActivity.class);
        startActivity(intent);
    }

    public void goToWordSearch(View view) {
        Intent intent = new Intent(LanguageHomeActivity.this, WordsearchActivity.class);
        startActivity(intent);
    }
}
