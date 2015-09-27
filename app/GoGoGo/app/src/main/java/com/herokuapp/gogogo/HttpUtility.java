package com.herokuapp.gogogo;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Siddharth on 9/26/2015.
 */
public class HttpUtility {

    public static BufferedReader openPostConnection(String urlString, Map<String, String> map){
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(urlString);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for(String key:map.keySet()){
            nameValuePairs.add(new BasicNameValuePair(key,map.get(key)));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            HttpResponse httpResponse = client.execute(post);
            return new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedReader openGetConnection(String urlString, Map<String, String> map){
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for(String key:map.keySet()){
            nameValuePairs.add(new BasicNameValuePair(key,map.get(key)));
        }
        String params = URLEncodedUtils.format(nameValuePairs,"utf-8");
        urlString +="?";
        urlString += params;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(urlString);
        try {
            HttpResponse httpResponse = client.execute(get);
            return new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
