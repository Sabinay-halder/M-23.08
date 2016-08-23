package com.widevision.pregnantwoman.mother;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.MotherHelthParameterTable;
import com.widevision.pregnantwoman.database.MotherRecordTable;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.ObjectSerializer;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelthParameterActivityWomen extends Fragment implements DatePickerDialog.OnDateSetListener {


    @Bind(R.id.NameEdt)
    TextView nameEdt;
    @NotEmpty(message = "date required.")
    @Bind(R.id.dateEdt)
    EditText dateEdt;
    @Bind(R.id.saveBt)
    Button mSaveBtn;
    @Bind(R.id.cancelBt)
    Button mCancelBtn;
    @Bind(R.id.weight_seek)
    DiscreteSeekBar weightSeek;
    @Bind(R.id.systolic_seek)
    DiscreteSeekBar systolicSeek;
    @Bind(R.id.diastolic_seek)
    DiscreteSeekBar diastolicSeek;

    @Bind(R.id.bloodsystolicTxt)
    TextView systolicTxt;
    @Bind(R.id.diastolicTxt)
    TextView diastolicTxt;

    @Bind(R.id.weightTxt)
    TextView weightTxt;
    @Bind(R.id.fasting_before_meal_Txt)
    TextView mFastingBeforeMealTxt;
    @Bind(R.id.after_meal_Txt)
    TextView mAfterMealTxt;
    @Bind(R.id.fasting_before_meal_seek)
    DiscreteSeekBar mFastingBeforeMealSeek;
    @Bind(R.id.after_meal_seek)
    DiscreteSeekBar mAfterMealSeek;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    private RadioButton radioButton;
    private String motherNameStr = "", dateStr = "", conceiveDate = "";
    private ActiveAndroidDBHelper helper;
    private boolean mPreExistinDiabetes = false;
    List<MotherRecordTable> motherList;
    View view;

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceConnector.writeString(getActivity(), PreferenceConnector.MOTHER_FIRST_TIME, "No");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helthparameter_activity_woman, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = ActiveAndroidDBHelper.getInstance();
        motherNameStr = PreferenceConnector.readString(getActivity(), PreferenceConnector.MOTHER_NAME, "");
        dateStr = Constant.getDate();

        nameEdt.setText(motherNameStr);

        dateEdt.setText(dateStr);
        motherList = helper.viewMother();
        conceiveDate = motherList.get(0).expecteConceiveDate;
        List<MotherHelthParameterTable> mList = helper.viewAllHealthRecord();
        if (mList.size() != 0) {
            dateEdt.setText(mList.get(mList.size() - 1)._Date);
            weightTxt.setText(mList.get(mList.size() - 1)._Weight);
            weightSeek.setProgress(Integer.parseInt(mList.get(mList.size() - 1)._Weight));

            systolicSeek.setProgress(Integer.parseInt(mList.get(mList.size() - 1)._SystolicBloodPressure));
            systolicTxt.setText(mList.get(mList.size() - 1)._SystolicBloodPressure);

            diastolicSeek.setProgress(Integer.parseInt(mList.get(mList.size() - 1)._DiastolicBloodPressure));
            diastolicTxt.setText(mList.get(mList.size() - 1)._DiastolicBloodPressure);

            mFastingBeforeMealSeek.setProgress((int) mList.get(mList.size() - 1)._BloodSugarBeforeMeal);
            mFastingBeforeMealTxt.setText("" + mList.get(mList.size() - 1)._BloodSugarBeforeMeal);

            mAfterMealSeek.setProgress((int) mList.get(mList.size() - 1)._BloodSugarAfterMeal);
            mAfterMealTxt.setText("" + mList.get(mList.size() - 1)._BloodSugarAfterMeal);

        }


        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        dateEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePicker();
                }
            }
        });
        weightSeek.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                weightTxt.setText("" + i);
                return i;
            }
        });
        systolicSeek.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                systolicTxt.setText("" + i);
                return i;
            }
        });
        diastolicSeek.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                diastolicTxt.setText("" + i);
                return i;
            }
        });


        mFastingBeforeMealSeek.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                mFastingBeforeMealTxt.setText("" + i);
                return i;
            }
        });

        mAfterMealSeek.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                mAfterMealTxt.setText("" + i);
                return i;
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) view.findViewById(id);
                if (radioButton.getText().toString().trim().equals(getActivity().getResources().getString(R.string.pre).trim())) {
                    mPreExistinDiabetes = true;
                } else if (radioButton.getText().toString().trim().equals(getActivity().getResources().getString(R.string.guest).trim())) {
                    mPreExistinDiabetes = false;
                }
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdt.getText().toString().trim();
                String date = dateEdt.getText().toString().trim();
                String weight = weightTxt.getText().toString().trim();
                String systolic = systolicTxt.getText().toString().trim();
                String diastolic = diastolicTxt.getText().toString().trim();
                String beforeMeal = mFastingBeforeMealTxt.getText().toString().trim();
                String afterMeal = mAfterMealTxt.getText().toString().trim();
                List<MotherRecordTable> listMother = helper.viewMother();
                List<MotherHelthParameterTable> helthParameterTableList = helper.viewAllHealthRecord();

                int tag = 0;
                for (int i = 0; i < helthParameterTableList.size(); i++) {
                    if (helthParameterTableList.get(i)._Date.trim().equals(date)) {
                        helper.updateHelthParameter(helthParameterTableList.get(i).getId(), listMother.get(listMother.size() - 1).getId(), date, weight, systolic, diastolic, Float.parseFloat(beforeMeal), Float.parseFloat(afterMeal), mPreExistinDiabetes);
                        tag = 1;
                    }
                }

                if (tag == 0) {
                    helper.addHelthParameter(listMother.get(listMother.size() - 1).getId(), date, weight, systolic, diastolic, Float.parseFloat(beforeMeal), Float.parseFloat(afterMeal), mPreExistinDiabetes);
                }

                String startDate = PreferenceConnector.readString(getActivity(), PreferenceConnector.START_DATE, "");
                DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePattern);
                DateTime dateTime = formatter.parseDateTime(startDate);
                DateTime dateTimeCurrent = formatter.parseDateTime(date);
                int week = Weeks.weeksBetween(dateTime, dateTimeCurrent).getWeeks();
                ArrayList<Float> list = new ArrayList<>();
                ArrayList<Integer> systolicList = new ArrayList<>();
                ArrayList<Integer> diastolicList = new ArrayList<>();
                //      load tasks from preference
                SharedPreferences prefs = getActivity().getSharedPreferences("list_file", Context.MODE_PRIVATE);
                try {
                    list = (ArrayList<Float>) ObjectSerializer.deserialize(prefs.getString("weight_list", ObjectSerializer.serialize(new ArrayList<Float>())));
                    systolicList = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString("systolic_list", ObjectSerializer.serialize(new ArrayList<Integer>())));
                    diastolicList = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString("diastolic_list", ObjectSerializer.serialize(new ArrayList<Integer>())));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int w = PreferenceConnector.readInteger(getActivity(), PreferenceConnector.WEEK, 0);
                if (w < 0 || week < 0) {
                    Constant.setAlert(getActivity(), "Enter correct date.");
                    return;
                } else {
                    if (week == 0) {
                        list.set(0, (float) Integer.parseInt(weight));
                        systolicList.set(0, Integer.parseInt(systolic));
                        diastolicList.set(0, Integer.parseInt(diastolic));
                    } else if (week == w) {
                        list.set(w - 1, (float) Integer.parseInt(weight));
                        systolicList.set(w - 1, Integer.parseInt(systolic));
                        diastolicList.set(w - 1, Integer.parseInt(diastolic));
                    } else if (week <= 39) {
                        for (int i = w + 1; i < week; i++) {
                            if (w == 0) {
                                if (i == week - 1) {
                                    list.set(i, (float) Integer.parseInt(weight));
                                    systolicList.set(i, Integer.parseInt(systolic));
                                    diastolicList.set(i, Integer.parseInt(diastolic));
                                    PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.START_WEIGHT, Integer.parseInt(weight));
                                    PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.START_WEEK, week);
                                } else {
                                    list.set(i, 10f);
                                    systolicList.set(i, 10);
                                    diastolicList.set(i, 10);
                                }
                            } else {
                                list.set(i, (float) Integer.parseInt(weight));
                                systolicList.set(i, Integer.parseInt(systolic));
                                diastolicList.set(i, Integer.parseInt(diastolic));
                            }
                        }
                    }
                }

                PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.WEEK, week);
                //save the task list to preference

                SharedPreferences.Editor editor = prefs.edit();
                try {
                    editor.putString("weight_list", ObjectSerializer.serialize(list));
                    editor.putString("systolic_list", ObjectSerializer.serialize(systolicList));
                    editor.putString("diastolic_list", ObjectSerializer.serialize(diastolicList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.commit();
                MainFragmentActivity.fragmentManager.popBackStack();
                getActivity().startActivity(new Intent(getActivity(), HealthParameterShowActivity.class));
                PreferenceConnector.writeString(getActivity(), PreferenceConnector.MOTHER_FIRST_TIME, "No");
            }
        });

        mCancelBtn.setTextColor(Color.WHITE);
        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.MOTHER_FIRST_TIME, "Yes").equals("Yes")) {
            mCancelBtn.setText("Skip");
        } else {
            mCancelBtn.setText("Cancle");
        }

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragmentActivity.fragmentManager.popBackStack();
                PreferenceConnector.writeString(getActivity(), PreferenceConnector.MOTHER_FIRST_TIME, "No");
            }
        });
    }

    void showDatePicker() {

        int year = 0, month = 0, day = 0;
        Calendar now = Calendar.getInstance();
        String dateToSet = "";

        if (dateEdt.getText().toString().trim().equals("")) {
            dateToSet = Constant.getDate();
        } else {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePattern);
            DateTime selectedDate = formatter.parseDateTime(dateEdt.getText().toString().trim());
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
                    String newDate = a[2] + "-" + a[1] + "-" + a[0];
                    DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePatternFullName);
                    DateTimeFormatter formatter1 = DateTimeFormat.forPattern(Constant.datePattern);
                    DateTime selectedDate = formatter.parseDateTime(newDate);
                    DateTime current = formatter1.parseDateTime(Constant.getDate());
                    if (!selectedDate.isAfter(current.minusDays(1))) {
                        Constant.setAlert(getActivity(), "Please select valid date.");
                        return;
                    }
                    dateEdt.setText(selectedDate.getYear() + "-" + selectedDate.getMonthOfYear() + "-" + selectedDate.getDayOfMonth());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        datePicker.setPicker();
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3) {
        i2++;
        String dd = "" + i3;
        String mm = "" + i2;
        if (dd.length() == 1) {
            dd = "0" + dd;
        }
        if (mm.length() == 1) {
            mm = "0" + mm;
        }
        String date = i + "-" + mm + "-" + dd;
        dateEdt.setText(date);
    }
}
