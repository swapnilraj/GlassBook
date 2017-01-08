package io.sixth.glassbook.data.api;

import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.GlassBookApp;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by thawne on 27/12/16.
 */

public class GlassBook {

  final private static String BASE_URL = "https://www.scss.tcd.ie/cgi-bin/webcal/sgmr";

  public static GlassBookApp app;

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
    OkHttpClient client = httpClientAPI.getClient();

    final Request request = new Request.Builder().url(authURL)
            .header("Authorization", Credentials.basic(username, password))
            .build();

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
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
}

