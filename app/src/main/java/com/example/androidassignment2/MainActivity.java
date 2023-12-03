package com.example.androidassignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    SurfaceView surfaceView;
    ClockSurfaceView clockSurfaceView = null;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar t = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(t);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        surfaceView = findViewById(R.id.surfaceViewClock);
        clockSurfaceView = new ClockSurfaceView(surfaceView, 400);
        bottomNavigationView.setSelectedItemId(R.id.clock);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.clock)
                    return true;
                else if (item.getItemId() == R.id.timer) {
                    Intent i = new Intent(MainActivity.this, TimerAlarmActivity.class);
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        clockSurfaceView.onPauseClock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clockSurfaceView.onResumeClock();
    }
}