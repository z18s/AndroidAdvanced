package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.ui.about.AboutFragment;
import com.example.weatherapp.ui.feedback.FeedbackFragment;
import com.example.weatherapp.ui.home.HomeFragment;
import com.example.weatherapp.ui.settings.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private MenuItem checkedMenuItem;

    private SensorService sensorService;
    private Sensor tempSensor;
    private Sensor humSensor;
    private float tempVal = 0.0f;
    private float humVal = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSensorService();

        Toolbar toolbar = initToolbar();
        initFab();
        initDrawer(toolbar);
    }

    private void initSensorService() {
        sensorService = new SensorService(this);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> setUpdatedValues());
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        initTempSensor();
        initHumSensor();
    }

    private void initTempSensor() {
        tempSensor = sensorService.getTemperatureSensor();
        if (tempSensor != null) {
            sensorService.getSensorManager().registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void initHumSensor() {
        humSensor = sensorService.getHumiditySensor();
        if (humSensor != null) {
            sensorService.getSensorManager().registerListener(this, humSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @SuppressLint("DefaultLocale")
    private void setUpdatedValues() {
        TextView textView = findViewById(R.id.text_home);
        textView.setText(String.format("Temperature: %.2f %n Humidity: %.2f", tempVal, humVal));
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorService.getSensorManager().unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (tempSensor.equals(sensorEvent.sensor)) {
            tempVal = sensorEvent.values[0];
        } else if (humSensor.equals(sensorEvent.sensor)) {
            humVal = sensorEvent.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}