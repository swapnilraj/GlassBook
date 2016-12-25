package io.sixth.glassbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import io.sixth.glassbook.utils.GlassBook;

/**
 * Created by thawne on 25/12/16.
 */

public class MainActivity extends AppCompatActivity {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    GlassBook app = (GlassBook) getApplication();
    if (app.getUser() == null) {
      //Logged out
    } else {
      //Logged in
    }
  }
}
