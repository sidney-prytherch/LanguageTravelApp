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
  }
}
