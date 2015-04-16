package me.cjds.ambrosia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class NotificationService extends Service {

    public static boolean isRecipeCompleteFlag = false;

    final String TAG="Magic";
    static int count=0;
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Rosie is preparing your meal", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Background service started", Toast.LENGTH_LONG).show();
        new DoBackgroundTask().execute("");
        Log.d(TAG, "onStart");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }


private class DoBackgroundTask extends AsyncTask<String, String, String> {

    public void setupNextNotification(String result){
        Notification.Builder mBuilder =
                new Notification.Builder(NotificationService.this)
                        .setSmallIcon(android.R.drawable.ic_menu_compass)
                        .setContentTitle("Rosie "+NotificationService.count++)
                        .setContentText(result);
        Intent resultIntent = new Intent(NotificationService.this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        NotificationService.this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                new DoBackgroundTask().execute("");
            }
        }, 10000);
    }
    public void getRecipeStatus(){
        WebHandler webHandler = new WebHandler();

        webHandler.setOnTaskFinishedEvent(new WebHandler.OnTaskExecutionFinished(){

            @Override
            public void OnTaskFinishedEvent(ArrayList<String> result){
                //Log.d("RecipeStatus Result",result.get(0));
                String json =result.get(0);
                try {
                    JSONArray resultArray = new JSONArray(json);
                    JSONObject dict = resultArray.getJSONObject(0);

//                    Integer new_device_step = dict.get("device_step");
                    if (dict.getString("device_step").equals("null")){
                        NotificationService.isRecipeCompleteFlag = true;
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add("http://rosiechef.herokuapp.com/device");
        ArrayList<String> requestList = new ArrayList<String>();
        requestList.add("get");
        ArrayList<String> paramList = new ArrayList<String>();
        paramList.add("device_id");
        ArrayList<String> valueList = new ArrayList<String>();
        valueList.add("2");
        webHandler.execute(requestList,urlList,paramList,valueList);
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        String dataToSend = params[0];
//        Log.d("FROM STATS SERVICE", dataToSend);
        this.getRecipeStatus();
        return "";
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d("ONPOSTNOTIF2 =", result);
        if (!NotificationService.isRecipeCompleteFlag){
            this.setupNextNotification(result);
        }else{
            //Kill service
        }
        super.onPostExecute(result);
    }
}
}
