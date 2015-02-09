package com.codepath.apps.JeremyTweetApp;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.JeremyTweetApp.models.Tweet;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jeremyshi on 2/7/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvRelTime = (TextView) convertView.findViewById(R.id.tvRelTime);
        tvUsername.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        long createTimeStamp  = 0;
        try {
            Date createDate = getTwitterDate(tweet.getCreatedAt());
            createTimeStamp = createDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvRelTime.setText(reformat(DateUtils.getRelativeTimeSpanString(createTimeStamp, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)));
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        return convertView;
    }

    private static String reformat(CharSequence relativeTimeSpanString) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            if (relativeTimeSpanString.charAt(i) == ' ') {
                sb.append(relativeTimeSpanString.subSequence(0, i));
                break;
            }
            i++;
        }
        sb.append(relativeTimeSpanString.charAt(i+1));
        return sb.toString();
    }

    private static Date getTwitterDate(String date) throws ParseException {

        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
        sf.setLenient(true);
        return sf.parse(date);
    }
}
