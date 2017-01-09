package io.sixth.glassbook.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.sixth.glassbook.AvailabilityScheduleFragment;

/**
 * Created by Jorik on 08/01/2017.
 */

public class AvailabilityPagerAdapter extends FragmentStatePagerAdapter {

    public AvailabilityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new AvailabilityScheduleFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(AvailabilityScheduleFragment.DAY_CODE, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position);
    }
}
