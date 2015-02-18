package com.codepath.apps.JeremyTweetApp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.JeremyTweetApp.R;
import com.codepath.apps.JeremyTweetApp.fragments.HomeTimeineFragment;
import com.codepath.apps.JeremyTweetApp.fragments.MentionsTimelineFragment;
import com.codepath.apps.JeremyTweetApp.fragments.TweetsListFragment;
import com.codepath.apps.JeremyTweetApp.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    TwitterClient client;
    private static final int NEW_TWEET = 70;
    private TweetsPagerAdapter vpAdapter;
    private HomeTimeineFragment homeTimelineFragment;
    private MentionsTimelineFragment mentionsTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        /*
        * get viewpager
        * set viewpager adapter
        * find the sliding tabs
        * attach tabs to viewpager
        * */
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(vpAdapter);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
     }



//    private void refreshTimelie() {
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//            // success
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                aTweets.clear();
//                aTweets.addAll(Tweet.fromJSONArray(response));
//            }
//
//            // failure
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//
//            }
//        }, true);
//    }



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
//        if (id == R.id.compose) {
//            return true;
//        }

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
//            refreshTimelie();
        }
    }

    private void postTweet(String tweetContent) {
        // call api to post a new tweet.
        client.postNewTweet(tweetContent, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                homeTimelineFragment.populateTimeline(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("debug", responseBody.toString());
            }
        });
    }

    //return order of the fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (homeTimelineFragment == null) {
                    homeTimelineFragment = new HomeTimeineFragment();

                }
                return homeTimelineFragment;
            } else if (position == 1){
                if (mentionsTimelineFragment == null) {
                    mentionsTimelineFragment = new MentionsTimelineFragment();

                }
                return mentionsTimelineFragment;
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }
}
