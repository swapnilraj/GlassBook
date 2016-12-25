package io.sixth.glassbook;

import io.sixth.glassbook.data.local.User;

/**
 * Created by thawne on 26/12/16.
 */

public interface LoginManager {

  void onLogin(User user);
  void onFail();
}
