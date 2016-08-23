package com.widevision.pregnantwoman.baby;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.widevision.pregnantwoman.Bean.BabyHeightWeightBean;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.BabyHeiWeiTable;
import com.widevision.pregnantwoman.database.BabyInfoTable;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.ScatterChart;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BabyHeightWeightChart extends HideKeyFragment {

    /*these variable for chart*/
    private XYMultipleSeriesRenderer multiRenderer;
    private XYMultipleSeriesDataset dataset;
    private XYSeries boy_heightSeries, boy_weightSeries, actualHeightSeries, actualWeightSeries;
    private GraphicalView chart;
    private float chartTextSize;

    @Bind(R.id.chartLayout)
    LinearLayout mChartLayout;
    @Bind(R.id.nameEdt)
    MaterialEditText nameEdt;
    @Bind(R.id.ageEdt)
    MaterialEditText ageEdt;

    private View view;
    private ActiveAndroidDBHelper helper;
    private ArrayList<BabyHeightWeightBean> babbyHeightWeightList = new ArrayList<>();

    private String[] testX = {"6", "10", "14", "18", "22", "26", "30", "34", "38", "42", "46", "50"};
    private String[] months = {"Birth", "3M", "6M", "9M", "12M", "15M", "18M", "21M", "24M", "27M", "30M", "33M", "36M", "4yr", " 5yr"};
    private int[] months_ideal = {0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 48, 60};
    private int month_position = 0;
    private int mBabyHeight = 0;
    private float mBabyWeight = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.baby_height_weight_chart_activity, container, false);
        ButterKnife.bind(this, view);
        helper = ActiveAndroidDBHelper.getInstance();
        /*get all the record from health parameter table*/
        babbyHeightWeightList = helper.getBabyHeightWeightData();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        chartTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);

        BabyInfoTable babyInfo = helper.viewBabyById(Constant.babyId);
        List<BabyHeiWeiTable> list = helper.babyHeightInfo(Constant.babyId);
        if (list.size() != 0) {
            String dob = babyInfo.dobbaby;
            String[] d = dob.split("-");
            String l = list.get(list.size() - 1).dateofmeasure;
            mBabyHeight = list.get(list.size() - 1).heightbaby;
            mBabyWeight = list.get(list.size() - 1).weightbaby;
            String[] last = l.split("-");
            LocalDate startDate = new LocalDate(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));          //Birth date
            LocalDate endDate = new LocalDate(Integer.parseInt(last[0]), Integer.parseInt(last[1]), Integer.parseInt(last[2]));                    //Today's date
            Period period = new Period(startDate, endDate, PeriodType.yearMonthDay());
            int months = period.getMonths();

            for (int i = 0; i < months_ideal.length; i++) {
                if (months_ideal[i] >= months) {
                    if (i != 0) {
                        month_position = months_ideal[i - 1];
                    } else {
                        month_position = months_ideal[0];
                    }
                    break;
                }
            }
            nameEdt.setText("" + babyInfo.name);
           /* dobTxt.setText(":- " + babyInfo.dobbaby);
            genderTxt.setText(":- " + babyInfo.genderbaby);*/
            if (months == 0) {
                ageEdt.setText(" Birth");
            } else {
                ageEdt.setText(" " + months + " Months");
            }

        } else {
            getFragmentManager().popBackStack();
            Constant.setAlert(getActivity(), "Insert height weight first.");
        }


        if (babbyHeightWeightList != null && babbyHeightWeightList.size() != 0) {
            if (Constant.gender.trim().equals("Boy")) {
                openChartBoy(babbyHeightWeightList);
            } else if (Constant.gender.trim().equals("Girl")) {
                openChartGirl(babbyHeightWeightList);
            }
        }








        return view;
    }


    @SuppressLint("ResourceAsColor")
    private void openChartBoy(ArrayList<BabyHeightWeightBean> list) {
// Creating an XYSeries for before meal & after meal
        boy_heightSeries = new XYSeries("Required Height");  // Adding data to
        boy_weightSeries = new XYSeries("Required Weight");

        actualWeightSeries = new XYSeries("");
        actualHeightSeries = new XYSeries("");

        RangeCategorySeries heightSeries = new RangeCategorySeries("Baby Height");
        RangeCategorySeries weightSeries = new RangeCategorySeries("Baby Weight");
        for (int i = 0; i < month_position; i++) {
            heightSeries.add(-3);
            weightSeries.add(-3);
        }
        heightSeries.add(mBabyHeight, mBabyHeight);
        weightSeries.add(mBabyWeight, mBabyWeight);

        // Creating a dataset to hold each series
        dataset = new XYMultipleSeriesDataset(); // Adding Income Series to the
        // dataset

        dataset.addSeries(heightSeries.toXYSeries());
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer heightRenderer = new XYSeriesRenderer();
        heightRenderer.setColor(Color.BLUE);
        heightRenderer.setPointStyle(PointStyle.CIRCLE);
        heightRenderer.setFillPoints(true);
        heightRenderer.setLineWidth(2);
        heightRenderer.setDisplayChartValues(true);
        heightRenderer.setDisplayChartValuesDistance(1);

        dataset.addSeries(weightSeries.toXYSeries());
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer weightRenderer = new XYSeriesRenderer();
        weightRenderer.setColor(Color.MAGENTA);
        weightRenderer.setPointStyle(PointStyle.CIRCLE);
        weightRenderer.setFillPoints(true);
        weightRenderer.setLineWidth(2);
        weightRenderer.setDisplayChartValues(true);
        weightRenderer.setDisplayChartValuesDistance(1);

        // Adding beforeMeal Series to dataset
        dataset.addSeries(boy_heightSeries);
        // Creating XYSeriesRenderer to customize beforeMealSeries
        XYSeriesRenderer beforeMealRenderer = new XYSeriesRenderer();
        beforeMealRenderer.setColor(Color.RED);
        beforeMealRenderer.setPointStyle(PointStyle.CIRCLE);
        beforeMealRenderer.setFillPoints(true);
        beforeMealRenderer.setLineWidth(2f);
        beforeMealRenderer.setDisplayChartValues(false);

        dataset.addSeries(boy_weightSeries);
        // Creating XYSeriesRenderer to customize afterMealSeries
        XYSeriesRenderer afterMealRenderer = new XYSeriesRenderer();
        afterMealRenderer.setColor(Color.GREEN);
        afterMealRenderer.setPointStyle(PointStyle.CIRCLE);
        afterMealRenderer.setFillPoints(true);
        afterMealRenderer.setLineWidth(2f);
        afterMealRenderer.setDisplayChartValues(false);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(10);//
        multiRenderer.setLegendTextSize(chartTextSize);
        multiRenderer.setXTitle("Months");
        multiRenderer.setYTitle(" Value ");
        multiRenderer.setAxisTitleTextSize(chartTextSize);
        multiRenderer.setChartTitleTextSize(chartTextSize);
        multiRenderer.setShowGrid(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setXAxisMax(5);
        multiRenderer.setYAxisMax(60);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYLabelsPadding(8);
        multiRenderer.setMargins(new int[]{15, 30, 5, 15});

        for (int i = 0; i < list.size(); i++) {
            BabyHeightWeightBean item = list.get(i);
            boy_heightSeries.add(i, item.getBoys_Height());
            boy_weightSeries.add(i, item.getBoys_Weight());
        }

        for (int j = 0; j < months.length; j++) {
            multiRenderer.addXTextLabel(j, months[j]);
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
        multiRenderer.setShowGrid(true);
        multiRenderer.addSeriesRenderer(heightRenderer);
        multiRenderer.addSeriesRenderer(weightRenderer);
        multiRenderer.addSeriesRenderer(beforeMealRenderer);
        multiRenderer.addSeriesRenderer(afterMealRenderer);

        if (chart != null) {
            mChartLayout.removeView(chart);
        }

        String[] types = new String[]{ScatterChart.TYPE, ScatterChart.TYPE, LineChart.TYPE, LineChart.TYPE};
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        // Creating a combined chart with the chart types specified in types array
        chart = (GraphicalView) ChartFactory.getCombinedXYChartView(getActivity(), dataset, multiRenderer, types);
        mChartLayout.addView(chart);
    }

    @SuppressLint("ResourceAsColor")
    private void openChartGirl(ArrayList<BabyHeightWeightBean> list) {
// Creating an XYSeries for before meal & after meal
        boy_heightSeries = new XYSeries("Required Height ");  // Adding data to
        boy_weightSeries = new XYSeries("Required Weight ");

        actualWeightSeries = new XYSeries("");
        actualHeightSeries = new XYSeries("");

        RangeCategorySeries heightSeries = new RangeCategorySeries("Baby Height");
        RangeCategorySeries weightSeries = new RangeCategorySeries("Baby Weight");
        for (int i = 0; i < month_position; i++) {
            heightSeries.add(-3);
            weightSeries.add(-3);
        }
        heightSeries.add(mBabyHeight, mBabyHeight);
        weightSeries.add(mBabyWeight, mBabyWeight);

        // Creating a dataset to hold each series
        dataset = new XYMultipleSeriesDataset(); // Adding Income Series to the
        // dataset

        dataset.addSeries(heightSeries.toXYSeries());
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer heightRenderer = new XYSeriesRenderer();
        heightRenderer.setColor(Color.BLUE);
        heightRenderer.setPointStyle(PointStyle.CIRCLE);
        heightRenderer.setFillPoints(true);
        heightRenderer.setLineWidth(2);
        heightRenderer.setDisplayChartValues(true);
        heightRenderer.setDisplayChartValuesDistance(1);


        dataset.addSeries(weightSeries.toXYSeries());
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer weightRenderer = new XYSeriesRenderer();
        weightRenderer.setColor(Color.MAGENTA);
        weightRenderer.setPointStyle(PointStyle.CIRCLE);
        weightRenderer.setFillPoints(true);
        weightRenderer.setLineWidth(2);
        weightRenderer.setDisplayChartValues(true);
        weightRenderer.setDisplayChartValuesDistance(1);

        // Adding beforeMeal Series to dataset
        dataset.addSeries(boy_heightSeries);
        // Creating XYSeriesRenderer to customize beforeMealSeries
        XYSeriesRenderer beforeMealRenderer = new XYSeriesRenderer();
        beforeMealRenderer.setColor(Color.RED);
        beforeMealRenderer.setPointStyle(PointStyle.CIRCLE);
        beforeMealRenderer.setFillPoints(true);
        beforeMealRenderer.setLineWidth(2f);
        beforeMealRenderer.setDisplayChartValues(false);

        dataset.addSeries(boy_weightSeries);
        // Creating XYSeriesRenderer to customize afterMealSeries
        XYSeriesRenderer afterMealRenderer = new XYSeriesRenderer();
        afterMealRenderer.setColor(Color.GREEN);
        afterMealRenderer.setPointStyle(PointStyle.CIRCLE);
        afterMealRenderer.setFillPoints(true);
        afterMealRenderer.setLineWidth(2f);
        afterMealRenderer.setDisplayChartValues(false);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYLabels(10);//
        multiRenderer.setLegendTextSize(chartTextSize);
        multiRenderer.setXTitle("Months");
        multiRenderer.setYTitle(" Value ");
        multiRenderer.setAxisTitleTextSize(chartTextSize);
        multiRenderer.setChartTitleTextSize(chartTextSize);
        multiRenderer.setShowGrid(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setXAxisMax(5);
        multiRenderer.setYAxisMax(60);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYLabelsPadding(8);
        multiRenderer.setMargins(new int[]{15, 30, 5, 15});

        for (int i = 0; i < list.size(); i++) {
            BabyHeightWeightBean item = list.get(i);
            boy_heightSeries.add(i, item.getGirls_Height());
            boy_weightSeries.add(i, item.getGirls_Weight());
        }

        for (int j = 0; j < months.length; j++) {
            multiRenderer.addXTextLabel(j, months[j]);
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
        multiRenderer.setShowGrid(true);
        multiRenderer.addSeriesRenderer(heightRenderer);
        multiRenderer.addSeriesRenderer(weightRenderer);
        multiRenderer.addSeriesRenderer(beforeMealRenderer);
        multiRenderer.addSeriesRenderer(afterMealRenderer);

        if (chart != null) {
            mChartLayout.removeView(chart);
        }

        String[] types = new String[]{ScatterChart.TYPE, ScatterChart.TYPE, LineChart.TYPE, LineChart.TYPE};
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        // Creating a combined chart with the chart types specified in types array
        chart = (GraphicalView) ChartFactory.getCombinedXYChartView(getActivity(), dataset, multiRenderer, types);
        mChartLayout.addView(chart);
    }

}

