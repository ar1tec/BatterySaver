package pradeet.batterysaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

public class PhoneStateListener extends BroadcastReceiver {
    public PhoneStateListener() {
    }

    static boolean flag = false;
    static long start_time, end_time;

    @Override
    public void onReceive(Context arg0, Intent intent) {
        String action = intent.getAction();
        if (action.equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                    TelephonyManager.EXTRA_STATE_RINGING)) {
                start_time = System.currentTimeMillis();
            }
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                    TelephonyManager.EXTRA_STATE_IDLE)) {
                end_time = System.currentTimeMillis();
                //Total time talked =
                long total_time = end_time - start_time;
                Fragment1.callTime += total_time;
            }
        }
    }
}
