package io.sixth.glassbook.data.local;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;

import io.sixth.glassbook.utils.CacheUtils;

import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Jorik on 28/12/16.
 */

public class AvailabilityCache extends RealmObject{

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
//    for (int currentRoom = 1; currentRoom <= 9; currentRoom++) {
//      String response = CacheUtils.checkAvailabilityFromServer(currentRoom, index);
//      this.availabilityList += response + ",\n";
//    }
    this.availabilityList += CacheUtils.dummyServer();
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
  public String toString() {
    return "AvailabilityCache{" +
            "availabilityList='" + availabilityList + '\'' +
            ", lastUpdate='" + lastUpdate + '\'' +
            ", index='" + index + '\'' +
            '}';
  }
}