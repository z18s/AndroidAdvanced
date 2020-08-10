package com.example.weatherapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.weatherapp.model.sensors.SensorValues;
import com.example.weatherapp.model.sensors.SensorValuesHolder;
import com.example.weatherapp.model.weather.IWorker;
import com.example.weatherapp.model.weather.UpdateWorker;
import com.example.weatherapp.model.weather.WeatherValues;
import com.example.weatherapp.ui.about.AboutFragment;
import com.example.weatherapp.ui.feedback.FeedbackFragment;
import com.example.weatherapp.ui.home.HomeFragment;
import com.example.weatherapp.ui.settings.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener, SensorValuesHolder, IWorker {

    private MenuItem checkedMenuItem;

    private SensorValues sensorValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSensors();

        Toolbar toolbar = initToolbar();
        initFab();
        initDrawer(toolbar);

        //startPeriodicWorker();
    }

    private void initSensors() {
        sensorValues = new SensorValues(this);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

        });
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void startOneTimeWorker() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest
                .Builder(UpdateWorker.class)
                .setInitialDelay(UpdateWorker.WORKER_ONETIME_DELAY_SEC, TimeUnit.SECONDS)
                .addTag(UpdateWorker.WORKER_ONETIME_TAG)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
        onWorkerUpdated(workRequest);
    }

    public void cancelOneTimeWorker() {
        cancelWorker(UpdateWorker.WORKER_ONETIME_TAG);
    }

    private void startPeriodicWorker() {
        PeriodicWorkRequest workPeriodicRequest = new PeriodicWorkRequest
                .Builder(UpdateWorker.class, UpdateWorker.WORKER_PERIODIC_INTERVAL_MIN, TimeUnit.MINUTES)
                .addTag(UpdateWorker.WORKER_PERIODIC_TAG)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(workPeriodicRequest);
        onWorkerUpdated(workPeriodicRequest);
    }

    public void cancelPeriodicWorker() {
        cancelWorker(UpdateWorker.WORKER_PERIODIC_TAG);
    }

    private void onWorkerUpdated(WorkRequest workRequest) {
        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, workInfo -> {

                    Log.d("DEBUG_MainActivity", "onChanged - " + workInfo.getState());

                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                        Log.d("DEBUG_MainActivity", "updateValues");
                        updateValues();
                    }
                });
    }

    public void updateValues() {
        String cityName = WeatherValues.getInstance().getCityName();
        Log.d("DEBUG_MainActivity", "updateValues.cityName = " + cityName);

        String temperature = WeatherValues.getInstance().getTempString();
        Log.d("DEBUG_MainActivity", "updateValues.temperature = " + temperature);

        String iconUrl = WeatherValues.getInstance().getIconUrl();
        Log.d("DEBUG_MainActivity", "updateValues.iconUrl = " + iconUrl);

        setTextOnTextView(R.id.text_home, cityName);
        setTextOnTextView(R.id.temp_value, temperature);
        setIconOnImageView(R.id.temp_pic, iconUrl);
    }

    private void setTextOnTextView(int viewId, String text) {
        ((TextView) findViewById(viewId)).setText(text);
    }

    private void setIconOnImageView(int viewId, String url) {
        Picasso.get()
                .load(url)
                .into((ImageView) findViewById(viewId));
    }

    private void cancelWorker(String tag) {
        WorkManager.getInstance(getApplicationContext()).getWorkInfosByTagLiveData(tag);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        clearSelectedMenuItem(checkedMenuItem);
        checkedMenuItem = item;
        markSelectedMenuItem(checkedMenuItem);

        int itemId = item.getItemId();

        switch (itemId) {
            case (R.id.nav_home):
                replaceFragmentTransaction(new HomeFragment());
                break;
            case (R.id.nav_feedback):
                replaceFragmentTransaction(new FeedbackFragment());
                break;
            case (R.id.nav_about):
                replaceFragmentTransaction(new AboutFragment());
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragmentTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }

    private void clearSelectedMenuItem(MenuItem checkedMenuItem) {
        if (checkedMenuItem != null) {
            checkedMenuItem.setChecked(false);
        }
    }

    private void markSelectedMenuItem(MenuItem item) {
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_settings) {
            replaceFragmentTransaction(new SettingsFragment());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorValues.registerListeners(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorValues.unregisterListeners(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelPeriodicWorker();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        sensorValues.update(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public SensorValues getSensorValues() {
        return sensorValues;
    }
}