package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.FragmentUtils;
import io.sixth.glassbook.utils.MyDatePickerDialog;
import java.io.IOException;
import java.util.Calendar;
import okhttp3.Response;

/**
 * Created by thawne on 26/12/16.
 */

public class AvailabilityScheduleFragment extends Fragment
    implements View.OnClickListener, TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener, GlassBook.RequestListener {

  public static String USER = "user";
  private TextView greeting;
  private TextView mResponse;
  private Calendar startTime;

  public AvailabilityScheduleFragment() {
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_availability_schedule, null);
    greeting = (TextView) view.findViewById(R.id.greetingText);
    mResponse = (TextView) view.findViewById(R.id.responseText);
    Button button = (Button) view.findViewById(R.id.buttonBook);
    button.setOnClickListener(this);

    final User user = getArguments().getParcelable(USER);
    if (user == null) {
      return view;
    }
    String name = user.getFirstName();
    greeting.append(" " + name);

    return view;
  }

  @Override public void onClick(View v) {
    Calendar now = Calendar.getInstance();
    MyDatePickerDialog dpd =
        MyDatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH));
    dpd.setVersion(DatePickerDialog.Version.VERSION_2);
    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
  }

  @Override public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
    GlassBook.bookRoom(startTime, this);
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    startTime = Calendar.getInstance();
    startTime.set(year, monthOfYear, dayOfMonth);
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd =
        TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), 0, true);
    if (dayOfMonth == now.get(Calendar.DATE)) {
      tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY), 0, 0);
    }
    tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
  }

  @Override public void onResult(final String response) {
    FragmentUtils.runOnUi(this, new Runnable() {
      @Override public void run() {
        mResponse.setText(response);
      }
    });
  }
}