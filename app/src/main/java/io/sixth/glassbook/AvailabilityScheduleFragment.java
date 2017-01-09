package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import io.sixth.glassbook.data.api.AvailabilityAPI;
import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.utils.FragmentUtils;
import io.sixth.glassbook.utils.MyDatePickerDialog;
import io.sixth.glassbook.utils.RealmManager;

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
  private Calendar startTime;
  private SwipeFragment parent;
  private int roomNumber = 1;
  private View rootView;
  private Button[] availabilityButtons = new Button[24];
  public int daysFromNow;


  public AvailabilityScheduleFragment() {
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_availability_schedule, null);


    daysFromNow = getArguments().getInt(DAY_CODE);

    startTime = Calendar.getInstance();
    TextView dateView = (TextView) rootView.findViewById(R.id.date_view);
    SimpleDateFormat format = new SimpleDateFormat("EEE MMMM d", Locale.ENGLISH);


    startTime.add(Calendar.DATE, daysFromNow);
    dateView.setText(format.format(startTime.getTime()).toUpperCase());
    dateView.setOnClickListener(this);

    RealmManager.updateAvailabilityCache(daysFromNow);
    updateButtons();
    return rootView;
  }

  @Override public void onClick(View v) {

//    put in an enum / switch statement?
    if (v instanceof Button) {
      startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(v.getTag().toString()));
      Snackbar.make(rootView, R.string.request_booking, Snackbar.LENGTH_LONG).show();
      AvailabilityAPI.bookRoom(startTime, roomNumber, this);
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
//          final String selector = "body > p:nth-child(3)";
//          final Document doc = Jsoup.parse(response);
//          final Elements ele = doc.select(selector);
//          final String metaContainer = ele.text();
//          final String content =
//              metaContainer.substring(0, metaContainer.indexOf(user.getFirstName()));
        }
      }
    });
  }

  @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    roomNumber = position + 1;
  }
  @Override public void onNothingSelected(AdapterView<?> parent) {

  }

  // move to a Realm change listener that updates the buttons as the cache does.
  private void updateButtons() {
    LinearLayout buttonContainer = (LinearLayout) rootView.findViewById(R.id.button_container);
    Calendar rightNow = Calendar.getInstance();
    int currentTime = ((daysFromNow == 0)?rightNow.get(Calendar.HOUR_OF_DAY):0);

    for (int button = 0; button < availabilityButtons.length; button++) {
      int time = currentTime + button;
      if (availabilityButtons[button] != null)
        buttonContainer.removeView(availabilityButtons[button]);
      if (time < 24) {
        availabilityButtons[button] = new Button(GlassBook.app);
        availabilityButtons[button].setText(String.format(
                getResources().getString(R.string.time_stamp),
                ((time) % 24),
                ((AvailabilityAPI.isTimeAvailable(time, daysFromNow) ? "   AVAILABLE" : " UNAVAILABLE"))));
        availabilityButtons[button].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        availabilityButtons[button].setOnClickListener(this);
        availabilityButtons[button].setTag(time);
        buttonContainer.addView(availabilityButtons[button]);
      }
    }
  }

  public void setParent(Fragment fragment) {
    parent = (SwipeFragment) fragment;
  }

}