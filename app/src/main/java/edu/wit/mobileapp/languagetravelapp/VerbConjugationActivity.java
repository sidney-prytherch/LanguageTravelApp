package edu.wit.mobileapp.languagetravelapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class VerbConjugationActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private int index = 1;
    private String[] questions = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verb_conjugation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer, getApplicationContext(), this));
        navigationView.getMenu().getItem(4).setChecked(true);

        InputStream is = getResources().openRawResource(R.raw.words);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line;
        try {
            ArrayList<String> verbs = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] lineData = line.split("\\|");
                String portugueseVerb = lineData[0];
                String englishVerb = lineData[4];

            }

        } catch (IOException e) {
            Log.v("myapp", "error reading file");
        }


        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment verbFragment = new VerbConjugationFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VerbConjugationFragment.VERB_TYPE, "Preterite");
            bundle.putString(VerbConjugationFragment.ENGLISH_VERB, "To Stay");
            bundle.putString(VerbConjugationFragment.PORTUGUESE_VERB, "Ficar");
            bundle.putString(VerbConjugationFragment.PERSON, "2nd");
            bundle.putBoolean(VerbConjugationFragment.SINGULAR, false);
            bundle.putString(VerbConjugationFragment.QUESTION, "I was");
            bundle.putString(VerbConjugationFragment.ANSWER, "eu ficou");
            verbFragment.setArguments(bundle);
            transaction.replace(R.id.container, verbFragment);
            transaction.commit();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.getMenu().getItem(4).setChecked(true);
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

    public void nextVerb(View view) {
        Intent intent = new Intent(VerbConjugationActivity.this, VerbConjugationActivity.class);
        setIntent(intent);
    }

    public void goToSolution(String input) {
        Log.v("bongleshnap", input);
        closeKeyboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment verbFragment = new VerbConjugationAnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VerbConjugationAnswerFragment.VERB_TYPE, "Preterite");
        bundle.putString(VerbConjugationAnswerFragment.ENGLISH_VERB, "To Stay");
        bundle.putString(VerbConjugationAnswerFragment.PORTUGUESE_VERB, "Ficar");
        bundle.putString(VerbConjugationAnswerFragment.PERSON, "2nd");
        bundle.putBoolean(VerbConjugationAnswerFragment.SINGULAR, false);
        bundle.putString(VerbConjugationAnswerFragment.QUESTION, "I was");
        bundle.putString(VerbConjugationAnswerFragment.ANSWER, "eu ficou");
        bundle.putString(VerbConjugationAnswerFragment.INPUT, input);
        verbFragment.setArguments(bundle);
        transaction.replace(R.id.container, verbFragment);
        transaction.commit();
    }

    public void goToPrevious() {
        if (index > 0) {
            closeKeyboard();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment verbFragment = new VerbConjugationFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VerbConjugationFragment.VERB_TYPE, "Preterite");
            bundle.putString(VerbConjugationFragment.ENGLISH_VERB, "To Stay");
            bundle.putString(VerbConjugationFragment.PORTUGUESE_VERB, "Ficar");
            bundle.putString(VerbConjugationFragment.PERSON, "2nd");
            bundle.putBoolean(VerbConjugationFragment.SINGULAR, false);
            bundle.putString(VerbConjugationFragment.QUESTION, "I was");
            bundle.putString(VerbConjugationFragment.ANSWER, "eu ficou");
            verbFragment.setArguments(bundle);
            transaction.replace(R.id.container, verbFragment);
            transaction.commit();
        }
    }

    public void goToNext() {
        if (index < questions.length) {
            closeKeyboard();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment verbFragment = new VerbConjugationFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VerbConjugationFragment.VERB_TYPE, "Preterite");
            bundle.putString(VerbConjugationFragment.ENGLISH_VERB, "To Stay");
            bundle.putString(VerbConjugationFragment.PORTUGUESE_VERB, "Fica");
            bundle.putString(VerbConjugationFragment.PERSON, "2nd");
            bundle.putBoolean(VerbConjugationFragment.SINGULAR, false);
            bundle.putString(VerbConjugationFragment.QUESTION, "I was");
            bundle.putString(VerbConjugationFragment.ANSWER, "eu ficou");
            verbFragment.setArguments(bundle);
            transaction.replace(R.id.container, verbFragment);
            transaction.commit();
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
