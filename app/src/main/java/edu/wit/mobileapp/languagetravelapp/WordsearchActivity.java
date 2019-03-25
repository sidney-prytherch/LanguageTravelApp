package edu.wit.mobileapp.languagetravelapp;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WordsearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordsearch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer, getApplicationContext(), this));
        navigationView.getMenu().getItem(6).setChecked(true);

        char[][] wordsearch = new char[][]{
            {'I', 'I', 'W', 'I', 'W', 'W', 'W', 'W', 'W'},
            {'W', 'I', 'W', 'I', 'W', 'I', 'I', 'I', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'W', 'I', 'I', 'I', 'W', 'I', 'W', 'I', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'W', 'I', 'W', 'I', 'W', 'I', 'I', 'I', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'W', 'I', 'I', 'I', 'W', 'I', 'W', 'I', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'I', 'W', 'I', 'I'}
        };


        LinearLayout wordsearchGrid = (LinearLayout) findViewById(R.id.wordsearch_grid);
        for (int i = 0; i < wordsearch.length; i++) {
            LinearLayout row = (LinearLayout) getLayoutInflater().inflate(R.layout.puzzle_table_row, null);
            row.setBackgroundColor(getResources().getColor(R.color.colorPuzzleWhiteSquareDefault));

            for (int j = 0; j < wordsearch.length; j++) {
                View a = getLayoutInflater().inflate(R.layout.wordsearch_white_square, row);
                ((TextView)row.getChildAt(j)).setText(Character.toString(wordsearch[i][j]));
            }
            wordsearchGrid.addView(row);
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

    public void onWhiteSquareClick(View v) {
        Log.v("myapp", "blah click blah");
    }

}
