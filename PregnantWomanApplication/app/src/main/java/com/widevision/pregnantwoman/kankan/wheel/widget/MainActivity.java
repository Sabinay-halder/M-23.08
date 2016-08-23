package com.widevision.pregnantwoman.kankan.wheel.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.TimePickerViewAndroid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements MyListener {


    Button forDatePicker, forTimePicker;
    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        forDatePicker = (Button) findViewById(R.id.forDatePicker);
        forTimePicker = (Button) findViewById(R.id.forTimePicker);
        root = (LinearLayout) findViewById(R.id.root);

        forDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String dateToSet = dateFormat.format(date);
                System.out.println(dateFormat.format(date));

                DatePickerViewAndroid picker = new DatePickerViewAndroid(MainActivity.this, dateToSet, "yyyy/MM/dd", MainActivity.this);
                picker.setPicker();
                picker.show();
            }
        });

        forTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
                Date date = new Date();
                String dateToSet = dateFormat.format(date);
                System.out.println(dateFormat.format(date));

                TimePickerViewAndroid picker = new TimePickerViewAndroid(MainActivity.this, new MyListener() {
                    @Override
                    public void onSet(String time) {
                        Toast.makeText(MainActivity.this, "selected time " + time, Toast.LENGTH_SHORT).show();
                    }
                });
                picker.setPicker();
                picker.show();
            }
        });
    }

    @Override
    public void onSet(String date) {
        Toast.makeText(MainActivity.this, "selected date " + date, Toast.LENGTH_SHORT).show();
    }
}
