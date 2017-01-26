package edu.asu.msse.avishen.expensestracker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Copyright 2015 Avijit Singh Vishen.
 * <p/>
 * License to anyone who may want to rebuild and execute the application.
 * Right to Use given to TA and Instructor to build and evaluate the package
 * <p/>
 * Purpose: Adapter for the tab view
 *
 * @version April 24 , 2015
 * @author: Avijit Vishen avijit.vishen@asu.edu
 * Software Engineering, CIDSE, Arizona State University, Polytechnic Campus
 */
public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Summary", "Add Expense",  "Add Budget","Accounts" };
    private Context context;

    public TabsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
            public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return SummaryFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return AddFragment.newInstance();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return BudgetFragment.newInstance();
                case 3: // Fragment # 1 - This will show SecondFragment
                    return AccountsFragment.newInstance();
                default:
                    return null;
            }
        }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
