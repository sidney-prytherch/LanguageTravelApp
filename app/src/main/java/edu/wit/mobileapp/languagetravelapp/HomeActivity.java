package edu.wit.mobileapp.languagetravelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        TextView quickAccessButton = findViewById(R.id.quick_button);
        if (prefs == null) {
            loadSettings();
        } else {
            switch (Objects.requireNonNull(prefs.getString("quick-access button", "settings"))) {
                case "learn":
                    quickAccessButton.setText(R.string.learn_and_review_button);
                    break;
                case "verb":
                    quickAccessButton.setText(R.string.verb_conjugations_button);
                    break;
                case "crossword":
                    quickAccessButton.setText(R.string.crossword_button);
                    break;
                case "wordsearch":
                    quickAccessButton.setText(R.string.wordsearch_button);
                    break;
                default:
                    quickAccessButton.setText(R.string.quick_button);
                    break;
            }
        }
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
        loadTravel();
    }
    public void loadCrossword() {
        Intent intent = new Intent(HomeActivity.this, CrosswordSettingsActivity.class);
        startActivity(intent);
    }
    public void loadWordsearch() {
        Intent intent = new Intent(HomeActivity.this, WordsearchActivity.class);
        startActivity(intent);
    }
    public void loadTravel() {
        Intent intent = new Intent(HomeActivity.this, TravelActivity.class);
        startActivity(intent);
    }
    public void loadVerbConjugation() {
        Intent intent = new Intent(HomeActivity.this, VerbConjugationSettingsActivity.class);
        startActivity(intent);
    }
    public void loadLearn() {
        Intent intent = new Intent(HomeActivity.this, LearnNewVocabSettingsActivity.class);
        startActivity(intent);
    }
    public void loadSettings() {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goToUserSelectedScreen(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs == null) {
            loadSettings();
            return;
        }
        switch (Objects.requireNonNull(prefs.getString("quick-access button", "settings"))) {
            case "learn":
                loadLearn();
                break;
            case "verb":
                loadVerbConjugation();
                break;
            case "crossword":
                loadCrossword();
                break;
            case "wordsearch":
                loadWordsearch();
                break;
            default:
                loadSettings();
                break;
        }
    }
}
