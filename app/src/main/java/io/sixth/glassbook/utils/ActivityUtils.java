package io.sixth.glassbook.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by thawne on 26/12/16.
 */

public class ActivityUtils {

  public static void loadFragment(FragmentManager fragmentManager, Fragment fragment,
      int container) {

    fragmentManager
        .beginTransaction()
        .replace(container, fragment)
        .commit();
  }

  public static void addToBackStack(FragmentManager fragmentManager, Fragment detailFragment, int container) {
    fragmentManager.beginTransaction()
            .replace(container, detailFragment)
            // Add this transaction to the back stack
            .addToBackStack(null)
            .commit();
  }
}
