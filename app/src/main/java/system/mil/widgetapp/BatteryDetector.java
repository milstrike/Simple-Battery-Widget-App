package system.mil.widgetapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

public class BatteryDetector extends Service {

    public int ok = 1;

    public BatteryDetector() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getBatteryState();
        return super.onStartCommand(intent, flags, startId);
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            notificate();
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if(plugged == BatteryManager.BATTERY_PLUGGED_AC){
                if(ok == 1){
                    ok = 0;
                    startActivity();
                }
                else{

                }
            }
            else{
                ok = 1;
                finishActivity();
            }

        }
    };

    public void getBatteryState(){
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void startActivity(){
        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
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

    public void finishActivity(){
        sendBroadcast(new Intent("xyz"));
    }
}
