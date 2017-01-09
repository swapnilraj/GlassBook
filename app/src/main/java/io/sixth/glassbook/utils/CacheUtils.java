package io.sixth.glassbook.utils;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import io.sixth.glassbook.data.api.httpClientAPI;

/**
 * Created by Jorik on 08/01/2017.
 */

public class CacheUtils {
    final private static String AVAILABILITY_URL = "https://glassrooms.zach.ie/get.php\\?n\\=%d\\&o\\%d";

    public static String checkAvailabilityFromServer(int room, int date) {
        OkHttpClient client = httpClientAPI.getClient();
        String url = String.format(Locale.ENGLISH, AVAILABILITY_URL, room, date);
        final String[] json = new String[1];
        Request request = new Request.Builder().url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    json[0] = response.body().string();
                }
            }
        });
        while (json[0] == null) ; // will cause hang
        return json[0];
    }

    public static String dummyServer() {
        return "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],\n" +
                "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],\n" +
                "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],\n" +
                "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],\n" +
                "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],\n" +
                "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],\n" +
                "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],\n" +
                "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],\n" +
                "[\"a\", null, \"b\", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null],";
    }
}
