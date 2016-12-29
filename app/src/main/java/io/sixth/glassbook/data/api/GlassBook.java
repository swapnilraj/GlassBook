package io.sixth.glassbook.data.api;

import android.support.design.widget.Snackbar;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import io.sixth.glassbook.R;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.GlassBookApp;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by thawne on 27/12/16.
 */

public class GlassBook {

  final private static String BASE_URL = "https://www.scss.tcd.ie/cgi-bin/webcal/sgmr";
  final private static String availibilityURL = "https://glassrooms.zach.ie/get.php";

  private static GlassBookApp app;

  public interface AuthListener {
    void onLoginSuccess(User user);
    void onLoginFail(Response response);
  }

  public interface RequestListener {
    void onResult(String string);
  }

  public static void setApplicationInstance(GlassBookApp applicationInstance) {
    app = applicationInstance;
  }

  public static void authenticate(final String username, final String password,
      final AuthListener listener) {

    final String selector = "body > h1";
    String authURL = BASE_URL + "/sgmr1.cancel.pl";
    OkHttpClient client = app.getClient();

    final Request request = new Request.Builder().url(authURL)
        .header("Authorization", Credentials.basic(username, password))
        .build();

    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          final Document doc = Jsoup.parse(response.body().string());
          final Elements ele = doc.select(selector);
          final String metaContainer = ele.text();
          final String[] content = metaContainer.split(" ");

          final User user = new User();
          user.setFirstName(content[10]);
          user.setLastName(content[11]);
          user.setStatus(content[12].substring(1, content[12].length() - 1));
          user.setUsername(username);
          user.setPassword(password);

          listener.onLoginSuccess(user);
        } else {
          listener.onLoginFail(response);
        }
      }
    });
  }

  public static void bookRoom(Calendar startTime, final int roomNumber,
      final RequestListener listener) {
    String requestURL = BASE_URL + String.format(Locale.ENGLISH, "/sgmr%d.request.pl", roomNumber);
    OkHttpClient client = app.getClient();

    User user = app.getUser();

    RequestBody formBody = new FormBody.Builder().add("StartTime",
        Integer.toString(startTime.get(Calendar.HOUR_OF_DAY) + 1))
        .add("EndTime", Integer.toString(startTime.get(Calendar.HOUR_OF_DAY) + 2))
        .add("Fullname", user.getFirstName())
        .add("Status", user.getStatus())
        .add("StartDate", Integer.toString(startTime.get(Calendar.DATE)))
        .add("StartMonth", Integer.toString(startTime.get(Calendar.MONTH) + 1))
        .add("StartYear", Integer.toString(startTime.get(Calendar.YEAR) - 2015))
        .build();

    Request request = new Request.Builder().url(requestURL)
        .header("Authorization", Credentials.basic(user.getUsername(), user.getPassword()))
        .post(formBody)
        .build();

    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          listener.onResult(response.body().string());
        }
      }
    });
  }

  public static void checkAvailability(int room, int date, final RequestListener listener) {
    OkHttpClient client = app.getClient();
    RequestBody formBody = new FormBody.Builder().add("n", Integer.toString(room))
            .add("o", Integer.toString(0))
            .build();

    Request request = new Request.Builder().url(availibilityURL)
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

