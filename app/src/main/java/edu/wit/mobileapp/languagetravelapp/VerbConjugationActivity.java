package edu.wit.mobileapp.languagetravelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class VerbConjugationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verb_conjugation);
    }

    public void nextVerb(View view) {
        Intent intent = new Intent(VerbConjugationActivity.this, VerbConjugationActivity.class);
        setIntent(intent);
    }
}
