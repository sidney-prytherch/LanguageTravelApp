package edu.wit.mobileapp.languagetravelapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
public class TravelFragmentAdapter extends FragmentPagerAdapter{
    int numberOfTabs;
    public TravelFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FoodFragment tab1 = new FoodFragment();
                return tab1;
            case 1:
                LocationFragment tab2 = new LocationFragment();
                return tab2;
            case 2:
                TipsFragment tab3 = new TipsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
