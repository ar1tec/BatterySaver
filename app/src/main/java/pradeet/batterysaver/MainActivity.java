package pradeet.batterysaver;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import pradeet.batterysaver.Adapter.ViewPagerAdapter;


public class MainActivity extends ActionBarActivity implements MaterialTabListener, View.OnClickListener {

    MaterialTabHost tabs;
    ImageButton wifi;
    ImageButton bluetooth;
    ImageButton brightness;
    ImageButton gps;
    ImageButton screenTimeout;

    boolean isWifiEnable;
    boolean isBluetoothEnable;
    int brightnessvalue;
    boolean isGpsEnable;

    WifiManager wifiManager;
    BluetoothAdapter bluetoothAdapter;
    ContentResolver cResolver;

    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = this.getResources();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        wifi = (ImageButton) findViewById(R.id.wifi);
        bluetooth = (ImageButton) findViewById(R.id.bluetooth);
        brightness = (ImageButton) findViewById(R.id.brightness);
        gps = (ImageButton) findViewById(R.id.gps);
        screenTimeout = (ImageButton) findViewById(R.id.screenTimeout);

        initStates();

        if (isWifiEnable)
            wifi.setImageDrawable(res.getDrawable(R.drawable.ic_signal_wifi_4_bar_white_18dp));
        else
            wifi.setImageDrawable(res.getDrawable(R.drawable.ic_signal_wifi_off_white_18dp));

        if (isBluetoothEnable)
            if (bluetoothAdapter.isDiscovering()) {
                bluetooth.setImageDrawable(res.getDrawable(R.drawable.ic_bluetooth_white_18dp));
            } else {
                bluetooth.setImageDrawable(res.getDrawable(R.drawable.ic_bluetooth_connected_white_18dp));
            }
        else
            bluetooth.setImageDrawable(res.getDrawable(R.drawable.ic_bluetooth_disabled_white_18dp));

        brightness.setImageDrawable(res.getDrawable(R.drawable.ic_brightness_high_white_18dp));
        gps.setImageDrawable(res.getDrawable(R.drawable.ic_gps_fixed_white_18dp));
        screenTimeout.setImageDrawable(res.getDrawable(R.drawable.ic_stay_current_portrait_white_18dp));

        wifi.setOnClickListener(this);
        bluetooth.setOnClickListener(this);
        brightness.setOnClickListener(this);
        gps.setOnClickListener(this);
        screenTimeout.setOnClickListener(this);

        tabs = (MaterialTabHost) findViewById(R.id.tabs);
        pager = (ViewPager) this.findViewById(R.id.pager);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabs.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabs.addTab(tabs.newTab().setText(pagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    private void initStates() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        isWifiEnable = wifiManager.isWifiEnabled();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        isBluetoothEnable = bluetoothAdapter.isEnabled();

        cResolver = getContentResolver();
        try {
            brightnessvalue = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        pager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifi:
                if (isWifiEnable) {
                    wifiManager.setWifiEnabled(false);
                    isWifiEnable = false;
                    wifi.setImageDrawable(res.getDrawable(R.drawable.ic_signal_wifi_off_white_18dp));
                } else {
                    wifiManager.setWifiEnabled(true);
                    isWifiEnable = true;
                    wifi.setImageDrawable(res.getDrawable(R.drawable.ic_signal_wifi_4_bar_white_18dp));
                }
                break;
            case R.id.bluetooth:
                if (isBluetoothEnable) {
                    bluetoothAdapter.disable();
                    isBluetoothEnable = false;
                    bluetooth.setImageDrawable(res.getDrawable(R.drawable.ic_bluetooth_disabled_white_18dp));
                } else {
                    bluetoothAdapter.enable();
                    isBluetoothEnable = true;
                    bluetooth.setImageDrawable(res.getDrawable(R.drawable.ic_bluetooth_connected_white_18dp));
                }
                break;
            case R.id.brightness:
                showDialog();
                break;
            case R.id.gps:
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                initStates();
                break;
            case R.id.screenTimeout:
                setTimeout(getTime());
                break;
        }
    }

    int time = 15000;
    private int getTime() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(15000);
        list.add(30000);
        list.add(60000);
        list.add(120000);
        list.add(600000);
        list.add(1800000);

        int index = list.indexOf(time) + 1;
        int finaltime;
        if(index == list.size()){
            index = 0;
        }
        finaltime = list.get(index);
        time = finaltime;
        int timeinsec = time/1000;
        Toast.makeText(this, "Screen Timer set to " + timeinsec + " sec", Toast.LENGTH_SHORT).show();
        return finaltime;
    }

    private void setTimeout(int screenOffTimeout) {
        int time;
        switch (screenOffTimeout) {
            case 0:
                time = 15000;
                break;
            case 1:
                time = 30000;
                break;
            case 2:
                time = 60000;
                break;
            case 3:
                time = 120000;
                break;
            case 4:
                time = 600000;
                break;
            case 5:
                time = 1800000;
                break;
            default:
                time = -1;
        }
        android.provider.Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, time);
    }

    private void showDialog() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        seek.setMax(255);
        seek.setProgress(brightnessvalue);

        popDialog.setTitle("Brightness");
        popDialog.setView(seek);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessvalue = progress;
            }

            public void onStartTrackingTouch(SeekBar arg0) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightnessvalue);

            }
        });

        popDialog.create();
        popDialog.show();
    }
}