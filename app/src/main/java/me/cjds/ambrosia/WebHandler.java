package me.cjds.ambrosia;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Carl Saldanha on 4/12/2015.
 */
public class WebHandler extends AsyncTask<ArrayList<String>,ArrayList<String>,ArrayList<String>> {

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        String url=params[1].get(0);
        ArrayList<String> keys=params[2];
        ArrayList<String> values=params[3];
        if(params[0].get(0)=="get"){
            1
            //http://rosiechef.herokuapp.com

        }
        else if(params[0].get(0)=="post"){
            HttpClient httpclient;
            HttpPost httppost;
            ArrayList<NameValuePair> postParameters;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(url);


            postParameters = new ArrayList<NameValuePair>();
            for(int i=0;i<keys.size();i++)
                postParameters.add(new BasicNameValuePair(keys.get(i),values.get(i)));

            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                HttpResponse response = httpclient.execute(httppost);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

     public void endFucncttion(JSONObject json){

     };

}
