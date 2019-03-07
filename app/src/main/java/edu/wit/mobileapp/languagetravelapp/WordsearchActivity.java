package edu.wit.mobileapp.languagetravelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WordsearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordsearch);

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

    public void onWhiteSquareClick(View v) {
        Log.v("myapp", "blah click blah");
    }

}
