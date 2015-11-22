package com.concavenp.nanodegree.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * References:
 *      Creating a Fragment - http://developer.android.com/training/basics/fragments/creating.html
 *      Learning Android: Develop Mobile Apps Using Java and Eclipse - Chapter 8 Fragments
 */
public class MainActivity extends AppCompatActivity implements MovieListingFragment.OnFragmentInteractionListener {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MainActivity.class.getSimpleName();

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       // Create an instance of movie listing fragment
       MovieListingFragment fragment = MovieListingFragment.newInstance("","");

       // Add the fragment to the 'fragment_container' FrameLayout
       getSupportFragmentManager().beginTransaction().add(R.id.main_content, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

}
