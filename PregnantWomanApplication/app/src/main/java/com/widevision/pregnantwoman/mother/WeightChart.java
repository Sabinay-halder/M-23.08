package com.widevision.pregnantwoman.mother;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.ObjectSerializer;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeightChart extends HideKeyFragment {

    private XYMultipleSeriesRenderer multiRenderer;
    private XYMultipleSeriesDataset dataset;
    private XYSeries minWeightSeries, maxWeightSeries, userWeightSeries;
    private float chartTextSize;
    private GraphicalView chart;

    @Bind(R.id.chartLayout)
    LinearLayout mChartLayout;
    @Bind(R.id.nameEdt)
    TextView nameEdt;

    private ArrayList<Float> weightListFirst = new ArrayList<>();
    private ArrayList<Float> weightListSecond = new ArrayList<>();
    private ActiveAndroidDBHelper helper;
    private ArrayList<Float> misList = new ArrayList<>();
    private ArrayList<Float> maxList = new ArrayList<>();
    private String column_min = "";
    private String column_max = "";
    private float mStartWeight = 0;
    private int mStartWeek = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weight_char_activity, container, false);
        ButterKnife.bind(this, view);
        helper = ActiveAndroidDBHelper.getInstance();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        chartTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);
        String name = PreferenceConnector.readString(getActivity(), PreferenceConnector.MOTHER_NAME, "");

        nameEdt.setText(name);

        //      load tasks from preference
        SharedPreferences prefs = getActivity().getSharedPreferences("list_file", Context.MODE_PRIVATE);
        try {
            weightListFirst = (ArrayList<Float>) ObjectSerializer.deserialize(prefs.getString("weight_list", ObjectSerializer.serialize(new ArrayList<Float>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //check twins or not then check starting weight
        mStartWeight = PreferenceConnector.readInteger(getActivity(), PreferenceConnector.START_WEIGHT, 0);
        mStartWeek = PreferenceConnector.readInteger(getActivity(), PreferenceConnector.START_WEEK, 0);
        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.TWINS, "No").equals("Yes")) {
            if (mStartWeight < 69) {
                column_min = "twins_min_68";
                column_max = "twins_max_68";
            } else if (mStartWeight < 82) {
                column_min = "twins_min_81";
                column_max = "twins_max_81";
            } else if (mStartWeight <= 151) {
                column_min = "twins_min_150";
                column_max = "twins_max_150";
            }
        } else {
            if (mStartWeight < 51) {
                column_min = "min_51";
                column_max = "max_51";
            } else if (mStartWeight < 69) {
                column_min = "min_68";
                column_max = "max_68";
            } else if (mStartWeight < 82) {
                column_min = "min_69";
                column_max = "max_81";
            } else if (mStartWeight < 151) {
                column_min = "min_150";
                column_max = "max_150";
            }
        }

        Cursor cursor = helper.viewAll();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Float s = cursor.getFloat(cursor.getColumnIndex(column_min));
                Float s1 = cursor.getFloat(cursor.getColumnIndex(column_max));
                misList.add(s + mStartWeight);
                maxList.add(s1 + mStartWeight);
            } while (cursor.moveToNext());
        }
        ArrayList<Float> list1 = new ArrayList<>();
        ArrayList<Float> list2 = new ArrayList<>();

        int week = PreferenceConnector.readInteger(getActivity(), PreferenceConnector.WEEK, 0);
        if (week == 0) {
            list1.add(misList.get(0));
            list2.add(maxList.get(0));
            weightListSecond.add(weightListFirst.get(0));
        } else {
            for (int i = 0; i < week; i++) {
                if (mStartWeek <= week) {
                    weightListSecond.add(weightListFirst.get(i));
                    list1.add(misList.get(i));
                    list2.add(maxList.get(i));
                } else {
                    weightListSecond.add(0f);
                    list1.add(0f);
                    list2.add(0f);
                }
            }
        }
        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0) {
            Constant.setAlert(getActivity(), "Enter health parameter first.");
            MainFragmentActivity.fragmentManager.popBackStack();
        } else {
            openChart(weightListSecond, list1, list2);
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (chart != null) {
            chart = ChartFactory.getLineChartView(getActivity(), dataset,
                    multiRenderer);
            mChartLayout.addView(chart);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void openChart(ArrayList<Float> userWeightList, ArrayList<Float> minWeightList, ArrayList<Float> maxWeightList) {

//xy series
        minWeightSeries = new XYSeries("MINIMUM WEIGHT");
        maxWeightSeries = new XYSeries("MAXIMUM WEIGHT");
        userWeightSeries = new XYSeries("YOUR WEIGHT");

        // Creating a dataset to hold each series
        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(minWeightSeries);

        // Adding Expense Series to dataset
        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer minWeightSeriesRenderer = new XYSeriesRenderer();
        minWeightSeriesRenderer.setColor(Color.RED);
        minWeightSeriesRenderer.setPointStyle(PointStyle.CIRCLE);
        minWeightSeriesRenderer.setFillPoints(true);
        minWeightSeriesRenderer.setLineWidth(2.8f);
        minWeightSeriesRenderer.setDisplayChartValues(false);
        dataset.addSeries(maxWeightSeries);
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer maxWeightSeriesRenderer = new XYSeriesRenderer();
        maxWeightSeriesRenderer.setColor(Color.BLUE);
        maxWeightSeriesRenderer.setPointStyle(PointStyle.CIRCLE);
        maxWeightSeriesRenderer.setFillPoints(true);
        maxWeightSeriesRenderer.setLineWidth(2);
        maxWeightSeriesRenderer.setDisplayChartValues(false);

        dataset.addSeries(userWeightSeries);
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer userWeightSeriesRenderer = new XYSeriesRenderer();
        userWeightSeriesRenderer.setColor(Color.GREEN);
        userWeightSeriesRenderer.setPointStyle(PointStyle.CIRCLE);
        userWeightSeriesRenderer.setFillPoints(true);
        userWeightSeriesRenderer.setLineWidth(2);
        userWeightSeriesRenderer.setDisplayChartValues(true);
        userWeightSeriesRenderer.setDisplayChartValuesDistance(1);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(10);
        multiRenderer.setYLabels(10);//
        multiRenderer.setLegendTextSize(chartTextSize);
        multiRenderer.setXTitle("Week");
        multiRenderer.setYTitle("Weight in Kg");

        multiRenderer.setAxisTitleTextSize(chartTextSize);
        multiRenderer.setChartTitleTextSize(chartTextSize);

        multiRenderer.setShowGrid(false);
        multiRenderer.setGridColor(Color.GRAY);

        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);

        multiRenderer.setXAxisMax(10);
        multiRenderer.setYAxisMax(150);
        multiRenderer.setYAxisMin(45);

        multiRenderer.setYLabelsPadding(8);

        multiRenderer.setMargins(new int[]{15, 30, 5, 15});


        for (int i = 0; i < minWeightList.size(); i++) {
            minWeightSeries.add(i, minWeightList.get(i));
            maxWeightSeries.add(i, maxWeightList.get(i));
            userWeightSeries.add(i, userWeightList.get(i));
        }

        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        // for change background multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);

        // for zoom buttons
        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setPanEnabled(true, false);
        multiRenderer.setPanLimits(new double[]{0, 39, 0, 160});
        multiRenderer.addSeriesRenderer(minWeightSeriesRenderer);
        multiRenderer.addSeriesRenderer(maxWeightSeriesRenderer);
        multiRenderer.addSeriesRenderer(userWeightSeriesRenderer);
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        chart = ChartFactory.getLineChartView(getActivity(), dataset,
                multiRenderer);
        mChartLayout.addView(chart);
    }
}
