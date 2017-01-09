package io.sixth.glassbook.utils;

import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmResults;
import io.sixth.glassbook.data.local.AvailabilityCache;
import io.sixth.glassbook.data.local.User;

/**
 * Created by Jorik on 08/01/2017.
 */

public class RealmManager {

    public static User getUser() {
        Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(User.class).findAll().first();
        } catch (IndexOutOfBoundsException exception) {
            return null;
        }
    }

    public static void setUser(@NonNull final User user) {
        Realm
                .getDefaultInstance()
                .executeTransactionAsync(new Realm.Transaction() {
                    @Override public void execute(final Realm realm) {
                        realm.copyToRealm(user);
                    }
                });
    }

    public static AvailabilityCache getAvailabilityCache(int dayFromNow) {
        Realm realm = Realm.getDefaultInstance();
        try {
            AvailabilityCache cache = realm.where(AvailabilityCache.class).equalTo("index", dayFromNow).findAll().first();
            return cache;
        } catch (IndexOutOfBoundsException exception) {
            AvailabilityCache cache = new AvailabilityCache(dayFromNow);
            setAvailabilityCache(cache);
            return cache;
        }
    }

    public static void purgeAvailabilityCache() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<AvailabilityCache> results = realm.where(AvailabilityCache.class).findAll();
                    results.deleteAllFromRealm();
                }
            });
        } catch (IndexOutOfBoundsException ignored) {
        }
    }


    private static void setAvailabilityCache(@NonNull final AvailabilityCache cache) {
        Realm
                .getDefaultInstance()
                .executeTransactionAsync(new Realm.Transaction() {
                    @Override public void execute(final Realm realm) {
                        realm.copyToRealm(cache);
                    }
                });
    }

    public static void updateAvailabilityCache(int daysFromNow) {
        Realm realm = Realm.getDefaultInstance();
        AvailabilityCache cache = getAvailabilityCache(daysFromNow);
        realm.beginTransaction();
        cache.update();
        realm.commitTransaction();
    }
}
