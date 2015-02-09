package com.codepath.apps.JeremyTweetApp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.codepath.apps.JeremyTweetApp.R;

public class ComposeTweetActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
        return true;
    }

    public void onFinish(MenuItem mi) {
        Intent i = getIntent();
        EditText etContent = (EditText) findViewById(R.id.etContent);
        String content = etContent.getText().toString();
        i.putExtra("content", content);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tweet) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
