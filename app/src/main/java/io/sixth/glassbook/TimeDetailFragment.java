package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Switch;

import java.util.Calendar;

import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.data.api.AvailabilityAPI;
import io.sixth.glassbook.utils.FragmentUtils;
import io.sixth.glassbook.utils.TimeDetailAdapter;

/**
 * Created by Jorik on 10/01/2017.
 */

public class TimeDetailFragment extends Fragment implements View.OnClickListener, GlassBook.RequestListener {
    private Calendar cal;
    private View rootView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TimeDetailAdapter mAdapter;
    public static TimeDetailFragment activeFragment;

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
        mRecyclerView = ((RecyclerView) rootView.findViewById(R.id.my_recycler_view));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TimeDetailAdapter(time, daysFromNow);
        mRecyclerView.setAdapter(mAdapter);


        activeFragment = this;
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Switch mSwitch = (Switch) v.findViewById(R.id.switch2);
        AvailabilityAPI.bookRoom(cal, ((int) v.getTag()) + 1, mSwitch.isChecked() , this);
    }

    @Override
    public void onResult(final String response) {
        FragmentUtils.runOnUi(this, new Runnable() {
            @Override public void run() {
                View rootView = getView();

                if (rootView == null) {
                    return;
                }

                if (response.contains("pending")) {
                    Snackbar.make(rootView, R.string.pending, Snackbar.LENGTH_LONG).show();
                } else if (response.contains("Successful")) {
                    Snackbar.make(rootView, R.string.success, Snackbar.LENGTH_LONG).show();
                } else if (response.contains("This Date is more than 7 days in advance"))
                    Snackbar.make(rootView, "Stupid no further than one week booking rule", Snackbar.LENGTH_LONG).show();
                else {
                    Snackbar.make(rootView, R.string.fail, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
