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

public class VerbConjugationActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private int index = -1;
    private String[] finalVerbForms = new String[24];
    private String[] finalEnglishVerbs = new String[24];
    private String[] finalPortugueseVerbs = new String[24];
    private String[] finalPersons = new String[24];
    private String[] finalAnswers = new String[24];

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

        ArrayList<String> verbFormStrings = getIntent().getStringArrayListExtra("VERB_FORMS");
        ArrayList<String> verbTypeStrings = getIntent().getStringArrayListExtra("VERB_TYPES");
        int verbCount = getIntent().getIntExtra("VERB_SET", 10);


        VerbType[] verbTypes = new VerbType[verbTypeStrings.size()];
        for (int i = 0; i < verbTypes.length; i++) {
            String verbType = verbTypeStrings.get(i);
            if (verbType.equals(getString(R.string.irregular))) {
                verbTypes[i] = VerbType.IRREGULAR;
                continue;
            }
            if (verbType.equals(getString(R.string.regular_ar))) {
                verbTypes[i] = VerbType.AR;
                continue;
            }
            if (verbType.equals(getString(R.string.regular_er))) {
                verbTypes[i] = VerbType.ER;
                continue;
            }
            if (verbType.equals(getString(R.string.regular_ir))) {
                verbTypes[i] = VerbType.IR;
            }
        }


        VerbForm[] verbForms = new VerbForm[verbFormStrings.size()];
        for (int i = 0; i < verbForms.length; i++) {
            String verbForm = verbFormStrings.get(i);
            if (verbForm.equals(getString(R.string.present))) {
                verbForms[i] = VerbForm.PRES_IND;
                continue;
            }
            if (verbForm.equals(getString(R.string.imperfect))) {
                verbForms[i] = VerbForm.IMP_IND;
                continue;
            }
            if (verbForm.equals(getString(R.string.preterite))) {
                verbForms[i] = VerbForm.PRET_IND;
                continue;
            }
            if (verbForm.equals(getString(R.string.simple_pluperfect))) {
                verbForms[i] = VerbForm.SIMP_PLUP_IND;
                continue;
            }
            if (verbForm.equals(getString(R.string.future))) {
                verbForms[i] = VerbForm.FUT_IND;
                continue;
            }
            if (verbForm.equals(getString(R.string.conditional))) {
                verbForms[i] = VerbForm.COND_IND;
                continue;
            }
            if (verbForm.equals(getString(R.string.present_perfect))) {
                verbForms[i] = VerbForm.PRES_PERF;
                continue;
            }
            if (verbForm.equals(getString(R.string.pluperfect))) {
                verbForms[i] = VerbForm.PLUP;
                continue;
            }
            if (verbForm.equals(getString(R.string.future_perfect))) {
                verbForms[i] = VerbForm.FUT_PERF;
                continue;
            }
            if (verbForm.equals(getString(R.string.conditional_perfect))) {
                verbForms[i] = VerbForm.COND_PERF;
                continue;
            }
            if (verbForm.equals(getString(R.string.present_progressive))) {
                verbForms[i] = VerbForm.PRES_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.imperfect_progressive))) {
                verbForms[i] = VerbForm.IMP_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.preterite_progressive))) {
                verbForms[i] = VerbForm.PRET_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.simple_pluperfect_progressive))) {
                verbForms[i] = VerbForm.SIMP_PLUP_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.future_progressive))) {
                verbForms[i] = VerbForm.FUT_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.conditional_progressive))) {
                verbForms[i] = VerbForm.COND_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.present_perfect_progressive))) {
                verbForms[i] = VerbForm.PRES_PERF_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.pluperfect_progressive))) {
                verbForms[i] = VerbForm.PLUP_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.future_perfect_progressive))) {
                verbForms[i] = VerbForm.FUT_PERF_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.conditional_perfect_progressive))) {
                verbForms[i] = VerbForm.COND_PERF_PROG;
                continue;
            }
            if (verbForm.equals(getString(R.string.present_subjunctive))) {
                verbForms[i] = VerbForm.PRES_SUBJ;
                continue;
            }
            if (verbForm.equals(getString(R.string.imperfect_subjunctive))) {
                verbForms[i] = VerbForm.IMP_SUBJ;
                continue;
            }
            if (verbForm.equals(getString(R.string.future_subjunctive))) {
                verbForms[i] = VerbForm.FUT_SUBJ;
                continue;
            }
            if (verbForm.equals(getString(R.string.present_perfect_subjunctive))) {
                verbForms[i] = VerbForm.PRES_PERF_SUBJ;
                continue;
            }
            if (verbForm.equals(getString(R.string.pluperfect_subjunctive))) {
                verbForms[i] = VerbForm.PLUP_SUBJ;
                continue;
            }
            if (verbForm.equals(getString(R.string.future_perfect_subjunctive))) {
                verbForms[i] = VerbForm.FUT_PERF_SUBJ;
            }
        }


        InputStream is = getResources().openRawResource(R.raw.verbs);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line;
        try {
            ArrayList<String> arVerbs = new ArrayList<>();
            ArrayList<String> erVerbs = new ArrayList<>();
            ArrayList<String> irVerbs = new ArrayList<>();
            ArrayList<String> irregularVerbs = new ArrayList<>();
            ArrayList<ArrayList<String>> arVerbsEnglish = new ArrayList<>();
            ArrayList<ArrayList<String>> erVerbsEnglish = new ArrayList<>();
            ArrayList<ArrayList<String>> irVerbsEnglish = new ArrayList<>();
            ArrayList<ArrayList<String>> irregularVerbsEnglish = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] lineData = line.split("\\|");
                String portugueseVerb = lineData[0];
                String englishVerb = lineData[4];
                VerbType verbType = Conjugator.getVerbType(portugueseVerb);
                if (verbType != null) {
                    switch (verbType) {
                        case AR:
                            if (!arVerbs.contains(portugueseVerb) && arVerbs.size() <= verbCount) {
                                arVerbs.add(portugueseVerb);
                                arVerbsEnglish.add(new ArrayList<>());
                                arVerbsEnglish.get(arVerbsEnglish.size() - 1).add(englishVerb);
                            } else if (arVerbs.contains(portugueseVerb)) {
                                arVerbsEnglish.get(arVerbsEnglish.size() - 1).add(englishVerb);
                            }
                            break;
                        case ER:
                            if (!erVerbs.contains(portugueseVerb) && erVerbs.size() <= verbCount) {
                                erVerbs.add(portugueseVerb);
                                erVerbsEnglish.add(new ArrayList<>());
                                erVerbsEnglish.get(erVerbsEnglish.size() - 1).add(englishVerb);
                            } else if (erVerbs.contains(portugueseVerb)) {
                                erVerbsEnglish.get(erVerbsEnglish.size() - 1).add(englishVerb);
                            }
                            break;
                        case IR:
                            if (!irVerbs.contains(portugueseVerb) && irVerbs.size() <= verbCount) {
                                irVerbs.add(portugueseVerb);
                                irVerbsEnglish.add(new ArrayList<>());
                                irVerbsEnglish.get(irVerbsEnglish.size() - 1).add(englishVerb);
                            } else if (irVerbs.contains(portugueseVerb)) {
                                irVerbsEnglish.get(irVerbsEnglish.size() - 1).add(englishVerb);
                            }
                            break;
                        case IRREGULAR:
                            if (!irregularVerbs.contains(portugueseVerb) && irregularVerbs.size() <= verbCount) {
                                irregularVerbs.add(portugueseVerb);
                                irregularVerbsEnglish.add(new ArrayList<>());
                                irregularVerbsEnglish.get(irregularVerbsEnglish.size() - 1).add(englishVerb);
                            } else if (irregularVerbs.contains(portugueseVerb)) {
                                irregularVerbsEnglish.get(irregularVerbsEnglish.size() - 1).add(englishVerb);
                            }
                    }
                }
            }

            String[] selectedPortugueseVerbs = new String[24];
            String[] selectedEnglishDefinitions = new String[24];
            int verbsToSelect = 24 / verbTypes.length;
            int addedCount = 0;
            for (VerbType verbType : verbTypes) {
                switch (verbType) {
                    case IRREGULAR:
                        for (int i = 0; i < verbsToSelect; i++) {
                            if (irregularVerbs.size() > 0) {
                                int randomVerbIndex = (int) (Math.random() * irregularVerbs.size());
                                selectedPortugueseVerbs[addedCount] = irregularVerbs.get(randomVerbIndex);
                                ArrayList<String> englishVerbsToSelectFrom = irregularVerbsEnglish.get(randomVerbIndex);
                                selectedEnglishDefinitions[addedCount] = englishVerbsToSelectFrom.get((int) (Math.random() * englishVerbsToSelectFrom.size()));
                                addedCount++;
                            }
                        }
                        break;
                    case AR:
                        for (int i = 0; i < verbsToSelect; i++) {
                            if (arVerbs.size() > 0) {
                                int randomVerbIndex = (int) (Math.random() * arVerbs.size());
                                selectedPortugueseVerbs[addedCount] = arVerbs.get(randomVerbIndex);
                                ArrayList<String> englishVerbsToSelectFrom = arVerbsEnglish.get(randomVerbIndex);
                                selectedEnglishDefinitions[addedCount] = englishVerbsToSelectFrom.get((int) (Math.random() * englishVerbsToSelectFrom.size()));
                                addedCount++;
                            }
                        }
                        break;
                    case ER:
                        for (int i = 0; i < verbsToSelect; i++) {
                            if (erVerbs.size() > 0) {
                                int randomVerbIndex = (int) (Math.random() * erVerbs.size());
                                selectedPortugueseVerbs[addedCount] = erVerbs.get(randomVerbIndex);
                                ArrayList<String> englishVerbsToSelectFrom = erVerbsEnglish.get(randomVerbIndex);
                                selectedEnglishDefinitions[addedCount] = englishVerbsToSelectFrom.get((int) (Math.random() * englishVerbsToSelectFrom.size()));
                                addedCount++;
                            }
                        }
                        break;
                    case IR:
                        for (int i = 0; i < verbsToSelect; i++) {
                            if (irVerbs.size() > 0) {
                                int randomVerbIndex = (int) (Math.random() * irVerbs.size());
                                selectedPortugueseVerbs[addedCount] = irVerbs.get(randomVerbIndex);
                                ArrayList<String> englishVerbsToSelectFrom = irVerbsEnglish.get(randomVerbIndex);
                                selectedEnglishDefinitions[addedCount] = englishVerbsToSelectFrom.get((int) (Math.random() * englishVerbsToSelectFrom.size()));
                                addedCount++;
                            }
                        }
                        break;
                }
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String country = prefs.getString("country", "Brazil");
            boolean tuEnabled = prefs.getBoolean("tu_enabled", false);

            finalVerbForms = new String[24];
            finalEnglishVerbs = new String[24];
            finalPortugueseVerbs = new String[24];
            finalPersons = new String[24];
            finalAnswers = new String[24];

            for (int i = 0; i < selectedPortugueseVerbs.length; i++) {
                String selectedPortugueseVerb = selectedPortugueseVerbs[i];
                finalPortugueseVerbs[i] = selectedPortugueseVerb;
                finalEnglishVerbs[i] = selectedEnglishDefinitions[i];
                boolean portugal = country != null && country.equals("Portugal");
                String[][] conjugatedVerbs = Conjugator.conjugate(selectedPortugueseVerb, verbForms, portugal);
                if (conjugatedVerbs != null) {
                    int randomVerbFormIndex = (int) (Math.random() * verbForms.length);
                    if (verbForms[randomVerbFormIndex] != null && verbForms.length > 0 && conjugatedVerbs[randomVerbFormIndex] != null && conjugatedVerbs[randomVerbFormIndex].length == 6) {
                        int randomVerbIndex;
                        if (tuEnabled) {
                            randomVerbIndex = (int) (Math.random() * 6);
                        } else {
                            randomVerbIndex = (int) (Math.random() * 5);
                            if (randomVerbIndex >= 1) {
                                randomVerbIndex++;
                            }
                        }
                        switch (randomVerbIndex) {
                            case 0:
                                finalPersons[i] = "Eu";
                                break;
                            case 1:
                                finalPersons[i] = "Tu";
                                break;
                            case 2:
                                int randomYou = (int) (Math.random() * 3);
                                finalPersons[i] = randomYou == 0 ? "Você" : randomYou == 1 ? "Ele" : "Ela";
                                break;
                            case 3:
                                finalPersons[i] = "Nós";
                                break;
                            case 4:
                                finalPersons[i] = (portugal && (int) (Math.random()) == 0) ? "Vós" : "Vocês";
                                break;
                            case 5:
                                finalPersons[i] = (int) (Math.random() * 2) == 0 ? "Elas" : "Eles";
                        }
                        finalAnswers[i] = conjugatedVerbs[randomVerbFormIndex][randomVerbIndex];
                        switch (verbForms[randomVerbFormIndex]) {
                            case PRES_IND:
                                finalVerbForms[i] = getString(R.string.present);
                                break;
                            case PRET_IND:
                                finalVerbForms[i] = getString(R.string.preterite);
                                break;
                            case IMP_IND:
                                finalVerbForms[i] = getString(R.string.imperfect);
                                break;
                            case SIMP_PLUP_IND:
                                finalVerbForms[i] = getString(R.string.simple_pluperfect);
                                break;
                            case FUT_IND:
                                finalVerbForms[i] = getString(R.string.future);
                                break;
                            case COND_IND:
                                finalVerbForms[i] = getString(R.string.conditional);
                                break;
                            case PRES_PERF:
                                finalVerbForms[i] = getString(R.string.present_perfect);
                                break;
                            case PLUP:
                                finalVerbForms[i] = getString(R.string.pluperfect);
                                break;
                            case FUT_PERF:
                                finalVerbForms[i] = getString(R.string.future_perfect);
                                break;
                            case COND_PERF:
                                finalVerbForms[i] = getString(R.string.conditional_perfect);
                                break;
                            case PRES_PROG:
                                finalVerbForms[i] = getString(R.string.present_progressive);
                                break;
                            case PRET_PROG:
                                finalVerbForms[i] = getString(R.string.preterite_progressive);
                                break;
                            case IMP_PROG:
                                finalVerbForms[i] = getString(R.string.imperfect_progressive);
                                break;
                            case SIMP_PLUP_PROG:
                                finalVerbForms[i] = getString(R.string.simple_pluperfect_progressive);
                                break;
                            case FUT_PROG:
                                finalVerbForms[i] = getString(R.string.future_progressive);
                                break;
                            case COND_PROG:
                                finalVerbForms[i] = getString(R.string.conditional_progressive);
                                break;
                            case PRES_PERF_PROG:
                                finalVerbForms[i] = getString(R.string.present_perfect_progressive);
                                break;
                            case PLUP_PROG:
                                finalVerbForms[i] = getString(R.string.pluperfect_progressive);
                                break;
                            case FUT_PERF_PROG:
                                finalVerbForms[i] = getString(R.string.future_perfect_progressive);
                                break;
                            case COND_PERF_PROG:
                                finalVerbForms[i] = getString(R.string.conditional_perfect_progressive);
                                break;
                            case PRES_SUBJ:
                                finalVerbForms[i] = getString(R.string.present_subjunctive);
                                break;
                            case PRES_PERF_SUBJ:
                                finalVerbForms[i] = getString(R.string.present_perfect_subjunctive);
                                break;
                            case IMP_SUBJ:
                                finalVerbForms[i] = getString(R.string.imperfect_subjunctive);
                                break;
                            case PLUP_SUBJ:
                                finalVerbForms[i] = getString(R.string.pluperfect_subjunctive);
                                break;
                            case FUT_SUBJ:
                                finalVerbForms[i] = getString(R.string.future_subjunctive);
                                break;
                            case FUT_PERF_SUBJ:
                                finalVerbForms[i] = getString(R.string.future_perfect_subjunctive);
                                break;
                        }
                    }
                }
            }

            int[] indices = new int[24];
            for (int i = 0; i < indices.length; i++) {
                indices[i] = i;
            }
            Random rgen = new Random();  // Random number generator
            for (int i = 0; i < indices.length; i++) {
                int randomPosition = rgen.nextInt(indices.length);
                int temp = indices[i];
                indices[i] = indices[randomPosition];
                indices[randomPosition] = temp;
            }

            Log.v("AHfbS", indices[0] + " " + indices[1] + " " + indices[2] + " " + indices[3] + " " + indices[4] + " " + indices[5] + " " + indices[6] + " " + indices[7] + " " + indices[8] + " " + indices[9]);

            for (int i = 0; i < indices.length; i++) {
                String temp = finalVerbForms[i];
                finalVerbForms[i] = finalVerbForms[indices[i]];
                finalVerbForms[indices[i]] = temp;

                temp = finalEnglishVerbs[i];
                finalEnglishVerbs[i] = finalEnglishVerbs[indices[i]];
                finalEnglishVerbs[indices[i]] = temp;

                temp = finalPortugueseVerbs[i];
                finalPortugueseVerbs[i] = finalPortugueseVerbs[indices[i]];
                finalPortugueseVerbs[indices[i]] = temp;

                temp = finalPersons[i];
                finalPersons[i] = finalPersons[indices[i]];
                finalPersons[indices[i]] = temp;

                temp = finalAnswers[i];
                finalAnswers[i] = finalAnswers[indices[i]];
                finalAnswers[indices[i]] = temp;
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

    public void goToSolution(String input) {
        Log.v("bongleshnap", input);
        closeKeyboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment verbFragment = new VerbConjugationAnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VerbConjugationAnswerFragment.VERB_TYPE, finalVerbForms[index]);
        bundle.putString(VerbConjugationAnswerFragment.QUESTION, finalEnglishVerbs[index]);
        bundle.putString(VerbConjugationAnswerFragment.PORTUGUESE_VERB, finalPortugueseVerbs[index]);
        bundle.putString(VerbConjugationAnswerFragment.PERSON, finalPersons[index]);
        bundle.putString(VerbConjugationAnswerFragment.ANSWER, finalAnswers[index]);
        bundle.putString(VerbConjugationAnswerFragment.INPUT, input);
        verbFragment.setArguments(bundle);
        transaction.replace(R.id.container, verbFragment);
        transaction.commit();
    }

    public void goToPrevious() {
        int oldIndex = index;
        do {
            index--;
            if (index < 0) {
                index = oldIndex;
            }
        } while (finalVerbForms[index] == null);
        closeKeyboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment verbFragment = new VerbConjugationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VerbConjugationFragment.VERB_TYPE, finalVerbForms[index]);
        bundle.putString(VerbConjugationFragment.QUESTION, finalEnglishVerbs[index]);
        bundle.putString(VerbConjugationFragment.PORTUGUESE_VERB, finalPortugueseVerbs[index]);
        bundle.putString(VerbConjugationFragment.PERSON, finalPersons[index]);
        verbFragment.setArguments(bundle);
        transaction.replace(R.id.container, verbFragment);
        transaction.commit();
    }

    private void returnToLanguageHome() {
        Intent intent = new Intent(VerbConjugationActivity.this, LanguageHomeActivity.class);
        startActivity(intent);
    }

    public void goToNext() {
        do {
            index++;
            if (index == finalVerbForms.length) {
                returnToLanguageHome();
                return;
            }
        } while (finalVerbForms[index] == null);
        closeKeyboard();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment verbFragment = new VerbConjugationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VerbConjugationFragment.VERB_TYPE, finalVerbForms[index]);
        bundle.putString(VerbConjugationFragment.QUESTION, finalEnglishVerbs[index]);
        bundle.putString(VerbConjugationFragment.PORTUGUESE_VERB, finalPortugueseVerbs[index]);
        bundle.putString(VerbConjugationFragment.PERSON, finalPersons[index]);
        verbFragment.setArguments(bundle);
        transaction.replace(R.id.container, verbFragment);
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
