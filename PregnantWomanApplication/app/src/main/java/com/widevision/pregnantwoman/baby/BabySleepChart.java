package com.widevision.pregnantwoman.baby;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.Bean.BabySleepBean;
import com.widevision.pregnantwoman.Bean.BabySleepRequiredBean;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.BabySleepRecordTable;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.Utils;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mercury-two on 28/8/15.
 */
public class BabySleepChart extends HideKeyFragment implements DatePickerDialog.OnDateSetListener {

    /*these variable for chart*/
    private XYMultipleSeriesRenderer multiRenderer;
    private XYMultipleSeriesDataset dataset;
    private XYSeries babySleepRequiredSeries, babySleepSeries;
    private GraphicalView chart;
    private float chartTextSize;
    @Bind(R.id.chartLayout)
    LinearLayout mChartLayout;
    @Bind(R.id.title)
    TextView mTitleTxt;
    @Bind(R.id.startDateEdt)
    EditText mStartDateEdt;
    @Bind(R.id.endDateEdt)
    EditText mEndDateEdt;
    @Bind(R.id.viewBtn)
    Button mViewBtn;
    @Bind(R.id.ageEdt)
    EditText mAgeEdt;
    @Bind(R.id.nameEdt)
    EditText mNameEdt;
    @Bind(R.id.layout)
    LinearLayout layout;

    private int dateTag = 0;
    private String mStartDateStr = "", mEndDateStr = "", standardValue = "";
    LocalDate startDate;
    private List<BabySleepRecordTable> BabySleepRecordTable = new ArrayList<>();
    private ShowcaseView showcaseView;

