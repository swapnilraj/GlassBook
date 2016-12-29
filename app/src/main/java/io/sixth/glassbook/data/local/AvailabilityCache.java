package io.sixth.glassbook.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmObject;
import io.sixth.glassbook.data.api.GlassBook;

import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Jorik on 28/12/16.
 */

public class AvailabilityCache extends RealmObject implements Parcelable {

    private String availabilityList = "";
    private long lastUpdate;

    public AvailabilityCache() {
    }
    
    public void update() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Calendar rightNow = Calendar.getInstance();
        this.availabilityList = "";
        //if (rightNow.getTimeInMillis() - lastUpdate > 300000) {
        for (int currentRoom = 1; currentRoom <= 9; currentRoom++) {
            String response = GlassBook.checkAvailability(currentRoom, 0);
            if (response != null)
                this.availabilityList += response;
        }
        lastUpdate = rightNow.getTimeInMillis();
        //}
        realm.commitTransaction();
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

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override public String toString() {
        return "AvailabilityCache{" +
                "availabilityList='" + availabilityList + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }
}
