package com.example.lab4task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ScaleGestureDetector scaleGestureDetector;

    ScaleListener scaleListener = new ScaleListener();
    private float mScaleFactor = 1.0f;
    ImageView imageView;
    SensorManager sensorManager;
    Sensor proximitySensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximitySensor==null){
            Toast.makeText(this,"No Proximity Sensor available.",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            sensorManager.registerListener(proximitySensorEventListener,proximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        scaleGestureDetector = new ScaleGestureDetector(this,new ScaleListener());
        imageView = findViewById(R.id.imageView);
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }

    }
    SensorEventListener proximitySensorEventListener
            = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY){
                if(sensorEvent.values[0]==0){
                    Toast.makeText(getApplicationContext(),"Object is near",Toast.LENGTH_SHORT).show();
                    mScaleFactor = 0.8f;
                    scaleListener.onScale(scaleGestureDetector);
                }
               /* if(sensorEvent.values[0]>0 && sensorEvent.values[0]<1){
                    Toast.makeText(getApplicationContext(),"Object is in middle",Toast.LENGTH_SHORT).show();
                    mScaleFactor = 0.6f;
                    scaleListener.onScale(scaleGestureDetector);
                }*/
                else{
                    Toast.makeText(getApplicationContext(),"Object is far",Toast.LENGTH_SHORT).show();
                    mScaleFactor = 0.4f;
                    scaleListener.onScale(scaleGestureDetector);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
