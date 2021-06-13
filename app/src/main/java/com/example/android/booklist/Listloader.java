package com.example.android.booklist;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class Listloader extends AsyncTaskLoader<List<Input>> {

    /** Query URL */
    private String mUrl;

    public Listloader(Context context,String url){
        super(context);
        mUrl=url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Input> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list
        List<Input> lists = (List<Input>) Queryutils.fetchlistdata(mUrl);
        return lists;
    }
}
