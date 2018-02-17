package com.vinay.guessthemovie.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.os.Build;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from ScoreObject.class
    public void clearAll() {
        realm.beginTransaction();
        realm.delete(ScoreObject.class);
        realm.commitTransaction();
    }

    public void addScore(ScoreObject scoreObject) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(scoreObject);
        realm.commitTransaction();
    }

    public int getLanScore(String lan) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return realm.where(ScoreObject.class).beginsWith("key", lan, Case.INSENSITIVE)
                    .findAll()
                    .stream().mapToInt(ScoreObject::getScore).sum();
        } else {
            int ans = 0;
            RealmResults<ScoreObject> realmResults = realm.where(ScoreObject.class).beginsWith("key", lan, Case.INSENSITIVE)
                    .findAll();
            for (ScoreObject s : realmResults) {
                ans += s.getScore();
            }
            return ans;
        }
    }

    public int getScore(String key) {
        ScoreObject scoreObject = realm.where(ScoreObject.class)
                .equalTo("key", key)
                .findFirst();
        return scoreObject == null ? 0 : scoreObject.getScore();
    }
}