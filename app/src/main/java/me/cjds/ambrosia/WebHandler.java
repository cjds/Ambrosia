package me.cjds.ambrosia;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl Saldanha on 4/12/2015.
 */
public class WebHandler extends AsyncTask<ArrayList<String>,Void,ArrayList<String>>{

    private OnTaskExecutionFinished _task_finished_event;

    public interface OnTaskExecutionFinished{
        public void OnTaskFinishedEvent(ArrayList<String> result);
    }

    public void setOnTaskFinishedEvent(OnTaskExecutionFinished onTaskExecutionFinished)
    {
        if(onTaskExecutionFinished != null)this._task_finished_event = onTaskExecutionFinished;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {

//        Log.d("Number of array lists", Integer.toString(params[1].size()));

        String url=params[1].get(0);
        ArrayList<String> keys=params[2];
        ArrayList<String> values=params[3];

        HttpResponse response;
        ArrayList<String> result= new ArrayList<String>();

        HttpClient httpclient;
        httpclient = new DefaultHttpClient();

        if(params[0].get(0)=="get"){

            //http://rosiechef.herokuapp.com

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            for(int i=2;i< params[0].size()-1;i+=2){
                nameValuePairs.add(new BasicNameValuePair(params[0].get(i),params[0].get(i+1)));
            }
            String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            url += "?"+paramString;
            HttpGet httpget = new HttpGet(url);

            try {
                response = httpclient.execute(httpget);
                result.add(this.getStringFromHTTPResponse(response));
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

        else if(params[0].get(0)=="post"){

            HttpPost httppost;
            ArrayList<NameValuePair> postParameters;
            httppost = new HttpPost(url);
            postParameters = new ArrayList<NameValuePair>();

            for(int i=0;i<keys.size();i++)
                postParameters.add(new BasicNameValuePair(keys.get(i),values.get(i)));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                response = httpclient.execute(httppost);
                result.add(this.getStringFromHTTPResponse(response));
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

        Log.d("Returning Result ArrayList", Integer.toString(result.size()));
        return result;
    }

     public void endFunction(JSONObject json){

     }

    public String getStringFromHTTPResponse(HttpResponse response){
        Log.d("String from response"," ");
        try {
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            Log.d("Result String",sb.toString());
            return sb.toString();
        }
        catch (IOException e) {
            Log.d("Result String", "io error");
            return "io error";
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String> result)
    {
        Log.d("Number of array lists", Integer.toString(result.size()));
        super.onPostExecute(result);
        if(this._task_finished_event != null){
            this._task_finished_event.OnTaskFinishedEvent(result);
        }
        else
        {
            Log.d("SomeClass", "task_finished even is null");
        }
    }
}
