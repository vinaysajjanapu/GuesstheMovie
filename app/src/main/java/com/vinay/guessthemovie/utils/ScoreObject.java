package com.vinay.guessthemovie.utils;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by salimatte on 25-01-2018.
 */

public class ScoreObject extends RealmObject {

    @PrimaryKey
    private String key;
    private int score;

    public ScoreObject() {
    }

    public ScoreObject(String key, int score) {
        this.key = key;
        this.score = score;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
