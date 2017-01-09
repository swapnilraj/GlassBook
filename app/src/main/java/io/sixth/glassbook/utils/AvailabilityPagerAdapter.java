package io.sixth.glassbook.utils;

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

    private Fragment parent;

    @Override
    public Fragment getItem(int i) {
        AvailabilityScheduleFragment fragment = new AvailabilityScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(AvailabilityScheduleFragment.DAY_CODE, i);
        fragment.setArguments(args);
        fragment.setParent(parent);
        return fragment;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position);
    }

    public void setParent(Fragment fragment) {
        this.parent = fragment;
    }
}
