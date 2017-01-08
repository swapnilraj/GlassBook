package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ScrollingView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import io.sixth.glassbook.data.api.AvailabilityAPI;
import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.data.local.AvailabilityCache;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.FragmentUtils;
import io.sixth.glassbook.utils.MyDatePickerDialog;
import io.sixth.glassbook.utils.RealmManager;

import java.util.Calendar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * Created by thawne on 26/12/16.
 */

public class AvailabilityScheduleFragment extends Fragment
    implements View.OnClickListener, TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener, GlassBook.RequestListener,
    AdapterView.OnItemSelectedListener {

  public static String USER = "user";
  private TextView greeting;
  private Calendar startTime;
  private User user;
  private int roomNumber = 1;
  private View rootView;
  private Button[] availabilityButtons = new Button[24];

  public AvailabilityScheduleFragment() {
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_availability_schedule, null);

    Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
        R.array.rooms, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(this);

    greeting = (TextView) rootView.findViewById(R.id.greetingText);
    Button button = (Button) rootView.findViewById(R.id.buttonBook);
    button.setOnClickListener(this);

    updateButtons();

    user = getArguments().getParcelable(USER);
    if (user == null) {
      return rootView;
    }
    String name = user.getFirstName();
    greeting.append(" " + name);

    RealmManager.updateAvailabilityCache();
    return rootView;
  }

  @Override public void onClick(View v) {
//    Calendar now = Calendar.getInstance();
//    MyDatePickerDialog dpd =
//        MyDatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
//            now.get(Calendar.DAY_OF_MONTH));
//    dpd.setVersion(DatePickerDialog.Version.VERSION_2);
//    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    Calendar rightNow = Calendar.getInstance();
    startTime = Calendar.getInstance();
    startTime.set(Calendar.HOUR_OF_DAY, rightNow.get(Calendar.HOUR_OF_DAY) + Integer.parseInt(v.getTag().toString()));
    AvailabilityAPI.bookRoom(startTime, roomNumber, this);
  }

  @Override public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
    Snackbar.make(rootView, R.string.request_booking, Snackbar.LENGTH_LONG).show();
    AvailabilityAPI.bookRoom(startTime, roomNumber, this);
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    startTime = Calendar.getInstance();
    startTime.set(year, monthOfYear, dayOfMonth);
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd =
        TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), 0, true);
    tpd.setTimeInterval(1, 60, 60);
    if (dayOfMonth == now.get(Calendar.DATE)) {
      tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY), 0, 0);
    }
    tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
  }

  @Override public void onResult(final String response) {
    FragmentUtils.runOnUi(this, new Runnable() {
      @Override public void run() {
        View rootView = getView();

        if (rootView == null) {
          return;
        }

        if (response.contains("pending")) {
          Snackbar.make(rootView, R.string.fail, Snackbar.LENGTH_LONG).show();
        } else if (response.contains("Successful")) {
          Snackbar.make(rootView, R.string.success, Snackbar.LENGTH_LONG).show();
        }
        else {
          Snackbar.make(rootView, "Booking Failed", Snackbar.LENGTH_LONG).show();
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

  private void updateButtons() {
    LinearLayout buttonContainer = (LinearLayout) rootView.findViewById(R.id.buttonContainer);
    Calendar rightNow = Calendar.getInstance();
    for (int button = 0; button < availabilityButtons.length; button++) {
      if (availabilityButtons[button] != null)
        buttonContainer.removeView(availabilityButtons[button]);
      availabilityButtons[button] = new Button(GlassBook.app);
      availabilityButtons[button].setText(String.format(
              getResources().getString(R.string.time_stamp),
              ((rightNow.get(Calendar.HOUR_OF_DAY) + button) % 24)));
      availabilityButtons[button].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
      availabilityButtons[button].setOnClickListener(this);
      availabilityButtons[button].setTag(button);
      buttonContainer.addView(availabilityButtons[button]);
    }
  }

}