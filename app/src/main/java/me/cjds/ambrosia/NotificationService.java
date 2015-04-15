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

import java.io.IOException;

public class NotificationService extends Service {
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
        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        new DoBackgroundTask().execute("");
        Log.d(TAG, "onStart");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }


private class DoBackgroundTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        String dataToSend = params[0];
        Log.i("FROM STATS SERVICE", dataToSend);
      /*  HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://google.com");
        try {
            httpPost.setEntity(new StringEntity(dataToSend, "UTF-8"));

            // Set up the header types needed to properly transfer JSON
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept-Encoding", "application/json");
            httpPost.setHeader("Accept-Language", "en-US");

            // Execute POST
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            if (responseEntity != null) {
                //response = EntityUtils.toString(responseEntity);
            } else {
                response = "{\"NO DATA:\"NO DATA\"}";
            }
        } catch (ClientProtocolException e) {
            response = "{\"ERROR\":" + e.getMessage().toString() + "}";
        } catch (IOException e) {
            response = "{\"ERROR\":" + e.getMessage().toString() + "}";
        }
        return response;*/
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        Notification.Builder mBuilder =
                new Notification.Builder(NotificationService.this)
                        .setSmallIcon(android.R.drawable.ic_menu_compass)
                        .setContentTitle("My notification"+NotificationService.count++)
                        .setContentText("Hello World!");
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
        //Utilities.STATUS = result;
        //Log.i("FROM STATUS SERVICE: STATUS IS:", Utilities.STATUS);
        super.onPostExecute(result);
    }
}
}
