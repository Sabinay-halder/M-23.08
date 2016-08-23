package com.widevision.pregnantwoman.mother;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

public class BloodPresureChart extends HideKeyFragment {

    @Bind(R.id.title)
    TextView mTitle;
    private XYMultipleSeriesRenderer multiRenderer;
    private XYMultipleSeriesDataset dataset;
    private XYSeries yourSystolicSeries, yourDiastolicSeries, systolicRequiredSeries, diastolicRequiredSeries;
    private float chartTextSize;
    private GraphicalView chart;

    @Bind(R.id.chartLayout)
    LinearLayout mChartLayout;
    private ArrayList<Integer> listFirst = new ArrayList<>();
    private ArrayList<Integer> listSecond = new ArrayList<>();
    private ArrayList<Integer> systolic = new ArrayList<>();
    private ArrayList<Integer> diastolic = new ArrayList<>();
    private ActiveAndroidDBHelper helper;

    @Bind(R.id.nameEdt)
    TextView mNameTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weight_char_activity, container, false);
        ButterKnife.bind(this, view);
        mTitle.setText("Blood Pressure Chart For");
        String name = PreferenceConnector.readString(getActivity(), PreferenceConnector.MOTHER_NAME, "");
        mNameTxt.setText(name);

        helper = ActiveAndroidDBHelper.getInstance();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        chartTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);

        //      load tasks from preference
        SharedPreferences prefs = getActivity().getSharedPreferences("list_file", Context.MODE_PRIVATE);
        try {
            listFirst = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString("systolic_list", ObjectSerializer.serialize(new ArrayList<Integer>())));
            listSecond = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString("diastolic_list", ObjectSerializer.serialize(new ArrayList<Integer>())));
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayList<Integer> systolicRequired = new ArrayList<>();
        ArrayList<Integer> diastolicRequired = new ArrayList<>();

        int week = PreferenceConnector.readInteger(getActivity(), PreferenceConnector.WEEK, 0);
        if (week == 0) {
            systolic.add(listFirst.get(0));
            diastolic.add(listSecond.get(0));
        } else {
            for (int i = 0; i < week; i++) {
                systolic.add(listFirst.get(i));
                diastolic.add(listSecond.get(i));
            }
        }
        for (int j = 0; j < 40; j++) {
            systolicRequired.add(100);
            diastolicRequired.add(70);
        }

        if (systolic == null || systolic.size() == 0 || diastolic == null || diastolic.size() == 0) {
            Constant.setAlert(getActivity(), "Enter health parameter first.");
            MainFragmentActivity.fragmentManager.popBackStack();
        } else {
            openChart(systolic, diastolic, systolicRequired, diastolicRequired);
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
    private void openChart(ArrayList<Integer> systolic, ArrayList<Integer> diastolic, ArrayList<Integer> diastolicRequired, ArrayList<Integer> systolicRequired) {

        systolicRequiredSeries = new XYSeries("Systolic Required");
        diastolicRequiredSeries = new XYSeries("Diastolic Required");
        yourSystolicSeries = new XYSeries("Your Systolic");
        // Creating an XYSeries for Expense
        yourDiastolicSeries = new XYSeries("Your Diastolic"); // Adding data to

        // Creating a dataset to hold each series
        dataset = new XYMultipleSeriesDataset(); // Adding Income Series to the
        // dataset

        dataset.addSeries(diastolicRequiredSeries);

        // Adding Expense Series to dataset
        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.RED);
        incomeRenderer.setPointStyle(PointStyle.CIRCLE);
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(2f);
        incomeRenderer.setDisplayChartValues(false);

        dataset.addSeries(yourDiastolicSeries);
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.GREEN);
        expenseRenderer.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);
        expenseRenderer.setDisplayChartValuesDistance(1);

        dataset.addSeries(systolicRequiredSeries);
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer2 = new XYSeriesRenderer();
        expenseRenderer2.setColor(Color.BLUE);
        expenseRenderer2.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer2.setFillPoints(true);
        expenseRenderer2.setLineWidth(2);
        expenseRenderer2.setDisplayChartValues(false);

        dataset.addSeries(yourSystolicSeries);
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer3 = new XYSeriesRenderer();
        expenseRenderer3.setColor(Color.parseColor("#4D8EB4"));
        expenseRenderer3.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer3.setFillPoints(true);
        expenseRenderer3.setLineWidth(2);
        expenseRenderer3.setDisplayChartValues(true);
        expenseRenderer3.setDisplayChartValuesDistance(1);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(10);
        multiRenderer.setYLabels(10);//
        multiRenderer.setLegendTextSize(chartTextSize);
        multiRenderer.setXTitle("Week");
        multiRenderer.setYTitle("mmHg/bpm");

        multiRenderer.setAxisTitleTextSize(chartTextSize);
        multiRenderer.setChartTitleTextSize(chartTextSize);

        multiRenderer.setShowGrid(false);
        multiRenderer.setGridColor(Color.GRAY);

        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);

        multiRenderer.setXAxisMax(10);
        multiRenderer.setYAxisMax(170);
        multiRenderer.setYAxisMin(40);

        multiRenderer.setYLabelsPadding(8);
        multiRenderer.setPanEnabled(true, true);
        multiRenderer.setMargins(new int[]{15, 30, 5, 15});


        for (int i = 0; i < diastolic.size(); i++) {
            yourDiastolicSeries.add(i, diastolic.get(i));
            diastolicRequiredSeries.add(i, diastolicRequired.get(i));
            systolicRequiredSeries.add(i, systolicRequired.get(i));
            yourSystolicSeries.add(i, systolic.get(i));
        }
      /*  for (int i = 0; i < diastolicRequired.size(); i++) {

        }
        for (int i = 0; i < systolicRequired.size(); i++) {

        }
        for (int i = 0; i < systolic.size(); i++) {

        }*/
        multiRenderer.setPanLimits(new double[]{-1, 40, -1, 200});

        multiRenderer.setLabelsColor(Color.BLACK);

        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        // for change background multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);

        // for zoom buttons
        multiRenderer.setZoomButtonsVisible(false);

        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer.addSeriesRenderer(expenseRenderer);
        multiRenderer.addSeriesRenderer(expenseRenderer2);
        multiRenderer.addSeriesRenderer(expenseRenderer3);
        if (chart != null) {
            mChartLayout.removeView(chart);
        }
        chart = ChartFactory.getLineChartView(getActivity(), dataset,
                multiRenderer);
        mChartLayout.addView(chart);
    }
}
