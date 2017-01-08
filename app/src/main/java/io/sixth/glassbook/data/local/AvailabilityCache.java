package io.sixth.glassbook.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    this.availabilityList = "";
    for (int currentRoom = 1; currentRoom <= 9; currentRoom++) {
      String response = GlassBook.checkAvailability(currentRoom, 0);
      this.availabilityList += response + ",\n";
    }
    this.lastUpdate = rightNow.getTimeInMillis();
  }

  public boolean isUpToDate(){
    return isUpToDate(20);
  }

  public boolean isUpToDate(int expiryInMinutes){
    Calendar rightNow = Calendar.getInstance();
    return (rightNow.getTimeInMillis() - this.lastUpdate < expiryInMinutes * 60 * 1000);
  }

  public boolean timeIsAvailable(int hoursFromNow) {
    Calendar rightNow = Calendar.getInstance();
    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
    if (!isUpToDate()) GlassBook.updateAvailabilityCache();
    for (int room = 0; room < 9; room++) {
      String[] roomData = getRoomsData(room);
      if (roomData[currentHour + hoursFromNow] == null) return true;
    }
    return false;
  }

  public boolean roomIsFree(int room, int hoursFromNow) {
    Calendar rightNow = Calendar.getInstance();
    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
    if (!isUpToDate()) GlassBook.updateAvailabilityCache();
    String[] roomData = getRoomsData(room);
    return (roomData[currentHour + hoursFromNow] == null);
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

  public String[] getRoomsData(int room)
  {
    Scanner list = new Scanner(this.availabilityList);
    for (int skip = 0; skip < room - 1; skip ++) {
      if (list.hasNextLine())
        list.nextLine();
      else
        return null;
    }
    Gson gson = new GsonBuilder().serializeNulls().create();
    String roomData = list.nextLine();
    return gson.fromJson(roomData.substring(0, roomData.length() - 1), String[].class);
  }
}
