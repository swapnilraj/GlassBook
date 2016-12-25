package io.sixth.glassbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.Prefs;

/**
 * Created by thawne on 25/12/16.
 */

public class MainActivity extends AppCompatActivity {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    if (isLoggedIn()) {
      Toast.makeText(this, "Lodged in", Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(this, "Not", Toast.LENGTH_LONG).show();
    }
  }

  @NonNull
  private boolean isLoggedIn() {
    Prefs prefs = new Prefs(getApplicationContext());
    return (prefs.getUser() != null);
  }
}
