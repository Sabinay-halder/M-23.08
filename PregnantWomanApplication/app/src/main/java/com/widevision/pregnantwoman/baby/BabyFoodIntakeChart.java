package com.widevision.pregnantwoman.baby;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.Bean.BabyDBBean;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.OnWheelChangedListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.WheelView;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;

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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BabyFoodIntakeChart extends HideKeyFragment implements DatePickerDialog.OnDateSetListener {

    /*these variable for chart*/
    private XYMultipleSeriesRenderer multiRenderer;
    private XYMultipleSeriesDataset dataset;
    private XYSeries babyFoodSeries;
    private GraphicalView chart;
    private float chartTextSize;
    @Bind(R.id.chartLayout)
    LinearLayout mChartLayout;
    @Bind(R.id.dateTxt)
    TextView dateTxt;
    @Bind(R.id.viewBtn)
    Button viewBtn;
    @Bind(R.id.spinner)
    TextView spinner;
    @Bind(R.id.nameEdt)
    MaterialEditText nameEdt;
    @Bind(R.id.ageEdt)
    MaterialEditText ageEdt;

    String date = "", catagory = "";
    private ActiveAndroidDBHelper helper;
    List<BabyDBBean> babyInfoList = new ArrayList<>();
    private ArrayAdapter<String> quantityAdapter;
    private int selected_value = 0;
    private ShowcaseView showcaseView;
    private int tag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_intake_chart_activity, container, false);
        ButterKnife.bind(this, view);

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        chartTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);
        helper = ActiveAndroidDBHelper.getInstance();

        /*for spinner*/
        final ArrayList<String> foodlist = helper.foodIntakeCategList();
        quantityAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, foodlist);
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showcaseView != null) {
                    tag = 1;
                    showcaseView.hide();
                }
                setPicker(foodlist);
            }
        });

        nameEdt.setText(Constant.babyName);
        String dob = Constant.babyDOB;
        String[] d = dob.split("-");
        LocalDate startDate = new LocalDate(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
        Period period = new Period(startDate, new LocalDate(), PeriodType.yearMonthDay());
        int month = period.getMonths();
        if (month == 0) {
            ageEdt.setText("Birth");
        } else {
            ageEdt.setText("" + month + " Months");
        }

        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showcaseView != null) {
                    tag = 1;
                    showcaseView.hide();
                }
                setDatePicker(dateTxt);
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catagory = spinner.getText().toString().trim();
                date = dateTxt.getText().toString();
                if (date.length() == 0 || catagory.length() == 0) {
                    if (date.length() == 0) {
                        Constant.setAlert(getActivity(), "Please select date.");
                    } else {
                        Constant.setAlert(getActivity(), "Please select catagory.");
                    }
                } else {
                    babyInfoList = helper.babyfoodDetail(Constant.babyId, date, catagory);
                    if (babyInfoList != null && babyInfoList.size() != 0) {
                        openChartBoyNew(babyInfoList, babyInfoList.get(0).getUnit());
                    } else {
                        Constant.setAlert(getActivity(), "No iteam found.");
                    }
                }
            }
        });

        babyInfoList = helper.orderDate(Constant.babyId);
        if (babyInfoList != null && babyInfoList.size() != 0) {
            openChartBoyNew(babyInfoList, babyInfoList.get(0).getUnit());
            dateTxt.setText(babyInfoList.get(0).getDatefood());
            int size = foodlist.size();
            for (int j = 0; j < size; j++) {
                if (babyInfoList.get(0).getCategoryfood().equals(foodlist.get(j))) {
                    spinner.setText(foodlist.get(j));
                    catagory = foodlist.get(j);
                    break;
                }
            }
        } else {
            Constant.babysaveAlert(getActivity(), "No iteam found.");
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE_DATE, "No").equals("No")) {
            ViewTarget target = new ViewTarget(R.id.dateTxt, getActivity());
            showcaseView = new ShowcaseView.Builder(getActivity())
                    .withHoloShowcase()
                    .setTarget(target)
                    .setContentTitle("")
                    .setContentText(getActivity().getResources().getString(R.string.baby_chart_intake_date))
                    .setStyle(R.style.CustomShowcaseTheme)
                    .replaceEndButton(R.layout.ok_button)
                    .setShowcaseEventListener(listener)
                    .build();
            PreferenceConnector.writeString(getActivity(), PreferenceConnector.FOODINTAKE_DATE, "Yes");
        } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE_DATE, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE_TYPE, "No").equals("No")) {
            ViewTarget target = new ViewTarget(R.id.spinner, getActivity());
            showcaseView = new ShowcaseView.Builder(getActivity())
                    .withHoloShowcase()
                    .setTarget(target)
                    .setContentTitle("")
                    .setContentText(getActivity().getResources().getString(R.string.baby_chart_intake_category))
                    .setStyle(R.style.CustomShowcaseTheme)
                    .replaceEndButton(R.layout.ok_button)
                    .setShowcaseEventListener(listener)
                    .build();
            PreferenceConnector.writeString(getActivity(), PreferenceConnector.FOODINTAKE_TYPE, "Yes");
        }

    }

    private OnShowcaseEventListener listener = new OnShowcaseEventListener() {
        @Override
        public void onShowcaseViewHide(ShowcaseView showcaseView) {
            if (tag == 0) {
                if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE_DATE, "No").equals("No")) {
                    ViewTarget target = new ViewTarget(R.id.dateTxt, getActivity());
                    showcaseView = new ShowcaseView.Builder(getActivity())
                            .withHoloShowcase()
                            .setTarget(target)
                            .setContentTitle("")
                            .setContentText(getActivity().getResources().getString(R.string.baby_chart_intake_date))
                            .setStyle(R.style.CustomShowcaseTheme)
                            .replaceEndButton(R.layout.ok_button)
                            .setShowcaseEventListener(listener)
                            .build();
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.FOODINTAKE_DATE, "Yes");
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE_DATE, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE_TYPE, "No").equals("No")) {
                    ViewTarget target = new ViewTarget(R.id.spinner, getActivity());
                    showcaseView = new ShowcaseView.Builder(getActivity())
                            .withHoloShowcase()
                            .setTarget(target)
                            .setContentTitle("")
                            .setContentText(getActivity().getResources().getString(R.string.baby_chart_intake_category))
                            .setStyle(R.style.CustomShowcaseTheme)
                            .replaceEndButton(R.layout.ok_button)
                            .setShowcaseEventListener(listener)
                            .build();
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.FOODINTAKE_TYPE, "Yes");
                }
            } else {
                tag = 0;
            }
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

    void setDatePicker(final TextView textView) {
        String dateToSet = "";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        int year = 0, month = 0, day = 0;
        Calendar now = Calendar.getInstance();
        if (textView.getText().toString().trim().equals("")) {
            dateToSet = Constant.getDate();
        } else {
            DateTime selectedDate = formatter.parseDateTime(textView.getText().toString().trim());
            year = selectedDate.getYear();
            month = selectedDate.getMonthOfYear();
            day = selectedDate.getDayOfMonth();
            dateToSet = year + "-" + month + "-" + day;
        }
        DatePickerViewAndroid datePicker = new DatePickerViewAndroid(getActivity(), dateToSet, "yyyy-MM-dd", new MyListener() {
            @Override
            public void onSet(String date1) {
                String a[] = date1.split("-");
                String newDate = a[2] + "-" + a[1] + "-" + a[0];
                DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePatternFullName);
                LocalDate date2 = formatter.parseLocalDate(newDate);

                String month = "" + date2.getMonthOfYear();
                String day = "" + date2.getDayOfMonth();
                String year = "" + date2.getYear();
                String da = String.valueOf(day);
                if (month.length() < 2) {
                    month = "0" + month;
                }
                if (da.length() < 2) {
                    da = "0" + da;
                }
                String datesave = year + "-" + month + "-" + da;
                dateTxt.setText(datesave);
                date = datesave;
            }
        });
        datePicker.setPicker();
        datePicker.show();
    }


    @SuppressLint("ResourceAsColor")
    private void openChartBoyNew(List<BabyDBBean> list, String yTitle) {
// Creating an XYSeries for before meal & after meal
        babyFoodSeries = new XYSeries(" Food Intake ");  // Adding data to
        /*babySleepSeries = new XYSeries(" Actual ");*/


        // Creating a dataset to hold each series
        dataset = new XYMultipleSeriesDataset(); // Adding Income Series to the
        // dataset

        // Adding beforeMeal Series to dataset
        dataset.addSeries(babyFoodSeries);
        // Creating XYSeriesRenderer to customize beforeMealSeries
        XYSeriesRenderer beforeMealRenderer = new XYSeriesRenderer();
        beforeMealRenderer.setColor(Color.RED);
        beforeMealRenderer.setPointStyle(PointStyle.CIRCLE);
        beforeMealRenderer.setFillPoints(true);
        beforeMealRenderer.setLineWidth(2f);
        beforeMealRenderer.setDisplayChartValues(true);
        beforeMealRenderer.setDisplayChartValuesDistance(1);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart

        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(20);
        multiRenderer.setLegendTextSize(chartTextSize);
        multiRenderer.setXTitle("Times");
        multiRenderer.setYTitle(" " + yTitle + " ");
        multiRenderer.setAxisTitleTextSize(chartTextSize);
        multiRenderer.setChartTitleTextSize(chartTextSize);
        multiRenderer.setShowGrid(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setXAxisMax(5);
        multiRenderer.setYAxisMax(24);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYLabelsPadding(8);
        multiRenderer.setMargins(new int[]{15, 30, 5, 15});

        for (int i = 0; i < list.size(); i++) {
            BabyDBBean item = list.get(i);
            babyFoodSeries.add(i, Float.parseFloat(item.getQuantityfood()));
            multiRenderer.addXTextLabel(i, item.getIntakeTime());
        }

        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        // for change background multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);

        // for zoom buttons
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setPanEnabled(true, false);
        multiRenderer.setPanLimits(new double[]{0, list.size(), 0, 30});
        multiRenderer.setShowGrid(true);
        multiRenderer.addSeriesRenderer(beforeMealRenderer);
        /*multiRenderer.addSeriesRenderer(afterMealRenderer);*/
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        chart = ChartFactory.getLineChartView(getActivity(), dataset,
                multiRenderer);
        mChartLayout.addView(chart);
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        String mont = String.valueOf(month + 1);
        String da = String.valueOf(day);
        if (mont.length() < 2) {
            mont = "0" + mont;
        }
        if (da.length() < 2) {
            da = "0" + da;
        }
        String datesave = year + "-" + mont + "-" + da;
        dateTxt.setText(datesave);
        date = datesave;
    }


    private void setPicker(final ArrayList<String> strArr) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogAnimation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picker_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        final WheelView country = (WheelView) dialog.findViewById(R.id.cityWheel);
        country.setVisibleItems(3);
        final CountryAdapter adapter = new CountryAdapter(getActivity(), strArr);
        country.setViewAdapter(adapter);

        LinearLayout titleLayout = (LinearLayout) dialog.findViewById(R.id.pickerTitleLayout);
        ImageView doneButton = (ImageView) dialog.findViewById(R.id.pickerDone);
        ImageView closeButton = (ImageView) dialog.findViewById(R.id.pickerClose);
        final TextView textView = (TextView) dialog.findViewById(R.id.selectedTxt);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.heading));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                String category = strArr.get(selected_value);
                spinner.setText(category);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selected_value = newValue;
                textView.setText(strArr.get(selected_value));
                textView.setTextColor(getResources().getColor(R.color.text_color));
                adapter.notifyDataChangedEvent();
            }
        });

        country.setCurrentItem(selected_value);
        textView.setText(strArr.get(selected_value));
        textView.setTextColor(getResources().getColor(R.color.text_color));

        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    /**
     * Adapter for picker
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> countries;

        /**
         * Constructor
         */
        protected CountryAdapter(Context context, ArrayList<String> countries) {
            super(context, R.layout.row_wheel_layout, NO_RESOURCE);
            this.countries = countries;
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView img = (TextView) view.findViewById(R.id.country_name);
            if (selected_value == index) {
                img.setTextColor(Color.parseColor("#000023"));
            } else {
                img.setTextColor(Color.parseColor("#A1242E76"));
            }
            img.setText(countries.get(index));
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries.get(index);
        }
    }


}
