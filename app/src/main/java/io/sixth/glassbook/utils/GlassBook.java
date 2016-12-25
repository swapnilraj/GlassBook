package io.sixth.glassbook.utils;

import android.app.Application;
import android.support.annotation.NonNull;
import com.facebook.stetho.Stetho;
import io.sixth.glassbook.data.local.User;

/**
 * Created by thawne on 26/12/16.
 */

public class GlassBook extends Application {

  private Prefs prefs;

  @Override public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    prefs = new Prefs(this);
  }

  public User getUser() {
    return prefs.getUser();
  }

  public void setUser(@NonNull final User user) {
    prefs.setUser(user);
  }
}
