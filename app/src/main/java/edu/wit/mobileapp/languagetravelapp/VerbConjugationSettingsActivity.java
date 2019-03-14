package edu.wit.mobileapp.languagetravelapp;

import android.animation.LayoutTransition;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;


public class VerbConjugationSettingsActivity extends AppCompatActivity {

    private final float FULL_HEIGHT_PERCENT = (float) 0.7;
    private final float ZERO_HEIGHT_PERCENT = (float) 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verb_conjugation_settings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ViewGroup) findViewById(R.id.constraintLayout)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
        }
    }

    public void buttonOnClick(View view) {
        focusOnScrollView(view.getId());
    }

    private void focusOnScrollView(int buttonId) {
        Log.v("myapp", "click event worked");
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        ScrollView scrollView2 = (ScrollView) findViewById(R.id.scrollView2);
        ScrollView scrollView3 = (ScrollView) findViewById(R.id.scrollView3);
        ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) scrollView.getLayoutParams();
        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) scrollView2.getLayoutParams();
        ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams) scrollView3.getLayoutParams();

        params1.matchConstraintPercentHeight = ZERO_HEIGHT_PERCENT;
        params3.matchConstraintPercentHeight = ZERO_HEIGHT_PERCENT;
        params2.matchConstraintPercentHeight = ZERO_HEIGHT_PERCENT;
        switch (buttonId) {
            case R.id.button:
                params1.matchConstraintPercentHeight = FULL_HEIGHT_PERCENT;
                break;
            case R.id.button2:
                params2.matchConstraintPercentHeight = FULL_HEIGHT_PERCENT;
                break;
            case R.id.button3:
                params3.matchConstraintPercentHeight = FULL_HEIGHT_PERCENT;
                break;
        }
        scrollView.setLayoutParams(params1);
        scrollView2.setLayoutParams(params2);
        scrollView3.setLayoutParams(params3);
    }

}
