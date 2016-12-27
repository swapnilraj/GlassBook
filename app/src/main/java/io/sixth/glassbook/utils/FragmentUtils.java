package io.sixth.glassbook.utils;

import android.support.v4.app.Fragment;

/**
 * Created by thawne on 28/12/16.
 */

public class FragmentUtils {
  public static void runOnUi(Fragment fragment, Runnable runnable) {
    fragment.getActivity().runOnUiThread(runnable);
  }
}
