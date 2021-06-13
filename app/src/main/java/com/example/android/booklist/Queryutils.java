package com.example.android.booklist;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class Queryutils {

    public static final String LOG_TAG = List.class.getName();
    /**
     * Create a private constructor because no one should ever create a {@link Queryutils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private Queryutils(){
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the list JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return a list of {@link Input} objects that has been built up from
     * parsing a JSON response.
     */
    /**
     * Return a list of {@link Input} objects that has been built up from
     * parsing the given JSON response.
     */
    private static java.util.List<Input> extractFeatureFromJson(String listJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(listJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding input to
        java.util.List<Input> lists = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(listJSON);


            JSONArray listArray = baseJsonResponse.getJSONArray("items");


            for (int i = 0; i < listArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentinput = listArray.getJSONObject(i);

                JSONObject volumeinfo = currentinput.getJSONObject("volumeInfo");

                String title = volumeinfo.getString("title");

                String mauthor= "";
                JSONArray author;
                if(volumeinfo.has("authors"))
                {
                    author =volumeinfo.getJSONArray("authors");

                    for(int j=0;j< author.length();j++)
                    {

                        mauthor+=author.getString(j);
                        if(j!= author.length()-1)
                            mauthor+=',';

                    }
                }



                String mimage="https://static.wikia.nocookie.net/source-filmmaker/images/a/a7/No_Image.jpg/revision/latest?cb=20201126143924";
                if(volumeinfo.has("imageLinks")) {
                    JSONObject imagelink=volumeinfo.getJSONObject("imageLinks");
                    mimage = imagelink.getString("thumbnail");
                }
                int index=3;
                String ModifiedImgURL=  mimage.substring(0, index + 1) + "s" + mimage.substring(index + 1);

                String murl=volumeinfo.getString("previewLink");
                Input input = new Input(title, mauthor, ModifiedImgURL, murl);


                lists.add(input);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return lists;
    }
    public static java.util.List<Input> fetchlistdata(String requestUrl)
    {
        // Create URL object
        URL url= createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse=null;
        try {
            jsonResponse=makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list
        java.util.List<Input> list=extractFeatureFromJson(jsonResponse);
        // Return the list
        return list;

    }

}
