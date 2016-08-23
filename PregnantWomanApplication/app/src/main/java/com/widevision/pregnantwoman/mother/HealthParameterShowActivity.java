package com.widevision.pregnantwoman.mother;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.model.HideKeyActivity;
import com.widevision.pregnantwoman.util.ObjectSerializer;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HealthParameterShowActivity extends HideKeyActivity {

    @Bind(R.id.title)
    TextView titleTxt;
    @Bind(R.id.content)
    TextView contentTxt;
    @Bind(R.id.careTxt)
    TextView careTxt;
    @Bind(R.id.doneTxt)
    TextView doneTxt;
    private ActiveAndroidDBHelper helper;
    private String titleStr = "", contentStr = "", careStr = "";
    private StringBuilder contentBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.health_parameter_show_layout);
        ButterKnife.bind(this);

        helper = ActiveAndroidDBHelper.getInstance();
        String column_min = "";
        String column_max = "";
        float mStartWeight = 0;
        ArrayList<Float> minList = new ArrayList<>();
        ArrayList<Float> maxList = new ArrayList<>();
        ArrayList<Float> listFirst;
        //      load tasks from preference
        SharedPreferences prefs = getSharedPreferences("list_file", Context.MODE_PRIVATE);
        try {
            listFirst = (ArrayList<Float>) ObjectSerializer.deserialize(prefs.getString("weight_list", ObjectSerializer.serialize(new ArrayList<Float>())));

            mStartWeight = listFirst.get(0);
            if (PreferenceConnector.readString(HealthParameterShowActivity.this, PreferenceConnector.TWINS, "No").equals("Yes")) {
                if (mStartWeight < 69) {
                    column_min = "twins_min_68";
                    column_max = "twins_max_68";
                } else if (mStartWeight < 82) {
                    column_min = "twins_min_81";
                    column_max = "twins_max_81";
                } else if (mStartWeight < 151) {
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
            int week = PreferenceConnector.readInteger(HealthParameterShowActivity.this, PreferenceConnector.WEEK, 0);
            Cursor cursor = helper.viewAll();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Float s = cursor.getFloat(cursor.getColumnIndex(column_min));
                    Float s1 = cursor.getFloat(cursor.getColumnIndex(column_max));
                    minList.add(s + mStartWeight);
                    maxList.add(s1 + mStartWeight);
                } while (cursor.moveToNext());
            }
            if (listFirst == null || listFirst.size() == 0) {
            } else if (listFirst.get(week) > maxList.get(week)) {
                titleStr = "Attention !";
                contentBuilder.append("high weight");
            } else if (listFirst.get(week) < minList.get(week)) {
                contentBuilder.append("low weight");
                titleStr = "Attention !";
            } else {
                titleStr = "Congratulations";
            }

            //for blood presure
            // load tasks from preference
            ArrayList<Integer> systolic;
            ArrayList<Integer> diastolic;

            systolic = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString("systolic_list", ObjectSerializer.serialize(new ArrayList<Integer>())));
            diastolic = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString("diastolic_list", ObjectSerializer.serialize(new ArrayList<Integer>())));
            if (systolic == null || systolic.size() == 0 || diastolic == null || diastolic.size() == 0) {
            } else if (systolic.get(week) < 90 || diastolic.get(week) < 60) {
                if (titleStr.trim().equals("Attention !")) {
                    contentBuilder.append(", ").append("low blood pressure");
                } else {
                    contentBuilder.append("low blood pressure");
                }
                titleStr = "Attention!";
            } else if (systolic.get(week) < 120 || diastolic.get(week) < 80) {
                titleStr = "Congratulations";
            } else if (systolic.get(week) < 140 || diastolic.get(week) < 90) {
                if (titleStr.trim().equals("Attention!")) {
                    contentBuilder.append(", ").append("pre-high blood pressure");
                } else {
                    contentBuilder.append("pre-high blood pressure");
                }
                titleStr = "Attention!";
            } else if (systolic.get(week) > 140 || diastolic.get(week) > 90) {
                if (titleStr.trim().equals("Attention!")) {
                    contentBuilder.append(", ").append("high blood pressure");
                } else {
                    contentBuilder.append("high blood pressure");
                }
                titleStr = "Attention!";
            }
        } catch (Exception e) {
        }

        if (titleStr.trim().equals("Congratulations")) {
            contentStr = getResources().getString(R.string.content_congratulation);
            careStr = "";
            titleTxt.setBackgroundColor(getResources().getColor(R.color.green));
        } else {
            contentStr = getResources().getString(R.string.content_attention_1) + " <font color='#ff0000'> " + contentBuilder.toString() + "</font> " + getResources().getString(R.string.content_attention_2);
            careStr = getResources().getString(R.string.care_txt);
            titleTxt.setBackgroundColor(getResources().getColor(R.color.red_dark));
        }

        titleTxt.setText(titleStr);
        contentTxt.setText(Html.fromHtml(contentStr));
        careTxt.setText(careStr);

        doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
