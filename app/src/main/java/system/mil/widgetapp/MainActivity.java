package system.mil.widgetapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import system.mil.widgetapp.SimpleGestureFilter.SimpleGestureListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SimpleGestureListener {

    private TextView txclock, txsecond, txdate, txbatt;
    private Calendar cal;
    private SimpleDateFormat sdf, sds, sdd;
    private SimpleGestureFilter detector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, BatteryDetector.class));

        detector = new SimpleGestureFilter(this,this);

        AssetManager assetManager = this.getAssets();
        Typeface tf = Typeface.createFromAsset(assetManager, "fonts/digital-italic.ttf");
        Typeface tg = Typeface.createFromAsset(assetManager, "fonts/digital-standard.ttf");
        Typeface th = Typeface.createFromAsset(assetManager, "fonts/Exo-ExtraLight.otf");

        txclock = (TextView) findViewById(R.id.clock_text);
        txsecond = (TextView) findViewById(R.id.second_text);
        txdate = (TextView) findViewById(R.id.text_date);
        txbatt = (TextView) findViewById(R.id.text_battery);
        txclock.setTypeface(tf);
        txsecond.setTypeface(tf);
        txdate.setTypeface(tg);
        txbatt.setTypeface(th);



        Timer timer = new Timer();
        timer.schedule(new getClock(), 0, 1000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            txbatt.setText(String.valueOf(level) + "%");
        }
    };

    private final BroadcastReceiver abcd = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    public void getBatteryState(){
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.registerReceiver(abcd, new IntentFilter("xyz"));
    }

    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT :
                finish();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :
                break;
            case SimpleGestureFilter.SWIPE_DOWN :
                break;
            case SimpleGestureFilter.SWIPE_UP :
                break;
        }
    }

    @Override
    public void onDoubleTap() {

    }

    class getClock extends TimerTask{
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cal = Calendar.getInstance();
                    sdf = new SimpleDateFormat("HH:mm");
                    sds = new SimpleDateFormat("ss");
                    sdd = new SimpleDateFormat("dd MMMM yyyy");
                    txclock.setText(sdf.format(cal.getTime()));
                    txsecond.setText(sds.format(cal.getTime()));
                    txdate.setText(sdd.format(cal.getTime()));
                    getBatteryState();
                }
            });
        }
    }


}
