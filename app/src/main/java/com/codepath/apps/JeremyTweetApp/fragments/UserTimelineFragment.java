package com.codepath.apps.JeremyTweetApp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.JeremyTweetApp.EndlessScrollListener;
import com.codepath.apps.JeremyTweetApp.TwitterApplication;
import com.codepath.apps.JeremyTweetApp.TwitterClient;
import com.codepath.apps.JeremyTweetApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jeremyshi on 2/16/15.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, parent, savedInstanceState);
        addEndlessScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
            }
        });
        return v;
    }

    public static UserTimelineFragment newInstance(String screename) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screename);
        userFragment.setArguments(args);
        return userFragment;
    }

    private void populateTimeline() {

        String screenName = getArguments().getString("screen_name");
        if (!isOnline()) addAll(Tweet.fromSQLite());
        else {
            client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
                // success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    addAll(Tweet.fromJSONArray(response));
                }

                // failure
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                }
            });
        }
    }
}
