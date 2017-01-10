package io.sixth.glassbook.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import io.sixth.glassbook.MainActivity;
import io.sixth.glassbook.R;
import io.sixth.glassbook.data.api.AvailabilityAPI;

/**
 * Created by Jorik on 09/01/2017.
 */

public class TimeDetailAdapter extends RecyclerView.Adapter<TimeDetailAdapter.ViewHolder> {

    private int parentTime;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TimeDetailAdapter(int time) {
        parentTime = time;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TimeDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_select_button, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View view = holder.mView;

        TextView name = (TextView) view.findViewById(R.id.roomName);
        name.setText("Room " + position);
        Switch mSwitch = (Switch) view.findViewById(R.id.switch2);
        mSwitch.setChecked(false);


        boolean firstHour = AvailabilityAPI.roomIsAvailable(position, parentTime, 0);
        boolean secondHour = AvailabilityAPI.roomIsAvailable(position, parentTime + 1, 0);
        if (!firstHour) {
            view.setBackgroundColor(view.getResources().getColor(R.color.unavailable));
            mSwitch.setVisibility(View.INVISIBLE);
        } else if (secondHour) {
            view.setBackgroundColor(view.getResources().getColor(R.color.available));
        }  else {
            view.setBackgroundColor(view.getResources().getColor(R.color.available));
            mSwitch.setVisibility(View.INVISIBLE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 9;
    }
}
