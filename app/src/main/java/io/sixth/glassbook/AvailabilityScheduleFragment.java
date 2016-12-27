package io.sixth.glassbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.FragmentUtils;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by thawne on 26/12/16.
 */

public class AvailabilityScheduleFragment extends Fragment {

  public static String USER = "user";
  private TextView greeting;

  public AvailabilityScheduleFragment() {
  }

  public void toastMaker(final Response response) throws Exception {
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
    //showAvailability();
    User user = getArguments().getParcelable(USER);
    return view;
  }

  //public void showAvailability() {
  //  String url = "https://www.scss.tcd.ie/cgi-bin/webcal/sgmr/sgmr1.cancel.pl";
  //  OkHttpClient client = new OkHttpClient();
  //
  //  RequestBody formBody = new FormBody.Builder()
  //      .add("Cancel", "20161227|01:00-02:00|user|rajs|Swapnil Raj|ba1|")
  //
  //      //.add("EndTime", "")
  //      //.add("Fullname", "")
  //      //.add("Status", "")
  //      //.add("StartDate", "")
  //      //.add("StartMonth", "")
  //      //.add("StartYear", "")
  //      .build();
  //
  //  final User user = getArguments().getParcelable(USER);
  //  if (user == null) {
  //    return;
  //  }
  //
  //
  //  client.newCall(request).enqueue(new Callback() {
  //    @Override public void onFailure(Call call, IOException e) {
  //      e.printStackTrace();
  //    }
  //
  //    @Override public void onResponse(Call call, Response response) throws IOException {
  //      if (!response.isSuccessful()) {
  //        throw new IOException("Unexpected code " + response);
  //      }
  //      try {
  //        toastMaker(response);
  //      } catch (Exception e) {
  //        e.printStackTrace();
  //      }
  //    }
  //  });
  //}
}

