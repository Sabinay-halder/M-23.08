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
import android.widget.TextView;

import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


@SuppressLint("InflateParams")
public class DatePickerViewAndroid {

    private final Activity context;
    private String dateToSet = "", format;
    private int selected_year_value, selected_month_value, selected_day_value;
    private int yearArray[];
    private final String[] monthArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final int[] monthArray31 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    private final int[] monthArray30 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    private final int[] monthArray29 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
    private final int[] monthArray28 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};
    private int[] daysArray = {};
    private WheelView days;
    private TextView dateText;
    private DaysAdapter daysAdapter;
    private MonthAdapter monthAdapter;
    private YearAdapter yearAdapter;
    private Typeface fontLight, fontBold;
    private int largeFontSize = 22, smallFontSize = 16;
    private Dialog dialog;
    private MyListener listener;

    public DatePickerViewAndroid(Activity context, String dateToSet, String format, MyListener listener) {
        this.context = context;
        this.dateToSet = dateToSet;
        this.format = format;
        this.listener = listener;
        dialog = new Dialog(context, R.style.DialogSlideAnim);
    }

    public DatePickerViewAndroid(Activity context) {
        this.context = context;
        dialog = new Dialog(context, R.style.DialogSlideAnim);

    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }


    public void setPicker() {
        final View view = context.getLayoutInflater().inflate(R.layout.date_picker_android_layout, null);
        int minYear = 1970;
        int maxYear = 2035;
        int differentBetweenYears = maxYear - minYear;
        if (differentBetweenYears >= 1) {
            yearArray = new int[differentBetweenYears];
            if (differentBetweenYears == 1) {
                yearArray[0] = minYear;
            } else {
                for (int i = 0; i < differentBetweenYears; i++) {
                    yearArray[i] = minYear + i;
                }
            }
        } else {
            throw new ArithmeticException("Max must be greater than Min");
        }

        fontLight = Typeface.createFromAsset(context.getAssets(), "Oregon LDO Medium.ttf");
        fontBold = Typeface.createFromAsset(context.getAssets(), "Oregon LDO Black.ttf");
        dateText = (TextView) view.findViewById(R.id.dateText);
        dateText.setTypeface(fontBold);
        days = (WheelView) view.findViewById(R.id.cityWheel);
        WheelView year = (WheelView) view.findViewById(R.id.yearWheel);
        WheelView month = (WheelView) view.findViewById(R.id.monthWheel);

        daysArray = monthArray31;
        daysAdapter = new DaysAdapter(context, monthArray31);
        days.setViewAdapter(daysAdapter);

        yearAdapter = new YearAdapter(context, yearArray);
        year.setViewAdapter(yearAdapter);

        monthAdapter = new MonthAdapter(context, monthArray);
        month.setViewAdapter(monthAdapter);

        month.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

            }
        });

        ImageButton doneButton = (ImageButton) view.findViewById(R.id.doneButton);
        ImageButton cancleButton = (ImageButton) view.findViewById(R.id.cancelButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSet(dateText.getText().toString());
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        days.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selected_day_value = newValue;

                try {
                    dateText.setText(daysArray[selected_day_value] + "-" + monthArray[selected_month_value] + "-" + yearArray[selected_year_value]);
                } catch (Exception e) {
                    selected_day_value = selected_day_value - 1;
                    dateText.setText(daysArray[selected_day_value] + "-" + monthArray[selected_month_value] + "-" + yearArray[selected_year_value]);

                }
                daysAdapter.notifyDataChangedEvent();
            }
        });

        year.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selected_year_value = newValue;
                if (monthArray[selected_month_value].equals("February")) {
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    if (gregorianCalendar.isLeapYear(yearArray[selected_year_value])) {
                        days.setViewAdapter(new DaysAdapter(context, monthArray29));
                        if (selected_day_value >= 29) {
                            days.setCurrentItem(28);
                        } else {
                            days.setCurrentItem(selected_day_value);
                        }
                    } else {
                        days.setViewAdapter(new DaysAdapter(context, monthArray28));
                        if (selected_day_value >= 28) {
                            days.setCurrentItem(27);
                        } else {
                            days.setCurrentItem(selected_day_value);
                        }
                    }
                } else {
                    switch (monthArray[selected_month_value]) {
                        case "January":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray31;
                            days.setViewAdapter(new DaysAdapter(context, monthArray31));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "March":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray31;
                            days.setViewAdapter(new DaysAdapter(context, monthArray31));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "April":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray30;
                            days.setViewAdapter(new DaysAdapter(context, monthArray30));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "May":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray31;
                            days.setViewAdapter(new DaysAdapter(context, monthArray31));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "June":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray30;
                            days.setViewAdapter(new DaysAdapter(context, monthArray30));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "July":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray31;
                            days.setViewAdapter(new DaysAdapter(context, monthArray31));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "August":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray31;
                            days.setViewAdapter(new DaysAdapter(context, monthArray31));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "September":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray30;
                            days.setViewAdapter(new DaysAdapter(context, monthArray30));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "October":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray31;
                            days.setViewAdapter(new DaysAdapter(context, monthArray31));
                            days.setCurrentItem(selected_day_value);
                            break;
                        case "November":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray30;
                            days.setViewAdapter(new DaysAdapter(context, monthArray30));
                            days.setCurrentItem(selected_day_value);
                            break;

                        case "December":
                            days = (WheelView) view.findViewById(R.id.cityWheel);

                            daysArray = monthArray31;
                            days.setViewAdapter(new DaysAdapter(context, monthArray31));
                            days.setCurrentItem(selected_day_value);
                            break;
                    }

                    yearAdapter.notifyDataChangedEvent();
                    try {
                        dateText.setText(daysArray[selected_day_value] + "-" + monthArray[selected_month_value] + "-" + yearArray[selected_year_value]);
                    } catch (Exception e) {
                        selected_day_value = selected_day_value - 1;
                        dateText.setText(daysArray[selected_day_value] + "-" + monthArray[selected_month_value] + "-" + yearArray[selected_year_value]);
                    }
                }
            }
        });

        month.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selected_month_value = newValue;

                switch (monthArray[newValue]) {
                    case "January":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray31;
                        days.setViewAdapter(new DaysAdapter(context, monthArray31));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "February":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        GregorianCalendar gregorianCalendar = new GregorianCalendar();
                        if (gregorianCalendar.isLeapYear(yearArray[selected_year_value])) {
                            daysArray = monthArray29;
                            days.setViewAdapter(new DaysAdapter(context, monthArray29));
                            if (selected_day_value >= 29) {
                                days.setCurrentItem(29);
                            } else {
                                days.setCurrentItem(selected_day_value);
                            }
                        } else {
                            daysArray = monthArray28;
                            days.setViewAdapter(new DaysAdapter(context, monthArray28));
                            if (selected_day_value >= 28) {
                                days.setCurrentItem(28);
                            } else {
                                days.setCurrentItem(selected_day_value);
                            }
                        }
                        break;
                    case "March":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray31;
                        days.setViewAdapter(new DaysAdapter(context, monthArray31));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "April":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray30;
                        days.setViewAdapter(new DaysAdapter(context, monthArray30));
                        days.setCurrentItem(selected_day_value - 1);
                        break;

                    case "May":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray31;
                        days.setViewAdapter(new DaysAdapter(context, monthArray31));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "June":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray30;
                        days.setViewAdapter(new DaysAdapter(context, monthArray30));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "July":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray31;
                        days.setViewAdapter(new DaysAdapter(context, monthArray31));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "August":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray31;
                        days.setViewAdapter(new DaysAdapter(context, monthArray31));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "September":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray30;
                        days.setViewAdapter(new DaysAdapter(context, monthArray30));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "October":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray31;
                        days.setViewAdapter(new DaysAdapter(context, monthArray31));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "November":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray30;
                        days.setViewAdapter(new DaysAdapter(context, monthArray30));
                        days.setCurrentItem(selected_day_value);
                        break;

                    case "December":
                        days = (WheelView) view.findViewById(R.id.cityWheel);

                        daysArray = monthArray31;
                        days.setViewAdapter(new DaysAdapter(context, monthArray31));
                        days.setCurrentItem(selected_day_value);
                        break;
                }

                monthAdapter.notifyDataChangedEvent();
                try {
                    dateText.setText(daysArray[selected_day_value] + "-" + monthArray[selected_month_value] + "-" + yearArray[selected_year_value]);
                } catch (Exception e) {
                    try {
                        selected_day_value = selected_day_value - 1;
                        dateText.setText(daysArray[selected_day_value] + "-" + monthArray[selected_month_value] + "-" + yearArray[selected_year_value]);
                    } catch (Exception e1) {
                        selected_day_value = selected_day_value - 1;
                        dateText.setText(daysArray[selected_day_value] + "-" + monthArray[selected_month_value] + "-" + yearArray[selected_year_value]);
                    }
                }
            }
        });

        if (dateToSet != null && dateToSet.trim().length() > 0) {

            try {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(format);

                LocalDate date = formatter.parseLocalDate(dateToSet);

                System.out.println(date.getYear());  // 2012
                System.out.println(date.getMonthOfYear()); // 8
                System.out.println(date.getDayOfMonth());   // 18
                int yearindex = -1;
                for (int i = 0; i < yearArray.length; i++) {
                    if (yearArray[i] == date.getYear()) {
                        yearindex = i;
                        break;
                    }
                }
                if (yearindex >= 0) {
                    days.setCurrentItem(date.getDayOfMonth() - 1);
                    year.setCurrentItem(yearindex);
                    month.setCurrentItem(date.getMonthOfYear() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            format = "yyyy/MM/dd";
            DateFormat dateFormat = new SimpleDateFormat(format);
            Date dateCurrent = new Date();
            String dateToSet = dateFormat.format(dateCurrent);

            try {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(format);

                LocalDate date = formatter.parseLocalDate(dateToSet);

                System.out.println(date.getYear());  // 2012
                System.out.println(date.getMonthOfYear()); // 8
                System.out.println(date.getDayOfMonth());   // 18
                int yearindex = -1;
                for (int i = 0; i < yearArray.length; i++) {
                    if (yearArray[i] == date.getYear()) {
                        yearindex = i;
                        break;
                    }
                }
                if (yearindex >= 0) {
                    days.setCurrentItem(date.getDayOfMonth() - 1);
                    year.setCurrentItem(yearindex);
                    month.setCurrentItem(date.getMonthOfYear() - 1);
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
    private class DaysAdapter extends AbstractWheelTextAdapter {
        final int[] daysArray;

        /**
         * Constructor
         */
        protected DaysAdapter(Context context, int[] daysArray) {
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
                if (test.equals("" + daysArray[selected_day_value])) {
                    img.setTypeface(fontBold);
                    img.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeFontSize);
                    img.setTextColor(Color.parseColor("#000023"));
                } else {
                    img.setTypeface(fontBold);
                    img.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallFontSize);
                    img.setTextColor(Color.parseColor("#A1242E76"));
                }
            } catch (Exception e) {
                selected_day_value = daysArray.length - 1;
                if (test.equals("" + daysArray[selected_day_value])) {
                    img.setTypeface(fontBold);
                    img.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeFontSize);
                    img.setTextColor(Color.parseColor("#000023"));
                } else {
                    img.setTypeface(fontBold);
                    img.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallFontSize);
                    img.setTextColor(Color.parseColor("#A1242E76"));
                }
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
    private class MonthAdapter extends AbstractWheelTextAdapter {
        final String[] countries;

        /**
         * Constructor
         */
        MonthAdapter(Context context, String[] countries) {
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

            if (test.equals(countries[selected_month_value])) {
                img.setTypeface(fontBold);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeFontSize);
                img.setTextColor(Color.parseColor("#000023"));
            } else {
                img.setTypeface(fontLight);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallFontSize);
                img.setTextColor(Color.parseColor("#A1242E76"));
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


    /**
     * Adapter for daysArray
     */
    private class YearAdapter extends AbstractWheelTextAdapter {
        final int[] yearArray;

        /**
         * Constructor
         */
        YearAdapter(Context context, int[] yearArray) {
            super(context, R.layout.date_text_adapter, NO_RESOURCE);
            this.yearArray = yearArray;
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView img = (TextView) view.findViewById(R.id.country_name);

            String test = "" + yearArray[index];
            img.setText(test);

            if (test.equals("" + yearArray[selected_year_value])) {
                img.setTypeface(fontBold);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeFontSize);
                img.setTextColor(Color.parseColor("#000023"));
            } else {
                img.setTypeface(fontLight);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallFontSize);
                img.setTextColor(Color.parseColor("#A1242E76"));
            }

            return view;
        }

        @Override
        public int getItemsCount() {
            return yearArray.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return "" + yearArray[index];
        }
    }
}
