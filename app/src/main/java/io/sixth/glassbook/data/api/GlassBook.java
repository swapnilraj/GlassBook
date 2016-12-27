package io.sixth.glassbook.data.api;

import io.sixth.glassbook.data.local.User;
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

  final static String BASE_URL = "https://www.scss.tcd.ie/cgi-bin/webcal/sgmr";

  public interface AuthListener {
    void onResult(User user);
  }

  public static void authenticate(final String username, final String password,
      final AuthListener listener) {

    final String selector = "body > h1:nth-child(3)";
    String url = BASE_URL + "/sgmr1.request.pl";
    OkHttpClient client = new OkHttpClient();

    final Request request = new Request.Builder().url(url)
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
          user.setFirstName(content[3]);
          user.setLastName(content[4]);
          user.setStatus(content[5].substring(1, content[5].length() - 1));
          user.setUsername(username);
          user.setPassword(password);

          listener.onResult(user);
        }
      }
    });
  }
}
