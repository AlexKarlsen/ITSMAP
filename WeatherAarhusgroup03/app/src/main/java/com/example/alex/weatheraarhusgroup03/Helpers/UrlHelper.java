package com.example.alex.weatheraarhusgroup03.Helpers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Alex on 07/05/2017.
 */

//This function is borrowed from Leafcastle's WeatherServiceDemo
//It takes a URL and sets up a HTTP connection and returns Api content as a string on success
public class UrlHelper {

    public static final String CONNECT = "CONNECTIVITY";

    public static String callURL(String callUrl) {

        InputStream is = null;

        try {
            //create URL
            URL url = new URL(callUrl);

            //configure HttpURLConnetion object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //we could use HttpsURLConnection, weather API does not support SSL on free version
            //HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);



            // Starts the request
            conn.connect();
            int response = conn.getResponseCode();

            //probably check check on response code here!

            //give user feedback in case of error

            Log.d(CONNECT, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string

            String contentAsString = convertStreamToStringBuffered(is);
            return contentAsString;


        } catch (ProtocolException pe) {
            Log.d(CONNECT, "oh noes....ProtocolException");
        } catch (UnsupportedEncodingException uee) {
            Log.d(CONNECT, "oh noes....UnsuportedEncodingException");
        } catch (IOException ioe) {
            Log.d(CONNECT, "oh noes....IOException");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                    Log.d(CONNECT, "oh noes....could not close stream, IOException");
                }
            }
        }
        return null;
    }

    //This function is borrowed from Leafcastle's WeatherServiceDemo
    private String convertStreamToStringBuffered(InputStream is) {
        String s = "";
        String line = "";

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));


        try {
            while ((line = rd.readLine()) != null) { s += line; }
        } catch (IOException ex) {
            Log.e(CONNECT, "ERROR reading HTTP response", ex);
            //ex.printStackTrace();
        }

        //Return full string
        return s;
    }
}
