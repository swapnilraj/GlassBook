package io.sixth.glassbook.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;

import io.sixth.glassbook.utils.CacheUtils;
import io.sixth.glassbook.utils.RealmManager;

import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Jorik on 28/12/16.
 */

public class AvailabilityCache extends RealmObject implements Parcelable {

  private String availabilityList = " ";
  private long lastUpdate = 0;
  private int index;

  public AvailabilityCache() {}
  public AvailabilityCache(int i) {
    index = i;
  }

  public boolean isUpToDate() {
    return isUpToDate(20);
  }

  public boolean isUpToDate(int expiryInMinutes) {
    Calendar rightNow = Calendar.getInstance();
    return (rightNow.getTimeInMillis() - this.lastUpdate < expiryInMinutes * 60 * 1000);
  }

  public void update() {
    if (!isUpToDate()) hardUpdate();
  }

  public void hardUpdate() {
    Calendar rightNow = Calendar.getInstance();
    this.availabilityList = "";
    for (int currentRoom = 1; currentRoom <= 9; currentRoom++) {
      String response = CacheUtils.checkAvailabilityFromServer(currentRoom, index);
      this.availabilityList += response + ",\n";
    }
    this.lastUpdate = rightNow.getTimeInMillis();
  }

  public String[] getRoomsData(int room) {
    Scanner list = new Scanner(this.availabilityList);
    for (int skip = 0; skip < room - 1; skip++) {
      if (list.hasNextLine())
        list.nextLine();
      else
        return null;
    }
    Gson gson = new GsonBuilder().serializeNulls().create();
    String roomData = list.nextLine();
    return gson.fromJson(roomData.substring(0, roomData.length() - 1), String[].class);
  }

  public boolean isTimeAvailable(int hour) {
    for (int room = 0; room < 9; room++) {
      String[] roomData = getRoomsData(room);
      if (roomData[hour] == null) return true;
    }
    return false;
  }

  public boolean roomIsFree(int room, int hour) {
    String[] roomData = getRoomsData(room);
    return (roomData[hour] == null);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.availabilityList);
    dest.writeLong(this.lastUpdate);
    dest.writeInt(this.index);
  }

  protected AvailabilityCache(Parcel in) {
    this.availabilityList = in.readString();
    this.lastUpdate = in.readLong();
    this.index = in.readInt();
  }

  public static final Parcelable.Creator<AvailabilityCache> CREATOR = new Parcelable.Creator<AvailabilityCache>() {
    @Override
    public AvailabilityCache createFromParcel(Parcel source) {
      return new AvailabilityCache(source);
    }

    @Override
    public AvailabilityCache[] newArray(int size) {
      return new AvailabilityCache[size];
    }
  };

  @Override
  public String toString() {
    return "AvailabilityCache{" +
            "availabilityList='" + availabilityList + '\'' +
            ", lastUpdate='" + lastUpdate + '\'' +
            ", index='" + index + '\'' +
            '}';
  }
}