package io.sixth.glassbook.utils;

import android.app.Application;
import android.support.annotation.NonNull;
import com.facebook.stetho.Stetho;
import io.realm.Realm;
import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.data.local.AvailabilityCache;
import io.sixth.glassbook.data.local.User;
import okhttp3.OkHttpClient;

/**
 * Created by thawne on 26/12/16.
 */

public class GlassBookApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Realm.init(this);
    GlassBook.setApplicationInstance(this);
  }

  public User getUser() {
    Realm realm = Realm.getDefaultInstance();
    try {
      return realm.where(User.class).findAll().first();
    } catch (IndexOutOfBoundsException exception) {
      return null;
    }
  }

  public AvailabilityCache getAvavailibilityCache() {
    Realm realm = Realm.getDefaultInstance();
    try {
      return realm.where(AvailabilityCache.class).findAll().first();
    } catch (IndexOutOfBoundsException exception) {
      return null;
    }
  }

  public void setUser(@NonNull final User user) {
    Realm
        .getDefaultInstance()
        .executeTransactionAsync(new Realm.Transaction() {
          @Override public void execute(final Realm realm) {
            realm.copyToRealm(user);
          }
        });
  }

<<<<<<< HEAD:app/src/main/java/io/sixth/glassbook/utils/GlassBookApp.java
  public void setAvailibilityCache(@NonNull final AvailabilityCache cache) {
    Realm
            .getDefaultInstance()
            .executeTransactionAsync(new Realm.Transaction() {
              @Override public void execute(final Realm realm) {
                realm.copyToRealm(cache);
              }
            });
=======
  public OkHttpClient getClient() {
    return new OkHttpClient();
>>>>>>> 86959adaeb7c0d200e6a7c1c005cc389febd5f41:app/src/release/java/io/sixth/glassbook/utils/GlassBookApp.java
  }
}