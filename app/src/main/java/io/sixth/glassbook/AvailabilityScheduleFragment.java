package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.sixth.glassbook.data.local.User;

/**
 * Created by thawne on 26/12/16.
 */

public class AvailabilityScheduleFragment extends Fragment {

  public static String USER = "user";

  public AvailabilityScheduleFragment() {
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_availability_schedule, null);
    TextView greeting = (TextView) view.findViewById(R.id.greetingText);

    User user = getArguments().getParcelable(USER);
    greeting.setText(user.getName());
    return view;
  }
}
