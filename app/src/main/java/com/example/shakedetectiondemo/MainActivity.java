package com.example.shakedetectiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.VibrationEffect.createOneShot;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    boolean isAvailable, isNotFirstTime = false;
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView textView1, textView2, textView3;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float diffX, diffY, diffZ;
    private float shakeThresold = 5f;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView);
        textView3 = findViewById(R.id.textView3);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAvailable = true;
        } else
            Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        textView1.setText("" + sensorEvent.values[0]);
        textView2.setText("" + sensorEvent.values[1]);
        textView3.setText("" + sensorEvent.values[2]);
        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];
        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        if (isNotFirstTime) {
            diffX = Math.abs(lastX - currentX);
            diffY = Math.abs(lastY - currentY);
            diffZ = Math.abs(lastZ - currentZ);
        }
        isNotFirstTime = true;
        if ((diffX > shakeThresold && diffY > shakeThresold) ||
                (diffX > shakeThresold && diffZ > shakeThresold) ||
                (diffY > shakeThresold && diffZ > shakeThresold)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ;
            vibrator.vibrate(createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else
            vibrator.vibrate(500);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAvailable)
            sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAvailable) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}