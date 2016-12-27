package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.FragmentUtils;
import okhttp3.Response;

/**
 * Created by thawne on 26/12/16.
 */

public class AvailabilityScheduleFragment extends Fragment {

  public static String USER = "user";
  private TextView greeting;

  public AvailabilityScheduleFragment() {
  }

  public void updateTextView(final Response response) throws Exception {
    final String res = response.body().string();
    FragmentUtils.runOnUi(this, new Runnable() {
      @Override public void run() {
        greeting.setText(res);
      }
    });
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_availability_schedule, null);
    greeting = (TextView) view.findViewById(R.id.greetingText);
    User user = getArguments().getParcelable(USER);
    return view;
  }
}