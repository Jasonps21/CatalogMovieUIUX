package com.example.jason.catalogmovieuiux.scheduler;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.example.jason.catalogmovieuiux.BuildConfig;
import com.example.jason.catalogmovieuiux.MovieDetail;
import com.example.jason.catalogmovieuiux.model.MovieItems;
import com.example.jason.catalogmovieuiux.R;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class SchedulerService extends GcmTaskService {

    private final static String GROUP_KEY_MOVIE = "group_key_movie";
    public static String TAG_UPCOMING = "tag_upcoming";
    List<MovieItems> stackNotif = new ArrayList<>();
    private int idNotif = 0;
    private int maxNotif = 3;

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_UPCOMING)) {
            loadData();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void loadData() {
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.movieDB_Api_Key + "&language=en-US";
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<MovieItems> moviedata = new ArrayList<>();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    MovieItems movieItems = new MovieItems();
                    String result = new String(responseBody);
                    JSONObject responObject = new JSONObject(result);
                    int page = responObject.getInt("page");
                    JSONArray movieList = responObject.getJSONArray("results");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String tanggalSekarang = format.format(new Date());
//                    String tanggalSekarang = "2018-06-14";

                    for (int i = 0; i < movieList.length(); i++) {
                        JSONObject data = movieList.getJSONObject(i);
                        if (tanggalSekarang.equalsIgnoreCase(data.getString("release_date"))) {
                            String judul = data.getString("original_title");

                            String message = "Hari ini " + judul + " Release";
                            int notifId = 100;


                            showNotification(getApplicationContext(), judul, message, notifId);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        SchedulerTask mSchedulerTask = new SchedulerTask(this);
        mSchedulerTask.createPeriodicTask();
    }

    private void showNotification(Context context, String title, String message, int notifId) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        Notification notification = null;
//        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MovieDetail.class);
        intent.putExtra(MovieDetail.EXTRA_JUDUL, title);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (idNotif < maxNotif) {
            notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    .setContentIntent(pendingIntent)
                    .setGroup(GROUP_KEY_MOVIE)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(alarmSound)
                    .build();
        }


        manager.notify(notifId, notification);
    }
}
