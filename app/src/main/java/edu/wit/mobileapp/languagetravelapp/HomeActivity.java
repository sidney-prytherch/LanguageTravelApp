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
