package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.ActivityUtils;
import io.sixth.glassbook.utils.GlassBookApp;
import io.sixth.glassbook.utils.RealmManager;


/**
 * Created by thawne on 25/12/16.
 */

public class MainActivity extends AppCompatActivity implements LoginManager {

  public Fragment swipeFragment;
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GlassBook.setApplicationInstance((GlassBookApp) getApplication());
    User user = RealmManager.getUser();
    Fragment fragment;

    if (user == null) {
      fragment = new LoginFragment();
    } else {
      fragment = swipeFragment = new SwipeFragment();
    }

    ActivityUtils.loadFragment(getSupportFragmentManager(), fragment, R.id.container_main);
  }

  @Override public void onLogin(@NonNull final User user) {
    GlassBookApp app = (GlassBookApp) getApplication();
    RealmManager.setUser(user);
    Fragment fragment = swipeFragment = new SwipeFragment();
    ActivityUtils.loadFragment(getSupportFragmentManager(), fragment, R.id.container_main);
  }

  @Override public void onFail() {
    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show();
  }
}
