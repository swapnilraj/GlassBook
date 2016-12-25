package io.sixth.glassbook.data.local;

import android.support.annotation.NonNull;

/**
 * Created by thawne on 26/12/16.
 */

public class User {

  private final String name;
  private final String password;

  public User(@NonNull final String encoded) {
    final String[] decoded = encoded.split(": :");
    this.name = decoded[0];
    this.password = decoded[1];
  }

  public User(@NonNull final String name, @NonNull final String password) {
    this.name = name;
    this.password = password;
  }

  @NonNull
  private String encode() {
    return String.format("%s: :%s", this.name, this.password);
  }

}
