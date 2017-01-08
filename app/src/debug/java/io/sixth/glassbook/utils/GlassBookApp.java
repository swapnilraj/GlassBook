package io.sixth.glassbook.utils;

import android.app.Application;
import android.support.annotation.NonNull;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import io.realm.Realm;
import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.data.local.AvailabilityCache;
import okhttp3.OkHttpClient;

/**
 * Created by thawne on 26/12/16.
 */

public class GlassBookApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Realm.init(this);
    Stetho.initializeWithDefaults(this);
    GlassBook.setApplicationInstance(this);
  }
}
