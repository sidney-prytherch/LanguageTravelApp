package edu.wit.mobileapp.languagetravelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class FlashcardActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private int index = -1;
    private String[] finalPortugueseWords = new String[24];
    private String[] finalEnglishWords = new String[24];
    private String[] finalEnglishDefinitions = new String[24];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer, getApplicationContext(), this));
        navigationView.getMenu().getItem(3).setChecked(true);


        InputStream is = getResources().openRawResource(R.raw.words);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line;
        try {
            ArrayList<String> englishwords = new ArrayList<>();
            ArrayList<String> definitions = new ArrayList<>();
            ArrayList<String> portugueseWords = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] lineData = line.split("\\|");
                englishwords.add(lineData[0]);
                portugueseWords.add(lineData[4]);
                definitions.add(lineData[3]);
            }

            finalPortugueseWords = new String[24];
            finalEnglishWords = new String[24];
            finalEnglishDefinitions = new String[24];
            ArrayList<Integer> selectedIndices = new ArrayList<>();

            for (int i = 0; i < 24; i++) {
                int randomWordIndex;
                do {
                    randomWordIndex = (int) (Math.random() * englishwords.size());
                } while (selectedIndices.contains(randomWordIndex) || definitions.get(randomWordIndex).toLowerCase().contains(englishwords.get(randomWordIndex)));
                selectedIndices.add(randomWordIndex);
                finalEnglishWords[i] = englishwords.get(randomWordIndex);
                finalPortugueseWords[i] = portugueseWords.get(randomWordIndex);
                finalEnglishDefinitions[i] = definitions.get(randomWordIndex);
            }

            int[] indices = new int[24];
            for (int i = 0; i < indices.length; i++) {
                indices[i] = i;
            }
            Random rgen = new Random();
            for (int i = 0; i < indices.length; i++) {
                int randomPosition = rgen.nextInt(indices.length);
                int temp = indices[i];
                indices[i] = indices[randomPosition];
                indices[randomPosition] = temp;
            }

            for (int i = 0; i < indices.length; i++) {
                String temp = finalPortugueseWords[i];
                finalPortugueseWords[i] = finalPortugueseWords[indices[i]];
                finalPortugueseWords[indices[i]] = temp;

                temp = finalEnglishWords[i];
                finalEnglishWords[i] = finalEnglishWords[indices[i]];
                finalEnglishWords[indices[i]] = temp;

                temp = finalEnglishDefinitions[i];
                finalEnglishDefinitions[i] = finalEnglishDefinitions[indices[i]];
                finalEnglishDefinitions[indices[i]] = temp;
            }

        } catch (IOException e) {
            Log.v("myapp", "error reading file");
        }

        if (savedInstanceState == null) {
            index = -1;
            goToNext();
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

    public void continueOnClick(View view) {
        index = -1;
        goToNext();
    }

    public void goToPrevious() {
        int oldIndex = index;
        do {
            index--;
            if (index < 0) {
                index = oldIndex;
            }
        } while (finalPortugueseWords[index] == null);
        closeKeyboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment flashcardFragment = new FlashcardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FlashcardFragment.PORT_WORD, finalPortugueseWords[index]);
        bundle.putString(FlashcardFragment.ENG_WORD, finalEnglishWords[index]);
        bundle.putString(FlashcardFragment.ENG_DEF, finalEnglishDefinitions[index]);
        flashcardFragment.setArguments(bundle);
        transaction.replace(R.id.container, flashcardFragment);
        transaction.commit();
    }

    private void returnToLanguageHome() {
        Intent intent = new Intent(FlashcardActivity.this, LanguageHomeActivity.class);
        startActivity(intent);
    }

    public void goToNext() {
        do {
            index++;
            if (index == finalPortugueseWords.length) {
                returnToLanguageHome();
                return;
            }
        } while (finalPortugueseWords[index] == null);
        closeKeyboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment flashcardFragment = new FlashcardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FlashcardFragment.PORT_WORD, finalPortugueseWords[index]);
        bundle.putString(FlashcardFragment.ENG_WORD, finalEnglishWords[index]);
        bundle.putString(FlashcardFragment.ENG_DEF, finalEnglishDefinitions[index]);
        flashcardFragment.setArguments(bundle);
        transaction.replace(R.id.container, flashcardFragment);
        transaction.commit();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

