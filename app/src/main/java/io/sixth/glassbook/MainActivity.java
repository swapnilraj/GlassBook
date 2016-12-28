package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.ActivityUtils;
import io.sixth.glassbook.utils.GlassBookApp;

/**
 * Created by thawne on 25/12/16.
 */

public class MainActivity extends AppCompatActivity implements LoginManager {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GlassBookApp app = (GlassBookApp) getApplication();
    User user = app.getUser();
    Fragment fragment;

    if (user == null) {
      fragment = new LoginFragment();
    } else {
      final Bundle bundle = new Bundle();
      bundle.putParcelable(AvailabilityScheduleFragment.USER, user);
      fragment = new AvailabilityScheduleFragment();
      fragment.setArguments(bundle);
    }

    ActivityUtils.loadFragment(getSupportFragmentManager(), fragment, R.id.container_main);
  }

  @Override public void onLogin(@NonNull final User user) {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        GlassBookApp app = (GlassBookApp) getApplication();
        app.setUser(user);
      }
    });

    Fragment fragment = new AvailabilityScheduleFragment();
    final Bundle bundle = new Bundle();
    bundle.putParcelable(AvailabilityScheduleFragment.USER, user);
    fragment.setArguments(bundle);

    ActivityUtils.loadFragment(getSupportFragmentManager(), fragment, R.id.container_main);
  }

  @Override public void onFail() {
    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show();
  }
}
