package com.example.wearos;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wearos.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    private SensorManager sm;
    private Sensor s;
    private TextView textview;
    private SensorEventListener sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = findViewById(R.id.textview);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sm !=null)s = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sv = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, event.values);

                float[] remappedRotationMatrix = new float[16];

                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                for(int i = 0; i < 3; i++) {
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }

                textview.setText(String.valueOf((int)orientations[2]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(sv,s,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(sv);
    }
}