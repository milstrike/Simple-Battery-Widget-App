package system.mil.widgetapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        AssetManager assetManager = this.getAssets();
        Typeface tf = Typeface.createFromAsset(assetManager, "fonts/Exo-ExtraLight.otf");

        TextView txi = (TextView) findViewById(R.id.text_info);
        txi.setTypeface(tf);

        startService(new Intent(this, BatteryDetector.class));
        notificate();

    }

    public void notificate(){
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.notification_icon)
                        .setContentTitle("Widget App")
                        .setContentText("Widget App is now running!")
                        .setAutoCancel(false)
                        .setOngoing(true);
        Intent resultIntent = new Intent(this, InfoActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(InfoActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
