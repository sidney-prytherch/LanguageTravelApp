package edu.wit.mobileapp.languagetravelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LanguageHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_home);
    }

    public void goToLearnNew(View view) {
        Intent intent = new Intent(LanguageHomeActivity.this, LearnNewVocabSettings.class);
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
