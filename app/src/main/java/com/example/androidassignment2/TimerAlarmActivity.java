package com.example.androidassignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TimerAlarmActivity extends AppCompatActivity {
    SurfaceView surfaceView = null;
    EditText editText;
    Button startTimer, stopTimer, updateTimer;

    BottomNavigationView bottomNavigationView;

    TimerSurfaceView timerSurfaceView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_alarm);

        Toolbar t = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(t);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        surfaceView = findViewById(R.id.surfaceViewTimer);

        editText = findViewById(R.id.timerValue);
        startTimer = findViewById(R.id.startTimer);
        stopTimer = findViewById(R.id.stopTimer);
        updateTimer = findViewById(R.id.updateTimer);

        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int timerInSeconds = Integer.parseInt(editText.getText().toString());
                timerSurfaceView = new TimerSurfaceView(surfaceView, timerInSeconds);
            }
        });

        stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerSurfaceView.onPauseTimer();
            }
        });

        updateTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerSurfaceView.onRestartTimer();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.timer);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.timer)
                    return true;
                else if (item.getItemId() == R.id.clock) {
                    Intent i = new Intent(TimerAlarmActivity.this, MainActivity.class);
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });
    }
}