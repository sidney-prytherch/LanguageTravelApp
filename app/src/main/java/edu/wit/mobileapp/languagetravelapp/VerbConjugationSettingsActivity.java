package edu.wit.mobileapp.languagetravelapp;

import android.animation.LayoutTransition;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;


public class VerbConjugationSettingsActivity extends AppCompatActivity {

    private final float FULL_HEIGHT_PERCENT = (float) 0.7;
    private final float ZERO_HEIGHT_PERCENT = (float) 0;
    private ScrollView[] scrollViews;
    private Button[] buttons;
    private ConstraintLayout.LayoutParams[] scrollViewLayoutParams;
    private int selectedButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verb_conjugation_settings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ViewGroup) findViewById(R.id.constraintLayout)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
        }

        scrollViews = new ScrollView[]{
            findViewById(R.id.scrollView),
            findViewById(R.id.scrollView2),
            findViewById(R.id.scrollView3),
        };

        scrollViewLayoutParams = new ConstraintLayout.LayoutParams[]{
            (ConstraintLayout.LayoutParams) scrollViews[0].getLayoutParams(),
            (ConstraintLayout.LayoutParams) scrollViews[1].getLayoutParams(),
            (ConstraintLayout.LayoutParams) scrollViews[2].getLayoutParams()
        };

        buttons = new Button[]{
            findViewById(R.id.button),
            findViewById(R.id.button2),
            findViewById(R.id.button3)
        };

        selectedButtonId = buttons[0].getId();

    }

    public void buttonOnClick(View view) {
        focusOnScrollView(view.getId());
    }

    private void focusOnScrollView(int buttonId) {
        if (buttonId != selectedButtonId) {
            selectedButtonId = buttonId;
            for (int i = 0; i < scrollViewLayoutParams.length; i++) {
                if (buttons[i].getId() == buttonId) {
                    scrollViewLayoutParams[i].matchConstraintPercentHeight = FULL_HEIGHT_PERCENT;
                    buttons[i].setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.dropdown_arrow_down, 0,0,0);
                } else {
                    scrollViewLayoutParams[i].matchConstraintPercentHeight = ZERO_HEIGHT_PERCENT;
                    buttons[i].setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.dropdown_arrow_right,0,0,0);
                }
                scrollViews[i].setLayoutParams(scrollViewLayoutParams[i]);
            }
        }
    }

}
