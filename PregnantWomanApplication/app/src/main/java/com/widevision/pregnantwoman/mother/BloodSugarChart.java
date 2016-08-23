package com.widevision.pregnantwoman.mother;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.widevision.pregnantwoman.Bean.MotherHealthBean;
import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.OnWheelChangedListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.WheelView;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.Utils;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.RangeBarChart;
import org.achartengine.chart.ScatterChart;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BloodSugarChart extends HideKeyFragment {

    /*these variable for chart*/
    private XYMultipleSeriesRenderer multiRenderer;
    private XYMultipleSeriesDataset dataset;
    private XYSeries beforeMealSeries, afterMealSeries;
    private GraphicalView chart;
    private float chartTextSize, textSize;

    @Bind(R.id.chartLayout)
    LinearLayout mChartLayout;
    @Bind(R.id.date_spinner)
    TextView spinner;
    @Bind(R.id.from_toEdt)
    EditText mFromToEdt;

    @Bind(R.id.name)
    TextView mNameTxt;

    private Typeface fontLight, fontBold;
    private int largeFontSize = 22, smallFontSize = 18;
    private View view;
    private ActiveAndroidDBHelper helper;
    private List<MotherHealthBean> mHelthParameterList = new ArrayList<>();
    private int selected_value = 0;
    private ShowcaseView showcaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.blood_sugar_char_activity, container, false);
        ButterKnife.bind(this, view);
        fontLight = Typeface.createFromAsset(getActivity().getAssets(), "Oregon LDO Medium.ttf");
        fontBold = Typeface.createFromAsset(getActivity().getAssets(), "Oregon LDO Black.ttf");
        helper = ActiveAndroidDBHelper.getInstance();
        String name = PreferenceConnector.readString(getActivity(), PreferenceConnector.MOTHER_NAME, "");
        mNameTxt.setText(name);

        /*get all the record from health parameter table*/
        mHelthParameterList.addAll(helper.orderByDate());
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        chartTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, metrics);
        if (mHelthParameterList == null || mHelthParameterList.size() == 0) {
            Constant.setAlert(getActivity(), "Enter health parameter first.");
            MainFragmentActivity.fragmentManager.popBackStack();
        } else {

            openChart(mHelthParameterList);
            mFromToEdt.setText("Showing chart from " + mHelthParameterList.get(0)._Date + " to " + mHelthParameterList.get(mHelthParameterList.size() - 1)._Date);

        /*for spinner*/
            final ArrayList<String> list = new ArrayList<>();
            list.add("ALL");
            for (MotherHealthBean item : mHelthParameterList) {
                list.add(item._Date);
            }
            spinner.setText("ALL");
            spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showcaseView != null) {
                        showcaseView.hide();
                    }
                    setPicker(list);
                }
            });
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.BLOOD_SUGAR_CHART, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.BLOOD_SUGAR_CHART, R.id.date_spinner, R.layout.ok_button, "Select Date", "click and select particular date of your entry to see your blood sugar on that day.", R.style.CustomShowcaseTheme, false, listener);
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


    @SuppressLint("ResourceAsColor")
    private void openChart(List<MotherHealthBean> list) {
// Creating an XYSeries for before meal & after meal
        beforeMealSeries = new XYSeries("Before Meal");  // Adding data to
        afterMealSeries = new XYSeries("After");

        // Creating a dataset to hold each series
        dataset = new XYMultipleSeriesDataset(); // Adding Income Series to the
        // dataset

        // Adding beforeMeal Series to dataset
        dataset.addSeries(beforeMealSeries);
        // Creating XYSeriesRenderer to customize beforeMealSeries
        XYSeriesRenderer beforeMealRenderer = new XYSeriesRenderer();
        beforeMealRenderer.setColor(Color.RED);
        beforeMealRenderer.setPointStyle(PointStyle.CIRCLE);
        beforeMealRenderer.setFillPoints(true);
        beforeMealRenderer.setLineWidth(2f);
        beforeMealRenderer.setDisplayChartValues(true);

        beforeMealRenderer.setDisplayChartValuesDistance(1);

        dataset.addSeries(afterMealSeries);
        // Creating XYSeriesRenderer to customize afterMealSeries
        XYSeriesRenderer afterMealRenderer = new XYSeriesRenderer();
        afterMealRenderer.setColor(Color.parseColor("#3f51b5"));
        afterMealRenderer.setPointStyle(PointStyle.CIRCLE);
        afterMealRenderer.setFillPoints(true);
        afterMealRenderer.setLineWidth(2);
        afterMealRenderer.setDisplayChartValues(true);
        afterMealRenderer.setDisplayChartValuesDistance(1);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart

        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(14);//
        multiRenderer.setLegendTextSize(chartTextSize);
        multiRenderer.setXTitle("Dates");
        multiRenderer.setYTitle("mmHg/bpm");
        multiRenderer.setAxisTitleTextSize(chartTextSize);
        multiRenderer.setChartTitleTextSize(chartTextSize);
        multiRenderer.setShowGrid(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setXAxisMax(5);
        multiRenderer.setYAxisMax(170);
        multiRenderer.setYAxisMin(40);
        multiRenderer.setYLabelsPadding(8);
        multiRenderer.setMargins(new int[]{15, 30, 5, 15});


        for (int i = 0; i < list.size(); i++) {
            beforeMealSeries.add(i, list.get(i)._BloodSugarBeforeMeal);
            afterMealSeries.add(i, list.get(i)._BloodSugarAfterMeal);
            multiRenderer.addXTextLabel(i, list.get(i)._Date);
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
        multiRenderer.setPanLimits(new double[]{0, 39, 0, 160});
        multiRenderer.addSeriesRenderer(beforeMealRenderer);
        multiRenderer.addSeriesRenderer(afterMealRenderer);
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        chart = ChartFactory.getLineChartView(getActivity(), dataset,
                multiRenderer);
        mChartLayout.addView(chart);
    }

    private void openChart(MotherHealthBean item) {
        String[] mMonth = new String[]{
                "", "Fasting & Before Meal", "After Meal"
        };
        int[] x = {0, 1, 2};
        float[] yourPoint = {0, item._BloodSugarBeforeMeal, item._BloodSugarAfterMeal};

        // Creating an  XYSeries for Income
        XYSeries incomeSeries = new XYSeries("Your BloodSugar");
        // Creating an  XYSeries for Expense
        RangeCategorySeries expenseSeries = new RangeCategorySeries("BloodSugar Range");
        if (item._PreExistingDiabetes) {
            expenseSeries.add(60, 100);
            expenseSeries.add(100, 129);
        } else {
            expenseSeries.add(0, 96);
            expenseSeries.add(0, 120);
        }

        // Adding data to Income and Expense Series
        for (int i = 0; i < x.length; i++) {
            incomeSeries.add(x[i], yourPoint[i]);
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Expense Series to dataset
        dataset.addSeries(expenseSeries.toXYSeries());
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.BLUE);
        expenseRenderer.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);


        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);
        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.RED);
        incomeRenderer.setPointStyle(PointStyle.SQUARE);
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(2);
        incomeRenderer.setPointStrokeWidth(10f);
        incomeRenderer.setDisplayChartValues(true);
        incomeRenderer.setChartValuesTextSize(textSize);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(7);
        multiRenderer.setYAxisMax(140);
        multiRenderer.setYAxisMin(00);
        multiRenderer.setPanEnabled(true, true);
        /*multiRenderer.setXTitle("Mg/dl");*/
        multiRenderer.setYTitle("Mg/dl");
        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setXAxisMax(4);
        multiRenderer.setBarSpacing(4);
        for (int i = 0; i < x.length; i++) {
            multiRenderer.addXTextLabel(i, mMonth[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(expenseRenderer);
        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer.setClickEnabled(true);
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setYLabelsPadding(8);
        multiRenderer.setMargins(new int[]{15, 30, 5, 15});
        // Specifying chart types to be drawn in the graph
        // Number of data series and number of types should be same
        // Order of data series and chart type will be same
        String[] types = new String[]{RangeBarChart.TYPE, ScatterChart.TYPE};
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        // Creating a combined chart with the chart types specified in types array
        chart = (GraphicalView) ChartFactory.getCombinedXYChartView(getActivity(), dataset, multiRenderer, types);


        // Adding the Combined Chart to the LinearLayout
        mChartLayout.addView(chart);
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
                String date = strArr.get(selected_value);
                spinner.setText("" + date);
                if (date.trim().equals("ALL")) {
                    openChart(mHelthParameterList);
                    mFromToEdt.setText("showing chart from " + mHelthParameterList.get(0)._Date + " to " + mHelthParameterList.get(mHelthParameterList.size() - 1)._Date);
                } else {
                    for (int j = 0; j < mHelthParameterList.size(); j++) {
                        if (mHelthParameterList.get(j)._Date.trim().equals(date.trim())) {
                            MotherHealthBean item = mHelthParameterList.get(j);
                            openChart(item);
                            mFromToEdt.setText("showing chart for " + item._Date);
                            break;
                        }
                    }
                }

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
                textView.setTextColor(Color.parseColor("#ffffff"));
                adapter.notifyDataChangedEvent();
            }
        });

        country.setCurrentItem(selected_value);
        textView.setText(strArr.get(selected_value));
        textView.setTextColor(Color.parseColor("#ffffff"));

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
                img.setTypeface(fontBold);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeFontSize);
                img.setTextColor(Color.parseColor("#000023"));
            } else {
                img.setTypeface(fontLight);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallFontSize);
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
