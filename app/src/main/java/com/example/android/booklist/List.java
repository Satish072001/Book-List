package com.example.android.booklist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class List extends AppCompatActivity implements LoaderManager.LoaderCallbacks<java.util.List<Input>> {

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    /**
     * Constant value for the list loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int List_LOADER_ID = 1;
    public static final String LOG_TAG = ListActivity.class.getName();
    /** Adapter for the list of earthquakes */
    private ListAdapter mAdapter;
    /** URL for earthquake data from the USGS dataset */
    private  String USGS_REQUEST_URL ="https://www.googleapis.com/books/v1/volumes?q=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getSupportActionBar().setTitle("Get Your Book");

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String s = intent.getStringExtra("key");
        USGS_REQUEST_URL+=s+"&maxResults=10";

        // Find a reference to the {@link ListView} in the layout
        ListView listView=(ListView)findViewById(R.id.list);
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter=new ListAdapter(this,new ArrayList<Input>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);

        mEmptyStateTextView=(TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(List_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);

            // Update empty state with no connection error message
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current earthquake that was clicked on
                Input currentinput = mAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentinput.getMurl());
                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);


            }
        });



    }

    @Override
    public Loader<java.util.List<Input>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new Listloader(this, USGS_REQUEST_URL);
    }
    @Override
    public void onLoadFinished(Loader<java.util.List<Input>> loader, java.util.List<Input> lists) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (lists != null && !lists.isEmpty()) {
            mAdapter.addAll(lists);
        }
    }
    @Override
    public void onLoaderReset(Loader<java.util.List<Input>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}