package com.example.jaiz.positionsensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView xText,yText,zText,tText,Output;
    private Sensor mySensor;
    private SensorManager SM;
    private long t = 0;
    private Button Start,Stop;

    Boolean on;

    private double[] x = {0,0,0,0,0};
    private double[] y = {0,0,0,0,0};
    private double[] z = {0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);


        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);
        tText = (TextView) findViewById(R.id.tText);
        Output = (TextView) findViewById(R.id.Output);
        Start = (Button) findViewById(R.id.button);
        Stop = (Button) findViewById(R.id.button2);

        Start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                on = Boolean.TRUE;
            }
        });
        Stop.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                on = Boolean.FALSE;
            }
        });


    }

    public double[] Shift(double s[], double a){
        for (int i = 0; i<s.length-1;i++){
            s[i]=s[i+1];
        }
        s[s.length-1]=a;
        return s;
    }

    public double SGFilter(double s[]){
        if (s.length==5){
            return (-3*s[0]+12*s[1]+17*s[2]+12*s[3]-3*s[4])/35;
        }
        else return -1;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (on == Boolean.TRUE){

            this.Shift(x,(double) sensorEvent.values[0]);
            this.Shift(y,(double) sensorEvent.values[1]);
            this.Shift(z,(double) sensorEvent.values[2]);



            xText.setText("Accelerometer X:  " + this.SGFilter(x));
            yText.setText("Accelerometer Y:  " + this.SGFilter(y));
            zText.setText("Accelerometer Z:  " + this.SGFilter(z));
            t++;
            tText.setText("Time:  "+ t);
            if(sensorEvent.values[1]>8.0) Output.setText("Standing");
            else if (sensorEvent.values[2]>8.0) Output.setText("Laying");
            else Output.setText("Ideal");
        }
        else{
            t=0;
            xText.setText("Accelerometer X:  NA");
            yText.setText("Accelerometer Y:  NA");
            zText.setText("Accelerometer Z:  NA");
            tText.setText("Time:  NA");
            Output.setText("Paused");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
