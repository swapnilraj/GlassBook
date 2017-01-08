package io.sixth.glassbook.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.sixth.glassbook.data.api.GlassBook;

import io.sixth.glassbook.utils.GlassBookApp;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Jorik on 28/12/16.
 */

public class AvailabilityCache extends RealmObject implements Parcelable {

  private String availabilityList = " ";
  private long lastUpdate = 0;

  @Ignore private static GlassBookApp app;

  public AvailabilityCache() {
  }

  public static void setApplicationInstance(GlassBookApp applicationInstance) {
    app = applicationInstance;
  }

  public void update() {
    Calendar rightNow = Calendar.getInstance();

    this.availabilityList = "[";

    for (int currentRoom = 1; currentRoom <= 9; currentRoom++) {
      String response = GlassBook.checkAvailability(currentRoom, 0);

      this.availabilityList += response + ",\n";
    }
    this.availabilityList += "]";
    this.lastUpdate = rightNow.getTimeInMillis();
  }

  public boolean roomIsFree(int room, int time) {
    update();
    Scanner roomList = new Scanner(this.availabilityList);
    for(int skip = 1; skip < room; skip++) {
      roomList.nextLine();
    }

    Gson gson = new Gson();
    String roomData = roomList.nextLine();
    String[] hourArray = gson.fromJson(roomData, String[].class);
    return (hourArray[time] == null);
  }

  @Override public int describeContents() {
      return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.availabilityList);
    dest.writeLong(this.lastUpdate);
  }

  protected AvailabilityCache(Parcel in) {
    this.availabilityList = in.readString();
    this.lastUpdate = in.readLong();
  }

  public static final Parcelable.Creator<AvailabilityCache> CREATOR = new Parcelable.Creator<AvailabilityCache>() {
    @Override public AvailabilityCache createFromParcel(Parcel source) {
          return new AvailabilityCache(source);
      }
    @Override public AvailabilityCache[] newArray(int size) {
          return new AvailabilityCache[size];
      }
  };

  @Override public String toString() {
    return "AvailabilityCache{" +
        "availabilityList='" + availabilityList + '\'' +
        ", lastUpdate='" + lastUpdate + '\'' +
        '}';
  }
}
