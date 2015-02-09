package com.codepath.apps.JeremyTweetApp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.JeremyTweetApp.R;
import com.codepath.apps.JeremyTweetApp.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private SwipeRefreshLayout swipeContainer;
    private static final int NEW_TWEET = 20;
    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimelie();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
            }
        });
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    private void populateTimeline() {
          if (!isOnline()) aTweets.addAll(Tweet.fromSQLite());
          else {
              client.getHomeTimeline(new JsonHttpResponseHandler() {
                  // success
                  @Override
                  public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                      aTweets.addAll(Tweet.fromJSONArray(response));
                  }

                  // failure
                  public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                  }
              }, false);
          }
    }

    private void refreshTimelie() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.clear();
                aTweets.addAll(Tweet.fromJSONArray(response));
                swipeContainer.setRefreshing(false);
            }

            // failure
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        }, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.compose) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCompose(MenuItem mi) {
        Intent toCompose = new Intent(this, ComposeTweetActivity.class);
        startActivityForResult(toCompose, NEW_TWEET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == NEW_TWEET) {
            String tweetContent = data.getStringExtra("content");
            postTweet(tweetContent);
            refreshTimelie();
        }
    }

    private void postTweet(String tweetContent) {
        // call api to post a new tweet.
        client.postNewTweet(tweetContent, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("debug", responseBody.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("debug", responseBody.toString());
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
