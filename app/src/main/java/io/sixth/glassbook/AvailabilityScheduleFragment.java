package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.utils.ActivityUtils;
import io.sixth.glassbook.utils.FragmentUtils;
import io.sixth.glassbook.utils.MyDatePickerDialog;
import io.sixth.glassbook.utils.TimeAvailabilityAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by thawne on 26/12/16.
 */

public class AvailabilityScheduleFragment extends Fragment
    implements View.OnClickListener,
    DatePickerDialog.OnDateSetListener, GlassBook.RequestListener,
    AdapterView.OnItemSelectedListener {

  public static final String DAY_CODE = "days_from_now";
  public static final String TIME = "time";

  private Calendar startTime;
  public int daysFromNow;
  private int roomNumber = 1;

  private SwipeFragment parent;
  private View rootView;
  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLayoutManager;
  private TimeAvailabilityAdapter mAdapter;
  private Button[] availabilityButtons = new Button[24];


  public AvailabilityScheduleFragment() {
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_availability_schedule, null);
    daysFromNow = getArguments().getInt(DAY_CODE);

    // Build Calendar
    startTime = Calendar.getInstance();
    TextView dateView = (TextView) rootView.findViewById(R.id.date_view);
    SimpleDateFormat format = new SimpleDateFormat("EEE MMMM d", Locale.ENGLISH);

    startTime.add(Calendar.DATE, daysFromNow);
    dateView.setText(format.format(startTime.getTime()).toUpperCase());
    dateView.setOnClickListener(this);

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.time_slot_recycler);

    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    mRecyclerView.setHasFixedSize(true);

    // use a linear layout manager
    mLayoutManager = new LinearLayoutManager(this.getContext());
    mRecyclerView.setLayoutManager(mLayoutManager);

    mAdapter = new TimeAvailabilityAdapter(daysFromNow, this);
    mRecyclerView.setAdapter(mAdapter);

    return rootView;
  }

  @Override public void onClick(View v) {

//    put in an enum / switch statement?
    if (v instanceof Button) {
      startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(v.getTag().toString()));

      Fragment fragment = new TimeDetailFragment();
      Bundle args = new Bundle();
      args.putInt(DAY_CODE, daysFromNow);
      args.putInt(TIME, Integer.parseInt(v.getTag().toString()));
      fragment.setArguments(args);
      ActivityUtils.addToBackStack(getActivity().getSupportFragmentManager(), fragment, R.id.container_main);

    } else if (v instanceof TextView) {
      Calendar now = Calendar.getInstance();
      MyDatePickerDialog dpd =
              MyDatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                      now.get(Calendar.DAY_OF_MONTH));
      dpd.setVersion(DatePickerDialog.Version.VERSION_2);
      dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    Calendar selected = Calendar.getInstance();
    selected.set(year, monthOfYear, dayOfMonth);
    long msDiff = selected.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
    long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

    parent.gotTo((int) daysDiff);
  }

  @Override public void onResult(final String response) {
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

  @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    roomNumber = position + 1;
  }

  @Override public void onNothingSelected(AdapterView<?> parent) {

  }

  public void setParent(Fragment fragment) {
    parent = (SwipeFragment) fragment;
  }

}