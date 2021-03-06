package edu.wit.mobileapp.languagetravelapp;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
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
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class CrosswordActivity extends AppCompatActivity {


    private CrosswordTask task;
    private int nextNumber;
    private CellNode[][] crosswordGrid;
    private NavigationView navigationView;
    private CrosswordFragment crosswordFragment = null;
    private MenuItem checkWordAlwaysItemView;
    private RootNode[] acrossRoots;
    private RootNode[] downRoots;
    //    private char[] letters = new char[]{
//            ' ', 'A', ' ', 'B', ' ',
//            'C', 'D', 'E', 'F', 'G',
//            ' ', 'H', 'I', 'J', ' ',
//            'K', 'L', 'M', 'N', 'O',
//            ' ', 'P', ' ', 'R', ' '};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v("myapp1", "menu made right now");
        getMenuInflater().inflate(R.menu.crossword_menu, menu);
        Log.v("myapp1", "" + menu);
        checkWordAlwaysItemView = menu.findItem(R.id.check_word_always_option);
        checkWordAlwaysItemView.setChecked(false);
        Log.v("myapp1", "" + checkWordAlwaysItemView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (crosswordFragment != null) {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.reveal_letter_option:
                    crosswordFragment.showLetter();
                    break;
                case R.id.reveal_word_option:
                    crosswordFragment.showLettersForWord();
                    break;
                case R.id.reveal_puzzle_option:
                    crosswordFragment.showLettersForPuzzle();
                    break;
                case R.id.check_letter_option:
                    crosswordFragment.checkLetter();
                    break;
                case R.id.check_word_option:
                    crosswordFragment.checkWord();
                    break;
                case R.id.check_word_always_option:
                    checkWordAlwaysItemView.setChecked(crosswordFragment.alwaysCheckWord());

            }
        } else {
//            fillCrossword();
//            char[] letters = new char[crosswordGrid.length * crosswordGrid.length];
//            for (int i = 0; i < crosswordGrid.length; i++) {
//                for (int j = 0; j < crosswordGrid.length; j++) {
//                    letters[i * crosswordGrid.length + j] = crosswordGrid[i][j].getSolutionLetter();
//                }
//            }

//            char[] letters = new char[]{
//                    ' ', 'A', ' ', 'B', ' ',
//                    'C', 'D', 'E', 'F', 'G',
//                    ' ', 'H', 'I', 'J', ' ',
//                    'K', 'L', 'M', 'N', 'O',
//                    ' ', 'P', ' ', 'R', ' '};

//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction transaction = fm.beginTransaction();
//            crosswordFragment = new CrosswordFragment();
//            Bundle bundle = new Bundle();
//            bundle.putCharArray(CrosswordFragment.CROSSWORD_SOLUTION, letters);
//            crosswordFragment.setArguments(bundle);
//            transaction.replace(R.id.container, crosswordFragment);
//            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (task != null) {
            task.cancel(false);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer, getApplicationContext(), this));
        navigationView.getMenu().getItem(5).setChecked(true);

        int size = getIntent().getIntExtra("SIZE", 9);


        int[][] potentialCrosswords;

        switch (size) {
            case 5:
                potentialCrosswords = new int[][]{
                        new int[]{0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0},
                        new int[]{1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0}
                };
                break;
            case 7:
                potentialCrosswords = new int[][]{
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                        new int[]{1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                        new int[]{0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1}
                };
                break;
            case 11:
                potentialCrosswords = new int[][]{
                        new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1},
                        new int[]{0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1},
                        new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1},
                        new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1}
                };
                break;
            case 13:
                potentialCrosswords = new int[][]{
                        new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                        new int[]{0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0},
                        new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0},
                        new int[]{0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
                        new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                        new int[]{0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0}
                };
                break;
            case 15:
                potentialCrosswords = new int[][]{
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1},
                        new int[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1}
                };
                break;
            case 9:
            default:
                potentialCrosswords = new int[][]{
                        new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                        new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                        new int[]{0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0},
                        new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0},
                        new int[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0},
                        new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0}
                };
                break;
        }

        int[] crosswordFromDB = potentialCrosswords[(int) (Math.random() * potentialCrosswords.length)];


        int crosswordDim = (int) Math.sqrt(crosswordFromDB.length * 2 - 1);

        crosswordGrid = new CellNode[crosswordDim][crosswordDim];

        int[][] crossword = new int[crosswordDim][crosswordDim];

        ArrayList<RootNode> acrossRootsList = new ArrayList<>();
        ArrayList<RootNode> downRootsList = new ArrayList<>();
        boolean[] wordLengthUsed = new boolean[crosswordDim];

        nextNumber = 1;

        for (int i = 0; i < crosswordFromDB.length; i++) {
            int x = i / crosswordDim;
            int y = i % crosswordDim;
            crossword[x][y] = crossword[crosswordDim - 1 - x][crosswordDim - 1 - y] = crosswordFromDB[i];
        }


        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword.length; j++) {
                if (crossword[i][j] == 0) {
                    //if it's the start of an across word:
                    if ((j == 0 || crossword[i][j - 1] == 1) && j != crosswordDim - 1 && crossword[i][j + 1] == 0) {
                        RootNode root = (RootNode) getOrCreateRootNode(i, j, null, null, WordOrientation.ACROSS, acrossRootsList.size());
                        acrossRootsList.add(root);
                        root.setRoot(WordOrientation.ACROSS, root);

                        CellNode left = root;

                        // create/get the CellNodes of the word associated with root, and set their roots
                        int k = j + 1;
                        while (k < crosswordDim && crossword[i][k] == 0) {
                            CellNode currentCellNode = getOrCreateCellNode(i, k, left, null);
                            currentCellNode.setRoot(WordOrientation.ACROSS, root);
                            left = currentCellNode;
                            k++;
                        }
                        int wordLength = k - j;
                        wordLengthUsed[wordLength - 1] = true;
                        root.setWordLength(WordOrientation.ACROSS, wordLength);
                    }
                    //if it's the start of a down word:
                    if ((i == 0 || crossword[i - 1][j] == 1) && i != crosswordDim - 1 && crossword[i + 1][j] == 0) {
                        RootNode root = (RootNode) getOrCreateRootNode(i, j, null, null, WordOrientation.DOWN, downRootsList.size());
                        downRootsList.add(root);
                        root.setRoot(WordOrientation.DOWN, root);

                        CellNode up = root;

                        // create/get the CellNodes of the word associated with root, and set their roots
                        int k = i + 1;
                        while (k < crosswordDim && crossword[k][j] == 0) {
                            CellNode currentCellNode = getOrCreateCellNode(k, j, null, up);
                            currentCellNode.setRoot(WordOrientation.DOWN, root);
                            up = currentCellNode;
                            k++;
                        }
                        int wordLength = k - i;
                        wordLengthUsed[wordLength - 1] = true;
                        root.setWordLength(WordOrientation.DOWN, wordLength);
                    }
                }
            }
        }

        acrossRoots = acrossRootsList.toArray(new RootNode[acrossRootsList.size()]);
        downRoots = downRootsList.toArray(new RootNode[downRootsList.size()]);
        for (RootNode rootNode : downRoots) {
            rootNode.setIndex(WordOrientation.DOWN, rootNode.getIndex(WordOrientation.DOWN) + acrossRoots.length);
        }
        int wordCount = acrossRoots.length + downRoots.length;


        ArrayList<VerbForm> verbFormsArrayList = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("present", false)) {
            verbFormsArrayList.add(VerbForm.PRES_IND);
        }
        if (prefs.getBoolean("preterite", false)) {
            verbFormsArrayList.add(VerbForm.PRET_IND);
        }
        if (prefs.getBoolean("imperfect", false)) {
            verbFormsArrayList.add(VerbForm.IMP_IND);
        }
        if (prefs.getBoolean("simple_pluperfect", false)) {
            verbFormsArrayList.add(VerbForm.SIMP_PLUP_IND);
        }
        if (prefs.getBoolean("future", false)) {
            verbFormsArrayList.add(VerbForm.FUT_IND);
        }
        if (prefs.getBoolean("conditional", false)) {
            verbFormsArrayList.add(VerbForm.COND_IND);
        }
        if (prefs.getBoolean("present_perfect", false)) {
            verbFormsArrayList.add(VerbForm.PRES_PERF);
        }
        if (prefs.getBoolean("pluperfect", false)) {
            verbFormsArrayList.add(VerbForm.PLUP);
        }
        if (prefs.getBoolean("future_perfect", false)) {
            verbFormsArrayList.add(VerbForm.FUT_PERF);
        }
        if (prefs.getBoolean("present_progressive", false)) {
            verbFormsArrayList.add(VerbForm.PRES_PROG);
        }
        if (prefs.getBoolean("preterite_progressive", false)) {
            verbFormsArrayList.add(VerbForm.PRET_PROG);
        }
        if (prefs.getBoolean("imperfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.IMP_PROG);
        }
        if (prefs.getBoolean("simple_pluperfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.SIMP_PLUP_PROG);
        }
        if (prefs.getBoolean("future_progressive", false)) {
            verbFormsArrayList.add(VerbForm.FUT_PROG);
        }
        if (prefs.getBoolean("conditional_progressive", false)) {
            verbFormsArrayList.add(VerbForm.COND_PROG);
        }
        if (prefs.getBoolean("present_perfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.PRES_PERF_PROG);
        }
        if (prefs.getBoolean("pluperfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.PLUP_PROG);
        }
        if (prefs.getBoolean("future_perfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.FUT_PERF_PROG);
        }
        if (prefs.getBoolean("conditional_perfect_progressive", false)) {
            verbFormsArrayList.add(VerbForm.COND_PERF_PROG);
        }
        if (prefs.getBoolean("present_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.PRES_SUBJ);
        }
        if (prefs.getBoolean("present_perfect_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.PRES_PERF_SUBJ);
        }
        if (prefs.getBoolean("imperfect_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.IMP_SUBJ);
        }
        if (prefs.getBoolean("pluperfect_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.PLUP_SUBJ);
        }
        if (prefs.getBoolean("future_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.FUT_SUBJ);
        }
        if (prefs.getBoolean("future_perfect_subjunctive", false)) {
            verbFormsArrayList.add(VerbForm.FUT_PERF_SUBJ);
        }
        boolean portugal = Objects.requireNonNull(prefs.getString("country", "")).equals("portugal");
        boolean tuEnabled = prefs.getBoolean("tu_enabled", false);

        VerbForm[] verbForms = new VerbForm[verbFormsArrayList.size()];
        for (int i = 0; i < verbFormsArrayList.size(); i++) {
            verbForms[i] = verbFormsArrayList.get(i);
        }

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment loadingFragment = new LoadingFragment();
            transaction.replace(R.id.container, loadingFragment);
            transaction.commit();
        }

//        prioritizedWords = getPrioritizedWordsByLength(wordLengthUsed);

//        fillCrossword();
        CrosswordTask crosswordTask = new CrosswordTask();
        crosswordTask.execute(new AsyncData(wordCount, acrossRoots, downRoots, wordLengthUsed, getResources(), verbForms, portugal, tuEnabled));


    }

    private class CrosswordTask extends AsyncTask<AsyncData, Void, Boolean> {

        AsyncData data;
        String[][][] prioritizedWords;

        @Override
        protected void onCancelled() {
            super.onCancelled();
            this.data = null;
            this.prioritizedWords = null;
        }

        @Override
        protected Boolean doInBackground(AsyncData... asyncData) {
            data = asyncData[0];
            prioritizedWords = getPrioritizedWordsByLength(data.wordLengthUsed);
            boolean[] nodeHasWord = new boolean[data.wordCount];
            ArrayList<RootNode> partiallyCompleteWords;
            ArrayList<WordOrientation> partiallyCompleteWordsOrientations;
            String[] selectedWords = new String[data.wordCount];
            int[] filledInCounts = new int[data.wordCount];
            Stack<RootNode> selectedNodes = new Stack<>();
            WordOrientation[] wordOrientations2 = new WordOrientation[data.wordCount];
            for (int i = 0; i < data.wordCount; i++) {
                wordOrientations2[i] = getRootNode(i).wordOrientation;
            }
            Tuple nextToBeSelected = getNextToBeSelected(nodeHasWord, null, null);
            WordOrientation wordOrientation = nextToBeSelected.wordOrientation;
            RootNode nodeToBeSelected = nextToBeSelected.rootNode;
            int nodeToBeSelectedIndex;

            while (nodeToBeSelected != null) {
                partiallyCompleteWords = new ArrayList<>(data.wordCount);
                partiallyCompleteWordsOrientations = new ArrayList<>(data.wordCount);
                nodeToBeSelectedIndex = nodeToBeSelected.getIndex(wordOrientation);
                String partialWord = nodeToBeSelected.getPatternString(wordOrientation);
//            Log.v("crosswordprinting", "pattern: " + partialWord);
                String[] wordAndHint = getNextWordFromPriorityList(nodeToBeSelected.getWordLength(wordOrientation), selectedWords, partialWord);
                String word = wordAndHint[0];
                String hint = wordAndHint[1];
                nodeToBeSelected.setWordSolution(wordOrientation, word, hint);
                nodeToBeSelected.finalizeSolution(wordOrientation);
                selectedWords[nodeToBeSelectedIndex] = word;
                nodeHasWord[nodeToBeSelectedIndex] = true;
                selectedNodes.push(nodeToBeSelected);
//            printCrossword();
                for (int i = 0; i < data.wordCount; i++) {
                    Tuple rootNodeData = getRootNode(i);
                    RootNode rootNode = rootNodeData.rootNode;
                    WordOrientation rootNodeWordOrientation = rootNodeData.wordOrientation;
                    int filledInCount = rootNode.getFilledInCount(rootNodeWordOrientation);
                    filledInCounts[i] = filledInCount;
                    if (filledInCount > 0 && rootNode.getWordLength(rootNodeWordOrientation) > filledInCount) {
                        partiallyCompleteWords.add(rootNode);
                        partiallyCompleteWordsOrientations.add(rootNodeWordOrientation);
                    }
                }


                nextToBeSelected = getNextToBeSelected(nodeHasWord, partiallyCompleteWords, partiallyCompleteWordsOrientations);
                wordOrientation = nextToBeSelected.wordOrientation;
                nodeToBeSelected = nextToBeSelected.rootNode;
            }
            for (RootNode root : data.acrossRoots) {
                root.finalizeSolution();
            }
            for (RootNode root : data.downRoots) {
                root.finalizeSolution();
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            switchToCrosswordFragment();
        }

        private String[][][] getPrioritizedWordsByLength(boolean[] wordLengthUsed) {
            String[][][] prioritizedWords = new String[2][wordLengthUsed.length][];
            ArrayList[] lists = new ArrayList[wordLengthUsed.length];
            ArrayList[] hintLists = new ArrayList[wordLengthUsed.length];
            for (int i = 0; i < wordLengthUsed.length; i++) {
                if (wordLengthUsed[i]) {
                    lists[i] = new ArrayList<String>();
                    hintLists[i] = new ArrayList<String>();
                }
            }
            InputStream is = data.resources.openRawResource(R.raw.words);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );
            String line;
            try {
                ArrayList<String> verbsConjugated = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] lineData = line.split("\\|");
                    String word = lineData[4].replaceAll(" ", "").replaceAll("-", "").replaceAll("\\?", "").replaceAll("!", "").replaceAll("\\.", "");
                    String hint = lineData[0] + " (" + lineData[3] + ")";
                    int wordLength = word.length();
                    int wordLengthMinusOne = wordLength - 1;
                    int maxWordLength = wordLengthUsed.length;
                    if (wordLength > 0 && wordLengthMinusOne < maxWordLength && wordLengthUsed[wordLengthMinusOne] &&
                            !lists[wordLengthMinusOne].contains(word)) {
                        lists[wordLengthMinusOne].add(word);
                        String finalHint = hint.substring(0, 1).toUpperCase() + hint.substring(1);
                        hintLists[wordLengthMinusOne].add(finalHint);
                    }
                    if (lineData[2].equals("v")) {
                        if (!verbsConjugated.contains(word)) {
                            verbsConjugated.add(word);
                            String[][] conjugatedVerbs = Conjugator.conjugate(lineData[4], data.verbForms, data.portugal);
                            if (conjugatedVerbs != null) {
                                for (int a = 0; a < conjugatedVerbs.length; a++) {
                                    String[] fullConjugation = conjugatedVerbs[a];
                                    String conjugatedForm = Conjugator.getVerbFormString(data.verbForms[a], data.resources).toLowerCase();
                                    if (fullConjugation != null && fullConjugation.length == 6) {
                                        conjugateLoop:
                                        for (int i = 0; i < 6; i++) {
                                            if (i == 1 && !data.tuEnabled || (i == 4 && !data.portugal)) {
                                                continue;
                                            }
                                            String subject = Conjugator.getSubject(i, data.portugal);
                                            String verbHintWithoutintro = " form, " + conjugatedForm + " tense of the verb " + hint;
                                            String verbHintWithIntro = subject + verbHintWithoutintro;
                                            String verbHintWithSubject = verbHintWithIntro + " (with subject)";
                                            String verbHintWithSubjectWithoutIntro = verbHintWithoutintro + " (with subject)";
                                            String conjugated = fullConjugation[i];
                                            if (conjugated != null) {
                                                int conjugatedLength = conjugated.length();
                                                int conjugatedLengthMinusOne = conjugatedLength - 1;
                                                if (conjugatedLength > 0) {
                                                    if (conjugatedLengthMinusOne < maxWordLength) {
                                                        if (wordLengthUsed[conjugatedLengthMinusOne]) {
                                                            lists[conjugatedLengthMinusOne].add(conjugated);
                                                            hintLists[conjugatedLengthMinusOne].add(verbHintWithIntro);
                                                        }
                                                        int conjugatedLengthPlusOne = conjugatedLength + 1;
                                                        int conjugatedLengthPlusTwo = conjugatedLength + 2;
                                                        if (conjugatedLengthPlusTwo < maxWordLength) {
                                                            if (wordLengthUsed[conjugatedLengthPlusOne]) {
                                                                switch (i) {
                                                                    case 0:
                                                                        lists[conjugatedLengthPlusOne].add("eu" + conjugated);
                                                                        Log.v("blahstuff", "eu " + conjugated + ": " + word);
                                                                        hintLists[conjugatedLengthPlusOne].add(verbHintWithSubject);
                                                                        continue conjugateLoop;
                                                                    case 1:
                                                                        lists[conjugatedLengthPlusOne].add("tu" + conjugated);
                                                                        hintLists[conjugatedLengthPlusOne].add(verbHintWithSubject);
                                                                        continue conjugateLoop;
                                                                }
                                                            }
                                                            int conjugatedLengthPlusThree = conjugatedLength + 3;
                                                            if (conjugatedLengthPlusThree < maxWordLength) {
                                                                if (wordLengthUsed[conjugatedLengthPlusTwo]) {
                                                                    switch (i) {
                                                                        case 2:
                                                                            lists[conjugatedLengthPlusTwo].add("ele" + conjugated);
                                                                            hintLists[conjugatedLengthPlusTwo].add("Ele" + verbHintWithSubjectWithoutIntro);
                                                                            lists[conjugatedLengthPlusTwo].add("ela" + conjugated);
                                                                            hintLists[conjugatedLengthPlusTwo].add("Ela" + verbHintWithSubjectWithoutIntro);
                                                                        case 3:
                                                                            lists[conjugatedLengthPlusTwo].add("nós" + conjugated);
                                                                            hintLists[conjugatedLengthPlusTwo].add(verbHintWithSubject);
                                                                            continue conjugateLoop;
                                                                        case 4:
                                                                                lists[conjugatedLengthPlusTwo].add("vós" + conjugated);
                                                                                hintLists[conjugatedLengthPlusTwo].add(verbHintWithSubject);

                                                                    }
                                                                }
                                                                int conjugatedLengthPlusFour = conjugatedLength + 4;
                                                                if (conjugatedLengthPlusFour < maxWordLength) {
                                                                    if (wordLengthUsed[conjugatedLengthPlusThree]) {
                                                                        switch (i) {
                                                                            case 2:
                                                                                lists[conjugatedLengthPlusThree].add("você" + conjugated);
                                                                                hintLists[conjugatedLengthPlusThree].add(verbHintWithSubject);
                                                                                continue conjugateLoop;
                                                                            case 5:
                                                                                lists[conjugatedLengthPlusThree].add("eles" + conjugated);
                                                                                hintLists[conjugatedLengthPlusThree].add("Eles" + verbHintWithSubjectWithoutIntro);
                                                                                lists[conjugatedLengthPlusThree].add("elas" + conjugated);
                                                                                hintLists[conjugatedLengthPlusThree].add("Elas" + verbHintWithSubjectWithoutIntro);
                                                                                continue conjugateLoop;
                                                                        }
                                                                    }
                                                                    int conjugatedLengthPlusFive = conjugatedLength + 5;
                                                                    if (conjugatedLengthPlusFive < maxWordLength && wordLengthUsed[conjugatedLengthPlusFour]) {
                                                                        if (i == 5) {
                                                                            lists[conjugatedLengthPlusFour].add("vocês" + conjugated);
                                                                            hintLists[conjugatedLengthPlusFour].add(verbHintWithSubject);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Log.v("myapp", "error reading file");
            }
            for (int i = 0; i < lists.length; i++) {
                if (wordLengthUsed[i]) {
                    prioritizedWords[0][i] = new String[lists[i].size()];
                    prioritizedWords[1][i] = new String[hintLists[i].size()];
                    for (int j = 0; j < lists[i].size(); j++) {
                        prioritizedWords[0][i][j] = (String) lists[i].get(j);
                        prioritizedWords[1][i][j] = (String) hintLists[i].get(j);
                    }

                    int[] indices = new int[prioritizedWords[0][i].length];
                    for (int j = 0; j < indices.length; j++) {
                        indices[j] = j;
                    }
                    Random rgen = new Random();
                    for (int j = 0; j < indices.length; j++) {
                        int randomPosition = rgen.nextInt(indices.length);
                        int temp = indices[j];
                        indices[j] = indices[randomPosition];
                        indices[randomPosition] = temp;
                    }

                    for (int j = 0; j < indices.length; j++) {
                        String temp = prioritizedWords[0][i][j];
                        prioritizedWords[0][i][j] = prioritizedWords[0][i][indices[j]];
                        prioritizedWords[0][i][indices[j]] = temp;

                        temp = prioritizedWords[1][i][j];
                        prioritizedWords[1][i][j] = prioritizedWords[1][i][indices[j]];
                        prioritizedWords[1][i][indices[j]] = temp;
                    }
                }
            }
            return prioritizedWords;
        }

        private Tuple getRootNode(int index) {
            // if all are selected
            if (index >= data.wordCount) {
                return new Tuple(null, null);
            }
            // if all across are selected, return next down
            if (index >= data.acrossRoots.length) {
                return new Tuple(WordOrientation.DOWN, data.downRoots[index - data.acrossRoots.length]);
            }
            // otherwise, return next across
            return new Tuple(WordOrientation.ACROSS, data.acrossRoots[index]);
        }

        // given the partiallyCompleteWords ordered by priority and ordered boolean array of complete words
        // return next word that should be selected
        private Tuple getNextToBeSelected(boolean[] nodeHasWord, ArrayList<RootNode> partiallyCompleteWords, ArrayList<WordOrientation> partiallyCompleteWordsOrientations) {
//        for (int i = 1; i < wordCount; i++) {
//            int currentIndex = i;
//            int previousIndex = i - 1;
//            WordOrientation currentWO = getRootNode(currentIndex).wordOrientation;
//            WordOrientation previousWO = getRootNode(previousIndex).wordOrientation;
//            while (previousIndex > -1 && partiallyCompleteWords[currentIndex] != null &&
//                    (partiallyCompleteWords[previousIndex] == null ||
//                            (partiallyCompleteWords[previousIndex].getFilledInCount(previousWO) > partiallyCompleteWords[currentIndex].getFilledInCount(currentWO)))) {
//                RootNode temp = partiallyCompleteWords[currentIndex];
//                partiallyCompleteWords[currentIndex] = partiallyCompleteWords[previousIndex];
//                partiallyCompleteWords[previousIndex] = temp;
//                previousIndex--;
//                currentIndex--;
//                currentWO = currentIndex > -1 ? getRootNode(currentIndex).wordOrientation : null;
//                previousWO = previousIndex > -1 ? getRootNode(previousIndex).wordOrientation : null;
//            }
//        }
//        String str = "";
//        for (int i = 0; i < wordCount; i++) {
//            if (partiallyCompleteWords[i] == null) {
//                str += "0, ";
//                continue;
//            }
//            str += partiallyCompleteWords[i].getFilledInCount(getRootNode(i).wordOrientation) + ", ";
//        }
//        Log.v("crosswordprinting", str);

            // get next partiallyCompleteWord
            if (partiallyCompleteWords != null && partiallyCompleteWords.size() > 0) {
                int index = 0;
                int length = partiallyCompleteWords.get(index).getFilledInCount(partiallyCompleteWordsOrientations.get(index));
                for (int i = 1; i < partiallyCompleteWords.size(); i++) {
                    int currentLength = partiallyCompleteWords.get(i).getFilledInCount(partiallyCompleteWordsOrientations.get(i));
                    if (currentLength > length) {
                        index = i;
                        length = currentLength;
                    }
                }
                return new Tuple(partiallyCompleteWordsOrientations.get(index), partiallyCompleteWords.get(index));
            }
            // find next incomplete word
            int next = 0;
            while (next < nodeHasWord.length && nodeHasWord[next]) {
                next++;
            }
            getRootNode(next);
            return getRootNode(next);
        }

        private String[] getNextWordFromPriorityList(int wordLength, String[] selectedWords, String pattern) {
            int currentBestPatternMatch = -1;
            int currentBestWordIndex = 0;
            for (String selectedWord : selectedWords) {
                if (selectedWord != null) {
                    Log.v("crosswordprinting", "a: " + selectedWord);
                }
            }

            wordLoop:
            for (int i = 0; i < prioritizedWords[0][wordLength - 1].length; i++) {
                for (String selectedWord : selectedWords) {
                    if (prioritizedWords[0][wordLength - 1][i].equals(selectedWord)) {
                        continue wordLoop;
                    }
                    int patternMatch = matchesPattern(prioritizedWords[0][wordLength - 1][i], pattern, wordLength);
                    if (patternMatch > currentBestPatternMatch) {
                        currentBestPatternMatch = patternMatch;
                        currentBestWordIndex = i;
                    }
                }
            }
            Log.v("crosswordprinting", "" + currentBestWordIndex + " - " + prioritizedWords[0][wordLength - 1].length);
            return new String[]{prioritizedWords[0][wordLength - 1][currentBestWordIndex], prioritizedWords[1][wordLength - 1][currentBestWordIndex]};
        }

        private int matchesPattern(String word, String pattern, int wordLength) {
            int count = 0;
            word = word.toUpperCase();
            pattern = pattern.toUpperCase();
            for (int i = 0; i < wordLength; i++) {
                if (pattern.charAt(i) == ' ' || pattern.charAt(i) == word.charAt(i)) {
                    count++;
                }
            }
            return count;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.getMenu().getItem(5).setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        fillCrossword();
    }

    // tries to find the associated CellNode for the WhiteSquare. If it doesn't exist,
    // create a new one and return that instead. Also connects previous CellNode
    private CellNode getOrCreateCellNode(int i, int j, CellNode left, CellNode up) {
        CellNode cellNode = crosswordGrid[i][j];
        if (cellNode == null) {
            cellNode = new CellNode(left, up, null);
            crosswordGrid[i][j] = cellNode;
        } else {
            cellNode.setPrev(left, up);
        }
        return cellNode;
    }

    // tries to find the associated CellNode for the WhiteSquare. If it doesn't exist,
    // create a new one and return that instead. Also connects previous CellNode
    private CellNode getOrCreateRootNode(int i, int j, CellNode left, CellNode up, WordOrientation wordOrientation, int index) {
        CellNode cellNode = crosswordGrid[i][j];
        if (cellNode == null) {
            cellNode = new RootNode(left, up, null, wordOrientation, index, nextNumber++);
            crosswordGrid[i][j] = cellNode;
        } else {
            if (!(cellNode instanceof RootNode)) {
                cellNode = new RootNode(cellNode, wordOrientation, index, nextNumber++);
                crosswordGrid[i][j] = cellNode;
            } else {
                ((RootNode) cellNode).setIndex(wordOrientation, index);
            }
            cellNode.setPrev(left, up);
        }
        return cellNode;
    }


    private class AsyncData {
        int wordCount;
        RootNode[] acrossRoots;
        RootNode[] downRoots;
        boolean[] wordLengthUsed;
        Resources resources;
        VerbForm[] verbForms;
        boolean portugal;
        boolean tuEnabled;

        public AsyncData(int wordCount, RootNode[] acrossRoots, RootNode[] downRoots, boolean[] wordLengthUsed,
                         Resources resources, VerbForm[] verbForms, boolean portugal, boolean tuEnabled) {
            this.wordCount = wordCount;
            this.acrossRoots = acrossRoots;
            this.downRoots = downRoots;
            this.wordLengthUsed = wordLengthUsed;
            this.resources = resources;
            this.verbForms = verbForms;
            this.portugal = portugal;
            this.tuEnabled = tuEnabled;
        }

    }

//    private void printCrossword() {
//        int gridSize = crosswordGrid.length;
//        String str = "a\n|";
//        for (int i = 0; i < gridSize; i++) {
//            for (int j = 0; j < gridSize; j++) {
//                if (crosswordGrid[i][j] == null) {
//                    str += "██ ";
//                } else {
//                    str += " " + crosswordGrid[i][j].getSolutionLetter() + " ";
//                }
//            }
//            str += "|\n|";
//        }
//        Log.v("crosswordprinting", str.substring(0, str.length() - 1));
//    }

    private void switchToCrosswordFragment() {
        int gridSize = crosswordGrid.length;
        char[] letters = new char[gridSize * gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (crosswordGrid[i][j] == null) {
                    letters[i * gridSize + j] = ' ';
                } else {
                    letters[i * gridSize + j] = crosswordGrid[i][j].getSolutionLetter();
                    Log.v("myapp", "hii" + (int) crosswordGrid[i][j].getSolutionLetter());
                }
            }
        }
//        printCrossword();
        String[] acrossHints = new String[acrossRoots.length];
        String[] downHints = new String[downRoots.length];
        for (int i = 0; i < acrossRoots.length; i++) {
            acrossHints[i] = acrossRoots[i].getHint(WordOrientation.ACROSS);
        }
        for (int i = 0; i < downRoots.length; i++) {
            downHints[i] = downRoots[i].getHint(WordOrientation.DOWN);
        }


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        crosswordFragment = new CrosswordFragment();
        Bundle bundle = new Bundle();
        bundle.putCharArray(CrosswordFragment.CROSSWORD_SOLUTION, letters);
        bundle.putStringArray(CrosswordFragment.ACROSS_HINTS, acrossHints);
        bundle.putStringArray(CrosswordFragment.DOWN_HINTS, downHints);
        crosswordFragment.setArguments(bundle);
        transaction.replace(R.id.container, crosswordFragment);
        transaction.commit();
    }

    private boolean isWordComplete(CellNode cellNode, WordOrientation wordOrientation) {
        CellNode currentCellNode = cellNode.getRoot(wordOrientation);
        while (currentCellNode != null) {
            String text = currentCellNode.getSquare().getText().toString();
            if (text.equals("") || isCharacterAccent(text.charAt(0))) {
                return false;
            }
            currentCellNode = currentCellNode.getNext(wordOrientation);
        }
        return true;
    }

    private boolean isCharacterAccent(char character) {
        if (character == '´' || character == '`' || character == '^' || character == '~' || character == '¨') {
            return true;
        }
        return false;
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

}
