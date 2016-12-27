package io.sixth.glassbook.data.local;

import android.os.Parcel;
import android.os.Parcelable;
import io.realm.RealmObject;

/**
 * Created by thawne on 26/12/16.
 */

public class User extends RealmObject implements Parcelable {

  public User() {}

  private String firstName;
  private String lastName;
  private String status;
  private String username;
  private String password;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.firstName);
    dest.writeString(this.lastName);
    dest.writeString(this.status);
    dest.writeString(this.username);
    dest.writeString(this.password);
  }

  protected User(Parcel in) {
    this.firstName = in.readString();
    this.lastName = in.readString();
    this.status = in.readString();
    this.username = in.readString();
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

  @Override public String toString() {
    return "User{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", status='" + status + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
