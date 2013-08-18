package com.example.batterylevelreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Used for finding the battery level of an Android-based phone.
 * 
 * @author Mihai Fonoage
 *
 */
public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private TextView batterLevel;

    @Override
    /**
     * Called when the current activity is first created.
     */
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        batterLevel = (TextView) this.findViewById(R.id.editTextLocation);
        batterLevel.setText("your battery status is ok");
        batteryLevel();
    }

    /**
     * Computes the battery level by registering a receiver to the intent triggered 
     * by a battery status/level change.
     */
    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                batterLevel.setText("Battery Level Remaining: " + level + "%");
                if(level<30)
                	Toast.makeText(getBaseContext(), "your battery is very low", Toast.LENGTH_LONG).show();
                else
                	Toast.makeText(getBaseContext(), "your battery is fine", Toast.LENGTH_LONG).show();
                
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
       // IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }
    
}