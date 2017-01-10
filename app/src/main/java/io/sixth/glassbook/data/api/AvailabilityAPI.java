package io.sixth.glassbook.data.api;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import io.sixth.glassbook.data.local.AvailabilityCache;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.RealmManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jorik on 08/01/2017.
 */

public class AvailabilityAPI {

    final private static String BASE_URL = "https://www.scss.tcd.ie/cgi-bin/webcal/sgmr";

    public static boolean isTimeAvailable(int hoursFromNow, int daysFromNow) {
        AvailabilityCache cache = RealmManager.getUpdatedAvailabilityCache(daysFromNow);
        return cache.isTimeAvailable(hoursFromNow);
    }

    public static boolean[] getRoomAvailablilty(int room, int hoursFromNow, int daysFromNow) {
        AvailabilityCache cache = RealmManager.getUpdatedAvailabilityCache(daysFromNow);

        return new boolean[1];
    }

    public static boolean isRoomAvailable(int room, int hoursFromNow, int daysFromNow) {
        AvailabilityCache cache = RealmManager.getUpdatedAvailabilityCache(daysFromNow);

        return cache.roomIsAvailable(room, hoursFromNow);
    }

    public static void bookRoom(Calendar startTime, final int roomNumber,
                                final GlassBook.RequestListener listener) {
        String requestURL = BASE_URL + String.format(Locale.ENGLISH, "/sgmr%d.request.pl", roomNumber);
        OkHttpClient client = httpClientAPI.getClient();

        RealmManager realmManager = new RealmManager();
        User user = realmManager.getUser();

        Calendar rightNow = Calendar.getInstance();

        RequestBody formBody = new FormBody.Builder().add("StartTime",
                Integer.toString(startTime.get(Calendar.HOUR_OF_DAY) + 1))
                .add("EndTime", Integer.toString(startTime.get(Calendar.HOUR_OF_DAY) + 2))
                .add("Fullname", user.getFirstName())
                .add("Status", user.getStatus())
                .add("StartDate", Integer.toString(startTime.get(Calendar.DATE)))
                .add("StartMonth", Integer.toString(startTime.get(Calendar.MONTH) + 1))
                .add("StartYear", Integer.toString(startTime.get(Calendar.YEAR)
                        - rightNow.get(Calendar.YEAR) + 1))
                .build();

        Request request = new Request.Builder().url(requestURL)
                .header("Authorization", Credentials.basic(user.getUsername(), user.getPassword()))
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    listener.onResult(response.body().string());
                }
            }
        });
    }
}
