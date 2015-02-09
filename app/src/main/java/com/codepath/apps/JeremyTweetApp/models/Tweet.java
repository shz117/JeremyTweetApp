package com.codepath.apps.JeremyTweetApp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jeremyshi on 2/7/15.
 */

/*
*
* */
@Table(name = "Tweets")
public class Tweet extends Model {
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid; //unique id for tweet
    @Column(name = "body")
    private  String body;
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "createdAt")
    private  String createdAt;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tweet.save();
        return tweet;
    }

    public Tweet() {
        super();
    }

    public User getUser() {
        return user;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray array) {
        ArrayList<Tweet> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                list.add(Tweet.fromJson(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return list;

    }

    public static ArrayList<Tweet> fromSQLite() {
        List<Model> result = null;
        try {
            result = new Select()
                    .from(Tweet.class)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Tweet> ret = new ArrayList<Tweet>();
        for (Model t: result) {
            ret.add((Tweet)t);
        }
        return ret;
    }
}
