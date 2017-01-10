package io.sixth.glassbook.utils;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;

import io.sixth.glassbook.AvailabilityScheduleFragment;
import io.sixth.glassbook.R;
import io.sixth.glassbook.data.api.AvailabilityAPI;
import io.sixth.glassbook.data.api.GlassBook;

/**
 * Created by Jorik on 10/01/2017.
 */

public class TimeAvailabilityAdapter extends RecyclerView.Adapter<TimeAvailabilityAdapter.ViewHolder> {

    private int daysFromNow;
    private AvailabilityScheduleFragment listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Button mView;

        public ViewHolder(View v) {
            super(v);
            mView = (Button) v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TimeAvailabilityAdapter(int days, AvailabilityScheduleFragment fragment) {
        daysFromNow = days;
        listener = fragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TimeAvailabilityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = new Button(GlassBook.app);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int startAdjustment = (daysFromNow > 0)?0:Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Button button = holder.mView;
        button.setText(String.format(GlassBook.app.getResources().getString(R.string.time_stamp),
                position + startAdjustment,
                (AvailabilityAPI.isTimeAvailable(position + startAdjustment, daysFromNow) ? "   AVAILABLE" : " UNAVAILABLE")));
        button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        button.setOnClickListener(listener);
        button.setTag(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (daysFromNow > 0)?24:24 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }
}