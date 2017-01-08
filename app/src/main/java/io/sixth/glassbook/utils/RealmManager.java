package io.sixth.glassbook.utils;

import android.support.annotation.NonNull;

import io.realm.Realm;
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

    public static AvailabilityCache getAvailabilityCache() {
        Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(AvailabilityCache.class).findAll().first();
        } catch (IndexOutOfBoundsException exception) {
            AvailabilityCache cache = new AvailabilityCache();
            setAvailabilityCache(cache);
            return cache;
        }
    }

    public static void setAvailabilityCache(@NonNull final AvailabilityCache cache) {
        Realm
                .getDefaultInstance()
                .executeTransactionAsync(new Realm.Transaction() {
                    @Override public void execute(final Realm realm) {
                        realm.copyToRealm(cache);
                    }
                });
    }

    public static void updateAvailabilityCache() {
        Realm realm = Realm.getDefaultInstance();
        AvailabilityCache cache = getAvailabilityCache();
        realm.beginTransaction();
        cache.update();
        realm.commitTransaction();
    }

}
