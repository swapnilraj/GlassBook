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
    AvailabilityCache.setApplicationInstance(this);
  }

  public User getUser() {
    Realm realm = Realm.getDefaultInstance();
    try {
      return realm.where(User.class).findAll().first();
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

  public AvailabilityCache getAvailabilityCache() {
    Realm realm = Realm.getDefaultInstance();
    try {
      return realm.where(AvailabilityCache.class).findAll().first();
    } catch (IndexOutOfBoundsException exception) {
      AvailabilityCache cache = new AvailabilityCache();
      setAvailabilityCache(cache);
      return cache;
    }
  }

  public void setAvailabilityCache(@NonNull final AvailabilityCache cache) {
    Realm
        .getDefaultInstance()
        .executeTransactionAsync(new Realm.Transaction() {
          @Override public void execute(final Realm realm) {
            realm.copyToRealm(cache);
          }
        });
  }

  public void updateAvailibilityCache() {
    Realm realm = Realm.getDefaultInstance();
    AvailabilityCache cache = getAvailabilityCache();
    realm.beginTransaction();
    cache.update();
    System.out.print(cache.toString());
    realm.commitTransaction();
    System.out.print(cache.toString());
  }

  public OkHttpClient getClient() {
    return new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
  }
}
