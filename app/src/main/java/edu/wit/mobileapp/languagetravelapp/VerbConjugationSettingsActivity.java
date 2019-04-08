package edu.wit.mobileapp.languagetravelapp;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Arrays;


public class VerbConjugationSettingsActivity extends AppCompatActivity {

    private final float FULL_HEIGHT_PERCENT = (float) 0.6;
    private final float ZERO_HEIGHT_PERCENT = (float) 0;
    private ScrollView[] scrollViews;
    private Button[] buttons;
    private ConstraintLayout.LayoutParams[] scrollViewLayoutParams;
    private int selectedButtonId;
    private CheckBox[] verbFormsCheckBoxes;
    private CheckBox[] verbTypesCheckBoxes;
    private RadioGroup verbSetRadioGroup;
    private RadioGroup fullOrIndividualRadioGroup;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verb_conjugation_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavItemSelectedListener(drawer, getApplicationContext(), this));
        navigationView.getMenu().getItem(4).setChecked(true);

        ((ViewGroup) findViewById(R.id.constraintLayout)).getLayoutTransition()
            .enableTransitionType(LayoutTransition.CHANGING);



        buttons = new Button[]{
            findViewById(R.id.verbTypesTabButton),
            findViewById(R.id.verbFormsTabButton),
            findViewById(R.id.verbSetTabButton)
        };

        selectedButtonId = buttons[0].getId();

        verbTypesCheckBoxes = new CheckBox[] {
            findViewById(R.id.regular_ar_checkbox),
            findViewById(R.id.regular_er_checkbox),
            findViewById(R.id.regular_ir_checkbox),
            findViewById(R.id.irregular_checkbox),
            findViewById(R.id.reflexive_checkbox),
        };

        setChecked(verbTypesCheckBoxes, new int[]{0, 1, 2, 3});

    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.verbFormsScrollView).setVisibility(View.VISIBLE);
        findViewById(R.id.verbSetScrollView).setVisibility(View.VISIBLE);

        scrollViews = new ScrollView[] {
                findViewById(R.id.verbTypesScrollView),
                findViewById(R.id.verbFormsScrollView),
                findViewById(R.id.verbSetScrollView),
        };

        scrollViewLayoutParams = new ConstraintLayout.LayoutParams[]{
                (ConstraintLayout.LayoutParams) scrollViews[0].getLayoutParams(),
                (ConstraintLayout.LayoutParams) scrollViews[1].getLayoutParams(),
                (ConstraintLayout.LayoutParams) scrollViews[2].getLayoutParams()
        };

        verbFormsCheckBoxes = new CheckBox[]{
                findViewById(R.id.presIndCheckbox),
                findViewById(R.id.impeIndCheckbox),
                findViewById(R.id.pretIndCheckbox),
                findViewById(R.id.plupIndCheckbox),
                findViewById(R.id.futuIndCheckbox),
                findViewById(R.id.condIndCheckbox),

                findViewById(R.id.presPerfCheckbox),
                findViewById(R.id.plupPerfCheckbox),
                findViewById(R.id.futuPerfCheckbox),
                findViewById(R.id.condPerfCheckbox),

                findViewById(R.id.presProgCheckbox),
                findViewById(R.id.impeProgCheckbox),
                findViewById(R.id.pretProgCheckbox),
                findViewById(R.id.simpPlupProgCheckbox),
                findViewById(R.id.futuProgCheckbox),
                findViewById(R.id.condProgCheckbox),
                findViewById(R.id.presPerfProgCheckbox),
                findViewById(R.id.plupProgCheckbox),
                findViewById(R.id.futuPerfProgCheckbox),

                findViewById(R.id.presSubjCheckbox),
                findViewById(R.id.impeSubjCheckbox),
                findViewById(R.id.futuSubjCheckbox),
                findViewById(R.id.presPerfSubjCheckbox),
                findViewById(R.id.plupSubjCheckbox),
                findViewById(R.id.futuPerfSubjCheckbox),

                findViewById(R.id.pastPartCheckbox),
                findViewById(R.id.commandsCheckbox),
                findViewById(R.id.personalInfinitiveCheckbox),
                findViewById(R.id.gerundCheckbox),
        };


        SeekBar seekBar = findViewById(R.id.verbFormsSlider);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCheckBoxes(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Write code to perform some action when touch is started.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateCheckBoxes(seekBar);
            }
        });

        updateCheckBoxes(seekBar);

        verbSetRadioGroup = findViewById(R.id.verbSetRadioGroup);
        verbSetRadioGroup.check(R.id.strugglesRadioButton);

        fullOrIndividualRadioGroup = findViewById(R.id.fullOrIndividual);
        fullOrIndividualRadioGroup.check(R.id.individualConjugation);

        for (Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    focusOnScrollView(v.getId());
                }
            });
        }
        ((Button) findViewById(R.id.continueButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueToVerbPractice(v);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.getMenu().getItem(4).setChecked(true);
    }

    public void onButtonClickSpecial(View view) {
        onButtonClick(buttons[0]);
        continueToVerbPractice(view);
    }

    public void onButtonClick(View view) {
        focusOnScrollView(view.getId());
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

    private void updateCheckBoxes(SeekBar seekBar) {
        switch (seekBar.getProgress()) {
            case 0:
                setChecked(verbFormsCheckBoxes, new int[]{0, 2});
                break;
            case 1:
                setChecked(verbFormsCheckBoxes, new int[]{0, 1, 2, 10, 25, 28});
                break;
            case 2:
                setChecked(verbFormsCheckBoxes, new int[]{0, 1, 2, 10, 25, 28});
                break;
            case 3:
                setChecked(verbFormsCheckBoxes, new int[]{0, 1, 2, 10, 25, 28});
                break;
            case 4:
                setChecked(verbFormsCheckBoxes, new int[]{0, 1, 2, 10, 25, 28});
                break;
            case 5:
                setChecked(verbFormsCheckBoxes, new int[]{0, 1, 2, 3, 4, 5, 10, 25, 28});
                break;
        }
    }

    private void focusOnScrollView(int buttonId) {
        if (buttonId != selectedButtonId) {
            selectedButtonId = buttonId;
            for (int i = 0; i < scrollViewLayoutParams.length; i++) {
                if (buttons[i].getId() == buttonId) {
                    scrollViews[i].scrollTo(0,0);
                    scrollViewLayoutParams[i].matchConstraintPercentHeight = FULL_HEIGHT_PERCENT;
                    buttons[i].setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_dropdown_arrow_down, 0, 0, 0);
                } else {
                    scrollViewLayoutParams[i].matchConstraintPercentHeight = ZERO_HEIGHT_PERCENT;
                    buttons[i].setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_dropdown_arrow_right, 0, 0, 0);
                }
                scrollViews[i].setLayoutParams(scrollViewLayoutParams[i]);
            }
        }
    }


    private void setChecked(CheckBox[] allCheckboxes, int[] checkboxesToCheck) {
        Arrays.sort(checkboxesToCheck);
        int currentIndex = 0;
        for (int i = 0; i < allCheckboxes.length; i++) {
            if (currentIndex < checkboxesToCheck.length &&
                i == checkboxesToCheck[currentIndex]) {
                if (!allCheckboxes[i].isChecked()) {
                    allCheckboxes[i].setChecked(true);
                }
                currentIndex++;
            } else {
                allCheckboxes[i].setChecked(false);
            }
        }
    }

    public void continueToVerbPractice(View view) {
        Intent intent = new Intent(VerbConjugationSettingsActivity.this, VerbConjugationActivity.class);

        ArrayList<String> checkedVerbforms = new ArrayList<String>();
        for(int i=0; i<verbFormsCheckBoxes.length; i++){
            if(verbFormsCheckBoxes[i].isChecked()){
                checkedVerbforms.add(verbFormsCheckBoxes[i].getText().toString());
            }
        }
        intent.putExtra("VERB_FORMS", checkedVerbforms);

        ArrayList<Boolean> checkedVerbtypes = new ArrayList<Boolean>();
        for(int i=0; i<verbTypesCheckBoxes.length; i++){
            if(verbTypesCheckBoxes[i].isChecked()){
                checkedVerbtypes.add(true);
            }else {
                checkedVerbtypes.add(false);
            }
        }
        intent.putExtra("VERB_TYPES", checkedVerbtypes);

        String checkedVerbset = ((RadioButton)findViewById(verbSetRadioGroup.getCheckedRadioButtonId())).getText().toString();
        intent.putExtra("VERB_SET", checkedVerbset);
        startActivity(intent);
    }

}
