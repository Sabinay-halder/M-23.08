package com.widevision.pregnantwoman.kankan.wheel.widget.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("InflateParams")
public class TimePickerViewAndroid {

    private final Activity context;
    private String timeToSet = "";
    private boolean is24Hours;
    private int selected_minute_value, selected_hour_value;
    private final String[] hour24Array = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private final String[] hour12Array = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};
    private final String[] minutesArray = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
    private String[] hourArray;
    private WheelView hour, minute;
    private TextView timeText, AmPm;
    private HourAdapter hourAdapter;
    private MinuteAdapter minuteAdapter;
    private LinearLayout TimeWheelLayout, AmPmWheel;
    private Typeface fontLight, fontBold;
    private int largeFontSize = 27, smallFontSize = 18;
    private MyListener listener;
    private Dialog dialog;

    public TimePickerViewAndroid(Activity context, String timeToSet, boolean is24Hours, MyListener listener) {
        this.context = context;
        this.timeToSet = timeToSet;
        this.is24Hours = is24Hours;

        if (is24Hours) {
            hourArray = hour24Array;
        } else {
            hourArray = hour12Array;
        }
        this.listener = listener;
        dialog = new Dialog(context, R.style.DialogSlideAnim);
    }

    public TimePickerViewAndroid(Activity context, MyListener listener) {
        this.context = context;
        is24Hours = false;
        this.listener = listener;
        dialog = new Dialog(context, R.style.DialogSlideAnim);
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void setPicker() {
        final View view = context.getLayoutInflater().inflate(R.layout.time_picker_android_layout, null);
        TimeWheelLayout = (LinearLayout) view.findViewById(R.id.TimeWheelLayout);
        AmPmWheel = (LinearLayout) view.findViewById(R.id.AmPmWheel);

        if (!is24Hours) {
            TimeWheelLayout.setWeightSum(10);
            AmPmWheel.setVisibility(View.VISIBLE);
            hourArray = hour12Array;
        } else {
            TimeWheelLayout.setWeightSum(6);
            AmPmWheel.setVisibility(View.GONE);
            hourArray = hour24Array;
        }

        fontLight = Typeface.createFromAsset(context.getAssets(), "Oregon LDO Medium.ttf");
        fontBold = Typeface.createFromAsset(context.getAssets(), "Oregon LDO Black.ttf");
        timeText = (TextView) view.findViewById(R.id.dateText);
        AmPm = (TextView) view.findViewById(R.id.AmPm);
        timeText.setTypeface(fontLight);
        hour = (WheelView) view.findViewById(R.id.hourWheel);
        hour.setCyclic(true);
        minute = (WheelView) view.findViewById(R.id.minuteWheel);
        minute.setCyclic(true);

        hourAdapter = new HourAdapter(context, hourArray);
        hour.setViewAdapter(hourAdapter);

        minuteAdapter = new MinuteAdapter(context, minutesArray);
        minute.setViewAdapter(minuteAdapter);


        ImageButton doneButton = (ImageButton) view.findViewById(R.id.doneButton);
        ImageButton cancelButton = (ImageButton) view.findViewById(R.id.cancelButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSet(timeText.getText().toString().trim());
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        hour.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selected_hour_value = newValue;

                if (!is24Hours) {
                    timeText.setText(hourArray[selected_hour_value] + ":" + minutesArray[selected_minute_value] + " " + AmPm.getText().toString());
                } else {
                    timeText.setText(hourArray[selected_hour_value] + ":" + minutesArray[selected_minute_value]);
                }

                hourAdapter.notifyDataChangedEvent();

            }
        });
        minute.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selected_minute_value = newValue;

                if (!is24Hours) {
                    timeText.setText(hourArray[selected_hour_value] + ":" + minutesArray[selected_minute_value] + " " + AmPm.getText().toString());
                } else {
                    timeText.setText(hourArray[selected_hour_value] + ":" + minutesArray[selected_minute_value]);
                }

                minuteAdapter.notifyDataChangedEvent();
            }

        });

        AmPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AmPm.getText().toString().equals("AM")) {
                    AmPm.setText("PM");
                } else {
                    AmPm.setText("AM");
                }
            }
        });

        if (timeToSet != null && timeToSet.trim().length() > 0) {

            try {
                DateTimeFormatter formatter;
                if (!is24Hours) {
                    formatter = DateTimeFormat.forPattern("HH:mm a");
                } else {
                    formatter = DateTimeFormat.forPattern("HH:mm");
                }
                LocalTime date = formatter.parseLocalTime(timeToSet);
                System.out.println(date.getHourOfDay());  // 2012
                System.out.println(date.getMinuteOfHour()); // 8

                int hourindex = -1, minindex = -1;
                for (int i = 0; i < hour24Array.length; i++) {
                    if (hour24Array[i] == "" + date.getHourOfDay()) {
                        hourindex = i;
                        break;
                    }
                }

                for (int i = 0; i < minutesArray.length; i++) {
                    if (minutesArray[i] == "" + date.getMinuteOfHour()) {
                        minindex = i;
                        break;
                    }
                }

                if (hourindex >= 0) {
                    hour.setCurrentItem(hourindex);
                }
                if (minindex >= 0) {
                    minute.setCurrentItem(minindex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            String format;
            if (!is24Hours) {
                format = "HH:mm a";
            } else {
                format = "HH:mm";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date dateCurrent = new Date();
            String dateToSet = dateFormat.format(dateCurrent);

            try {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
                LocalTime date = formatter.parseLocalTime(dateToSet);

                int hourindex = -1, minindex = -1;
                for (int i = 0; i < hour24Array.length; i++) {
                    if (hour24Array[i] == "" + date.getHourOfDay()) {
                        hourindex = i;
                        break;
                    }
                }

                for (int i = 0; i < minutesArray.length; i++) {
                    if (minutesArray[i] == "" + date.getMinuteOfHour()) {
                        minindex = i;
                        break;
                    }
                }

                if (hourindex >= 0) {
                    hour.setCurrentItem(hourindex);
                }
                if (minindex >= 0) {
                    minute.setCurrentItem(minindex);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Adapter for daysArray
     */
    private class HourAdapter extends AbstractWheelTextAdapter {
        final String[] daysArray;

        /**
         * Constructor
         */
        protected HourAdapter(Context context, String[] daysArray) {
            super(context, R.layout.date_text_adapter, NO_RESOURCE);
            this.daysArray = daysArray;
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView img = (TextView) view.findViewById(R.id.country_name);

            String test = "" + daysArray[index];
            img.setText(test);

            try {
                if (test.equals(daysArray[selected_hour_value])) {
                    img.setTypeface(fontBold);
                    img.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeFontSize);

                } else {
                    img.setTypeface(fontLight);
                    img.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallFontSize);
                }
            } catch (Exception e) {

            }


            return view;
        }

        @Override
        public int getItemsCount() {
            return daysArray.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return "" + daysArray[index];
        }
    }

    /**
     * Adapter for daysArray
     */
    private class MinuteAdapter extends AbstractWheelTextAdapter {
        final String[] countries;

        /**
         * Constructor
         */
        MinuteAdapter(Context context, String[] countries) {
            super(context, R.layout.date_text_adapter, NO_RESOURCE);
            this.countries = countries;
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView img = (TextView) view.findViewById(R.id.country_name);

            String test = "" + countries[index];
            img.setText(test);

            if (test.equals(countries[selected_minute_value])) {
                img.setTypeface(fontBold);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeFontSize);

            } else {
                img.setTypeface(fontLight);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallFontSize);
            }

            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }


}
