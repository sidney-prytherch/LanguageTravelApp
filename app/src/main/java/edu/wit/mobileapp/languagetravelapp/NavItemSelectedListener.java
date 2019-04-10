package edu.wit.mobileapp.languagetravelapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

public class NavItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Context context;
    private Activity activityContext;

    public NavItemSelectedListener(DrawerLayout drawerLayout, Context context, Activity activityContext) {
        super();
        this.drawerLayout = drawerLayout;
        this.context = context;
        this.activityContext = activityContext;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home && !(activityContext instanceof HomeActivity)) {
            Intent newAct = new Intent(context, HomeActivity.class);
            activityContext.startActivity(newAct);
        } else if (id == R.id.nav_travel && !(activityContext instanceof TravelActivity)) {
            Intent newAct = new Intent(context, TravelActivity.class);
            activityContext.startActivity(newAct);
        } else if (id == R.id.nav_language && !(activityContext instanceof LanguageHomeActivity)) {
            Intent newAct = new Intent(context, LanguageHomeActivity.class);
            activityContext.startActivity(newAct);
        } else if (id == R.id.nav_settings && !(activityContext instanceof SettingsActivity)) {
            Intent newAct = new Intent(context, SettingsActivity.class);
            activityContext.startActivity(newAct);
//        } else if (id == R.id.nav_learn_and_review && !(activityContext instanceof LearnNewVocabSettingsActivity)) {
//            Intent newAct = new Intent(context, LearnNewVocabSettingsActivity.class);
//            activityContext.startActivity(newAct);
        } else if (id == R.id.nav_crossword && !(activityContext instanceof CrosswordSettingsActivity)) {
            Intent intent = new Intent(activityContext, CrosswordSettingsActivity.class);
            activityContext.startActivity(intent);
//        } else if (id == R.id.nav_wordsearch && !(activityContext instanceof WordsearchActivity)) {
//            Intent newAct = new Intent(context, WordsearchActivity.class);
//            activityContext.startActivity(newAct);
        } else if (id == R.id.nav_conjugation_practice && !(activityContext instanceof VerbConjugationSettingsActivity)) {
            Intent newAct = new Intent(context, VerbConjugationSettingsActivity.class);
            activityContext.startActivity(newAct);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
