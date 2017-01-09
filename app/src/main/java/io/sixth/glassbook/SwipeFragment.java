package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.sixth.glassbook.utils.AvailabilityPagerAdapter;

/**
 * Created by Jorik on 08/01/2017.
 */

public class SwipeFragment extends Fragment{
    public ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_view, null);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        AvailabilityPagerAdapter adapter = new AvailabilityPagerAdapter(getChildFragmentManager());
        adapter.setParent(this);
        mViewPager.setAdapter(adapter);
        return mViewPager;
    }

    public void gotTo(int i) {
        this.mViewPager.setCurrentItem(i);
    }
}
