package edu.wit.mobileapp.languagetravelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button crosswordButton = findViewById(R.id.crossword_button);
        crosswordButton.setOnClickListener((View v) -> {
            Log.v("MyApp", "Crossword button is clicked");
            Intent intent = new Intent(HomeActivity.this, CrosswordActivity.class);
            startActivity(intent);
        });

        Button wordsearchButton = findViewById(R.id.wordsearch_button);
        wordsearchButton.setOnClickListener((View v) -> {
            Log.v("MyApp", "Wordsearch button is clicked");
            Intent intent = new Intent(HomeActivity.this, WordsearchActivity.class);
            startActivity(intent);
        });

        Button travelButton = findViewById(R.id.travel_button);
        travelButton.setOnClickListener((View v) -> {
            Log.v("MyApp", "Travel button is clicked");
            Intent intent = new Intent(HomeActivity.this, TravelActivity.class);
            startActivity(intent);
        });

        Button verbConjugationButton = findViewById(R.id.verb_conjugation_button);
        verbConjugationButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(HomeActivity.this, VerbConjugationSettingsActivity.class);
            startActivity(intent);
        });
    }
}
