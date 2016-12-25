package io.sixth.glassbook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.sixth.glassbook.data.local.User;

/**
 * Created by thawne on 26/12/16.
 */

public class Prefs {

  private final static String KEY = "glassbook.pref";
  private final static String USER_KEY = KEY + "/user";
  private final SharedPreferences preferences;

  public Prefs(@NonNull final Context context) {
    this.preferences = context.getSharedPreferences(KEY, context.MODE_PRIVATE);
  }

  @Nullable
  public User getUser() {
    String encoded = preferences.getString(USER_KEY, null);

    if (encoded == null) {
      return null;
    }

    return new User(encoded);
  }
}