    private String[] months = {"Birth", "3M", "6M", "9M", "12M", "15M", "18M", "21M", "24M", "27M", "30M", "33M", "36M", "4yr", " 5yr"};
    private int[] months_ideal = {0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 48, 60};
    private ActiveAndroidDBHelper helper;
    private ArrayList<BabySleepRequiredBean> requiredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_sleep_char_activity, container, false);
        ButterKnife.bind(this, view);
        mTitleTxt.setText("Baby Sleep Chart");
        helper = ActiveAndroidDBHelper.getInstance();
        requiredList = helper.babySleepRequiredData();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        chartTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, metrics);

        LocalDate now = new LocalDate();
        calculateAge(now);
        mNameEdt.setText(Constant.babyName);

        List<BabySleepRecordTable> list = helper.babySleepDetail(Constant.babyId);
        /*get last 10 record from list*/
        if (list != null) {
            int size = list.size();
            if (size > 10) {
                Collections.reverse(list);
                for (int i = 9; i >= 0; i--) {
                    BabySleepRecordTable.add(list.get(i));
                }
                Collections.reverse(BabySleepRecordTable);
            } else {
                BabySleepRecordTable = list;
            }
            openChartBoyNew(BabySleepRecordTable);
        }


        mViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showcaseView != null) {
                    showcaseView.hide();
                }
                List<BabySleepBean> list = helper.babySleepRecordBetweenDates(mStartDateStr, mEndDateStr);
                if (list != null && list.size() != 0) {
                    String d[] = mEndDateStr.split("-");
                    LocalDate endDate = new LocalDate(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
                    calculateAge(endDate);
                    openChartBoy(list);
                } else {
                    Constant.setAlert(getActivity(), "No value for selected dates.");
                }
            }
        });

        mStartDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showcaseView != null) {
                    showcaseView.hide();
                }
                showDatePicker(mStartDateEdt);
                dateTag = 0;
            }
        });
        mStartDateEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (showcaseView != null) {
                        showcaseView.hide();
                    }
                    showDatePicker(mStartDateEdt);
                    dateTag = 0;
                }
            }
        });


        mEndDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showcaseView != null) {
                    showcaseView.hide();
                }
                showDatePicker(mEndDateEdt);
                dateTag = 1;
            }
        });
        mEndDateEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (showcaseView != null) {
                        showcaseView.hide();
                    }
                    showDatePicker(mEndDateEdt);
                    dateTag = 1;
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.SLEEPCHART, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.SLEEPCHART, R.id.layout, R.layout.ok_button, "", getActivity().getResources().getString(R.string.baby_sleep_chart), R.style.CustomShowcaseTheme, false, listener);
        }
    }

    private OnShowcaseEventListener listener = new OnShowcaseEventListener() {
        @Override
        public void onShowcaseViewHide(ShowcaseView showcaseView) {

        }

        @Override
        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

        }

        @Override
        public void onShowcaseViewShow(ShowcaseView showcaseView) {

        }

        @Override
        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

        }
    };

    void showDatePicker(final TextView textView) {

        int year = 0, month = 0, day = 0;
        Calendar now = Calendar.getInstance();
        String dateToSet = "";

        if (textView.getText().toString().trim().equals("")) {
            dateToSet = Constant.getDate();
        } else {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePattern);
            DateTime selectedDate = formatter.parseDateTime(textView.getText().toString().trim());
            year = selectedDate.getYear();
            month = selectedDate.getMonthOfYear();
            day = selectedDate.getDayOfMonth();
            dateToSet = year + "-" + month + "-" + day;
        }

        DatePickerViewAndroid datePicker = new DatePickerViewAndroid(getActivity(), dateToSet, "yyyy-MM-dd", new MyListener() {
            @Override
            public void onSet(String date) {
                try {
                    String a[] = date.split("-");
                    String newDate1 = a[2] + "-" + a[1] + "-" + a[0];
                    DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePatternFullName);
                    DateTime dateSelected = formatter.parseDateTime(newDate1);
                    String monthOfYear = "" + dateSelected.getMonthOfYear();
                    String dayOfMonth = "" + dateSelected.getDayOfMonth();
                    String year = "" + dateSelected.getYear();

                    String mont = String.valueOf(monthOfYear);
                    String da = String.valueOf(dayOfMonth);
                    if (mont.length() < 2) {
                        mont = "0" + mont;
                    }
                    if (da.length() < 2) {
                        da = "0" + da;
                    }
                    String datesave = year + "-" + mont + "-" + da;
                    if (dateTag == 0) {
                        mStartDateEdt.setText(datesave);
                        mStartDateStr = datesave;
                        LocalDate startDate = new LocalDate(Integer.parseInt(year), Integer.parseInt(mont), Integer.parseInt(da));
                        LocalDate newDate = startDate.plusMonths(1);
                        String m = String.valueOf(newDate.getMonthOfYear());
                        String d = String.valueOf(newDate.getDayOfMonth());
                        if (m.length() < 2) {
                            m = "0" + m;
                        }
                        if (d.length() < 2) {
                            d = "0" + d;
                        }
                        String date1 = newDate.getYear() + "-" + m + "-" + d;
                        mEndDateEdt.setText(date1);
                        mEndDateStr = date1;
                    } else {
                        mEndDateEdt.setText(datesave);
                        mEndDateStr = datesave;
                        LocalDate startDate = new LocalDate(Integer.parseInt(year), Integer.parseInt(mont), Integer.parseInt(da));
                        LocalDate newDate = startDate.minusMonths(1);
                        String m = String.valueOf(newDate.getMonthOfYear());
                        String d = String.valueOf(newDate.getDayOfMonth());
                        if (m.length() < 2) {
                            m = "0" + m;
                        }
                        if (d.length() < 2) {
                            d = "0" + d;
                        }
                        String date2 = newDate.getYear() + "-" + m + "-" + d;
                        mStartDateEdt.setText(date2);
                        mStartDateStr = date2;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        datePicker.setPicker();
        datePicker.show();
    }

    private void calculateAge(LocalDate toDate) {
        //function used to calculate age and standard value of sleep.
        String dob = Constant.babyDOB;
        String[] d = dob.split("-");
        startDate = new LocalDate(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
        Period period = new Period(startDate, toDate, PeriodType.yearMonthDay());
        int months = period.getMonths();
        if (months == 0) {
            mAgeEdt.setText("Birth");
        } else {
            mAgeEdt.setText("" + months + "  Months");
        }


        int length = months_ideal.length;
        for (int i = 0; i < length; i++) {
            if (!(months_ideal[i] < months)) {
                int m = i;
                standardValue = requiredList.get(i).getHour();
                break;
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private void openChartBoyNew(List<BabySleepRecordTable> list) {
        babySleepRequiredSeries = new XYSeries(" Your  ");
        babySleepSeries = new XYSeries(" Standard ");

        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(babySleepRequiredSeries);
        XYSeriesRenderer sleepRenderer = new XYSeriesRenderer();
        sleepRenderer.setColor(Color.BLUE);
        sleepRenderer.setPointStyle(PointStyle.CIRCLE);
        sleepRenderer.setFillPoints(true);
        sleepRenderer.setLineWidth(2f);
        sleepRenderer.setDisplayChartValues(true);
        sleepRenderer.setDisplayChartValuesDistance(1);

        dataset.addSeries(babySleepSeries);
        XYSeriesRenderer sleepRendererStandard = new XYSeriesRenderer();
        sleepRendererStandard.setColor(Color.GREEN);
        sleepRendererStandard.setPointStyle(PointStyle.CIRCLE);
        sleepRendererStandard.setFillPoints(true);
        sleepRendererStandard.setLineWidth(2f);
        sleepRendererStandard.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(10);//
        multiRenderer.setLegendTextSize(chartTextSize);
        multiRenderer.setXTitle("Dates");
        multiRenderer.setYTitle(" Hour ");
        multiRenderer.setAxisTitleTextSize(chartTextSize);
        multiRenderer.setChartTitleTextSize(chartTextSize);
        multiRenderer.setShowGrid(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setXAxisMax(11);
        multiRenderer.setYAxisMax(24);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYLabelsPadding(8);
        multiRenderer.setMargins(new int[]{15, 30, 5, 15});

        for (int i = 0; i < list.size(); i++) {
            BabySleepRecordTable item = list.get(i);
            babySleepRequiredSeries.add(i, item.Hours);
            String date = item.sleepdatebaby;
            String a[] = date.split("-");
            multiRenderer.addXTextLabel(i, a[2]);
            babySleepSeries.add(i, Float.parseFloat(standardValue));
        }

        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);

        // for zoom buttons
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setPanEnabled(true, false);
        multiRenderer.setShowGrid(true);
        multiRenderer.addSeriesRenderer(sleepRenderer);
        multiRenderer.addSeriesRenderer(sleepRendererStandard);
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        chart = ChartFactory.getLineChartView(getActivity(), dataset,
                multiRenderer);
        mChartLayout.addView(chart);
    }


    @SuppressLint("ResourceAsColor")
    private void openChartBoy(List<BabySleepBean> list) {
        babySleepRequiredSeries = new XYSeries(" Your  ");
        babySleepSeries = new XYSeries(" Standard ");

        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(babySleepRequiredSeries);
        XYSeriesRenderer sleepRenderer = new XYSeriesRenderer();
        sleepRenderer.setColor(Color.BLUE);
        sleepRenderer.setPointStyle(PointStyle.CIRCLE);
        sleepRenderer.setFillPoints(true);
        sleepRenderer.setLineWidth(4f);
        sleepRenderer.setDisplayChartValues(true);

        dataset.addSeries(babySleepSeries);
        XYSeriesRenderer sleepRendererStandard = new XYSeriesRenderer();
        sleepRendererStandard.setColor(Color.GREEN);
        sleepRendererStandard.setPointStyle(PointStyle.CIRCLE);
        sleepRendererStandard.setFillPoints(true);
        sleepRendererStandard.setLineWidth(2f);
        sleepRendererStandard.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(10);//
        multiRenderer.setLegendTextSize(chartTextSize);
        multiRenderer.setXTitle("Dates");
        multiRenderer.setYTitle(" Hour ");
        multiRenderer.setAxisTitleTextSize(chartTextSize);
        multiRenderer.setChartTitleTextSize(chartTextSize);
        multiRenderer.setShowGrid(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setXAxisMax(11);
        multiRenderer.setYAxisMax(24);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYLabelsPadding(8);
        multiRenderer.setMargins(new int[]{15, 30, 5, 15});

        for (int i = 0; i < list.size(); i++) {
            BabySleepBean item = list.get(i);
            babySleepRequiredSeries.add(i, item.Hours);
            String date = item.sleepdatebaby;
            String a[] = date.split("-");
            multiRenderer.addXTextLabel(i, a[2]);
            babySleepSeries.add(i, Float.parseFloat(standardValue));
        }

        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);


        // for zoom buttons
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setPanEnabled(true, false);
        multiRenderer.setShowGrid(true);
        multiRenderer.addSeriesRenderer(sleepRenderer);
        multiRenderer.addSeriesRenderer(sleepRendererStandard);
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        chart = ChartFactory.getLineChartView(getActivity(), dataset,
                multiRenderer);
        mChartLayout.addView(chart);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mont = String.valueOf(monthOfYear + 1);
        String da = String.valueOf(dayOfMonth);
        if (mont.length() < 2) {
            mont = "0" + mont;
        }
        if (da.length() < 2) {
            da = "0" + da;
        }
        String datesave = year + "-" + mont + "-" + da;
        if (dateTag == 0) {
            mStartDateEdt.setText(datesave);
            mStartDateStr = datesave;
            LocalDate startDate = new LocalDate(year, Integer.parseInt(mont), Integer.parseInt(da));
            LocalDate newDate = startDate.plusMonths(1);
            String m = String.valueOf(newDate.getMonthOfYear());
            String d = String.valueOf(newDate.getDayOfMonth());
            if (m.length() < 2) {
                m = "0" + m;
            }
            if (d.length() < 2) {
                d = "0" + d;
            }
            String date = newDate.getYear() + "-" + m + "-" + d;
            mEndDateEdt.setText(date);
            mEndDateStr = date;
        } else {
            mEndDateEdt.setText(datesave);
            mEndDateStr = datesave;
            LocalDate startDate = new LocalDate(year, Integer.parseInt(mont), Integer.parseInt(da));
            LocalDate newDate = startDate.minusMonths(1);
            String m = String.valueOf(newDate.getMonthOfYear());
            String d = String.valueOf(newDate.getDayOfMonth());
            if (m.length() < 2) {
                m = "0" + m;
            }
            if (d.length() < 2) {
                d = "0" + d;
            }
            String date = newDate.getYear() + "-" + m + "-" + d;
            mStartDateEdt.setText(date);
            mStartDateStr = date;
        }
    }
}
