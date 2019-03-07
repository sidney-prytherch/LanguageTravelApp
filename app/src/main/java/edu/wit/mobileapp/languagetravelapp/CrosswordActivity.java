package edu.wit.mobileapp.languagetravelapp;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


public class CrosswordActivity extends AppCompatActivity {

    Keyboard crosswordKeyboard;
    KeyboardView crosswordKeyboardView;
    PuzzleWhiteSquare selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        int[][] crossword = new int[][]{
            {1, 1, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 0, 1, 1}
        };


        LinearLayout crosswordGrid = (LinearLayout) findViewById(R.id.crossword_grid);
        for (int i = 0; i < crossword.length; i++) {
            LinearLayout row = (LinearLayout) getLayoutInflater().inflate(R.layout.puzzle_table_row, null);
            row.setBackgroundColor(getResources().getColor(R.color.colorDefaultBlack));

            for (int j = 0; j < crossword.length; j++) {
                if (crossword[i][j] == 0 || crossword[i][j] == 2) {
                    getLayoutInflater().inflate(R.layout.crossword_white_square, row);
                } else {
                    getLayoutInflater().inflate(R.layout.crossword_black_square, row);
                }
            }
            crosswordGrid.addView(row);
        }

        // Create the Keyboard
        crosswordKeyboard = new Keyboard(this, R.xml.keyboard);

        // Lookup the KeyboardView
        crosswordKeyboardView = (KeyboardView) findViewById(R.id.crossword_keyboard_view);
        // Attach the keyboard to the view
        crosswordKeyboardView.setPreviewEnabled(false);
        crosswordKeyboardView.setKeyboard(crosswordKeyboard);

        // Do not show the preview balloons
        //crosswordKeyboardView.setPreviewEnabled(false);

        // Install the key handler
        crosswordKeyboardView.setOnKeyboardActionListener(cwOnKeyboardActionListener);

        crosswordKeyboardView.setVisibility(View.VISIBLE);
        crosswordKeyboardView.setEnabled(true);
    }

    public void onWhiteSquareClick(View v) {
        Log.v("myapp", "blah click blah");
        if (selected != null) {
            selected.setBackgroundColor(getResources().getColor(R.color.colorPuzzleWhiteSquareDefault));
        }
        selected = (PuzzleWhiteSquare) v;
        selected.setBackgroundColor(getResources().getColor(R.color.colorPuzzleWhiteSquareSelected));
    }

    private KeyboardView.OnKeyboardActionListener cwOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            //Here check the primaryCode to see which key is pressed
            //based on the android:codes property
            if (selected != null) {
                if (primaryCode == -2) {
                    selected.setText("");
                } else {
                    selected.setText(Character.toString((char) primaryCode));
                }
            }
        }

        @Override
        public void onPress(int arg0) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeUp() {
        }
    };

}
