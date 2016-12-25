package io.sixth.glassbook.data.local;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by thawne on 26/12/16.
 */

public class User implements Parcelable {

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

  public String getName() {
    return name;
  }

  @NonNull
  public String encode() {
    return String.format("%s: :%s", this.name, this.password);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.password);
  }

  protected User(Parcel in) {
    this.name = in.readString();
    this.password = in.readString();
  }

  public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    @Override public User createFromParcel(Parcel source) {
      return new User(source);
    }

    @Override public User[] newArray(int size) {
      return new User[size];
    }
  };
}
