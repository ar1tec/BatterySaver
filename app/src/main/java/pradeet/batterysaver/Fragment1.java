package pradeet.batterysaver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    static int callTime = 0;
    ShimmerTextView batPer;
    ShimmerTextView Temperature;
    ShimmerTextView batStatus;
    ShimmerTextView totalCallTime;
    ShimmerTextView batTech;
    Shimmer shimmer;
    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        batPer = (ShimmerTextView) view.findViewById(R.id.batPer);
        batStatus = (ShimmerTextView) view.findViewById(R.id.batState);
        Temperature = (ShimmerTextView) view.findViewById(R.id.temp);
        totalCallTime = (ShimmerTextView) view.findViewById(R.id.callTime);
        batTech = (ShimmerTextView) view.findViewById(R.id.batTech);

        getBatteryPercenatge();

        if(isPhonePluggedIn(getActivity())){
            batStatus.setText("Status: Charging");
        } else {
            batStatus.setText("Status: Discharging");
        }

        getBatteryTempTech();

        totalCallTime.setText(callTime + "");

        shimmer = new Shimmer();
        shimmer.start(batPer);
        shimmer.start(batStatus);
        shimmer.start(Temperature);
        shimmer.start(totalCallTime);

        return view;
    }

    private void getBatteryTempTech() {
        BroadcastReceiver batteryTemp = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                float temperature = (float)intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;
                String Tech = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                String DEGREE = "\u00b0";
                Temperature.setText(temperature + DEGREE + "C");
                batTech.setText(Tech);

            }
        };

        IntentFilter batteryTempFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(batteryTemp, batteryTempFilter);
    }

    private void getBatteryPercenatge() {


        BroadcastReceiver batteryLevel = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level= -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }
                batPer.setText(level + "%");
            }
        };

        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(batteryLevel, batteryLevelFilter);

    }

    public static boolean isPhonePluggedIn(Context context) {
        boolean charging = false;
//        String result = "No";
        final Intent batteryIntent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status == BatteryManager.BATTERY_STATUS_CHARGING;

        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (batteryCharge)
            charging = true;
        if (usbCharge)
            charging = true;
        if (acCharge)
            charging = true;

        return charging;
    }


}
