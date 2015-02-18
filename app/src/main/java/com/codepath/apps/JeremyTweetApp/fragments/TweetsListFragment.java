package com.codepath.apps.JeremyTweetApp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.JeremyTweetApp.EndlessScrollListener;
import com.codepath.apps.JeremyTweetApp.ProfileActivity;
import com.codepath.apps.JeremyTweetApp.R;
import com.codepath.apps.JeremyTweetApp.TweetsArrayAdapter;
import com.codepath.apps.JeremyTweetApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremyshi on 2/16/15.
 */
public abstract class TweetsListFragment extends Fragment {

    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;




    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                populateTimeline();
//            }
//        });
        lvTweets.setAdapter(aTweets);
        setupListViewListener();
        return v;
    }

    private void setupListViewListener() {
        lvTweets.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        String screenName = aTweets.getItem(pos).getUser().getScreenName();
                        Intent i = new Intent(getActivity(), ProfileActivity.class);
                        i.putExtra("screen_name", screenName);
                        startActivity(i);
                    }

                });

    }

    public void addEndlessScrollListener(EndlessScrollListener listener) {
        lvTweets.setOnScrollListener(listener);
    }

    // creation lifecycle event

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }



    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void clearAll() {
        aTweets.clear();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}


