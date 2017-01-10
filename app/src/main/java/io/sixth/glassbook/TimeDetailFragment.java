package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Calendar;

import io.sixth.glassbook.utils.TimeDetailAdapter;

/**
 * Created by Jorik on 10/01/2017.
 */

public class TimeDetailFragment extends Fragment {
    private Calendar cal;
    private View rootView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TimeDetailAdapter mAdapter;

    public TimeDetailFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Establish time and date
        int daysFromNow = getArguments().getInt(AvailabilityScheduleFragment.DAY_CODE);
        int time = getArguments().getInt(AvailabilityScheduleFragment.TIME);

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysFromNow);
        cal.set(Calendar.HOUR_OF_DAY, time);

        //get list
        rootView = inflater.inflate(R.layout.time_detail, null);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TimeDetailAdapter(time, daysFromNow);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
