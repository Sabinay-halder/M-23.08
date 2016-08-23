package com.widevision.pregnantwoman.mother;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.HomeActivity;
import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.ObjectSerializer;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by newtrainee on 30/7/15.
 */
public class WomanHomeActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, Validator.ValidationListener, OnShowcaseEventListener {

    @NotEmpty(message = "Name is required.")
    @Bind(R.id.nameEt)
    MaterialEditText nameEt;
    @NotEmpty(message = "Last Name is required.")
    @Bind(R.id.lastNameEt)
    MaterialEditText lastNameEt;
    @NotEmpty(message = "Please select date of birth.")
    @Bind(R.id.dateofbirthEt)
    MaterialEditText dateofbirthEt;
    @NotEmpty(message = "Age is required.")
    @Bind(R.id.ageEt)
    MaterialEditText ageEt;
    @NotEmpty(message = "Please select expected date.")
    @Bind(R.id.expected_dateEt)
    MaterialEditText mExpecteConceiveDate;
    @NotEmpty(message = "Please select expected date.")
    @Bind(R.id.delivery_dateEt)
    EditText mExpecteDeliveryDate;
    @Bind(R.id.saveBt)
    ImageView saveBt;
    @Bind(R.id.cancelBt)
    ImageView cancelBt;
    @Bind(R.id.twin)
    CheckBox mTwinsChek;
    @Bind(R.id.back)
    LinearLayout mBackBtn;
    @Bind(R.id.checkLayout)
    LinearLayout mCheckLayout;
    @Bind(R.id.doctorNameEt)
    MaterialEditText doctorNameEt;
    @Bind(R.id.doctorNumberEt)
    MaterialEditText doctorNumberEt;
    @Bind(R.id.myProfileTxt)
    TextView myProfileTxt;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    private Validator validator;
    private boolean aDate = false, bDate = false, DeliveryDate = false;
    private String doctorName = "", doctorNumber = "", nameStr = "", lastNameStr = "", dobStr = "", ageStr = "", extectedDateStr = "", pregnancyMonth = "", twinsStr = "", expectedDeliveryDateStr = "";
    private ArrayList<Float> weightList = new ArrayList<>();
    private ArrayList<Integer> systolicBloodPressure = new ArrayList<>();
    private ArrayList<Integer> diastolicBloodPressure = new ArrayList<>();

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Constant.hideSoftKeyboard(WomanHomeActivity.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_women);
        ButterKnife.bind(this);
        setupUI(findViewById(R.id.parent_homeactivitywomen_second));

        myProfileTxt.setText("My Profile");


        validator = new Validator(this);
        validator.setValidationListener(this);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WomanHomeActivity.this, HomeActivity.class));
                finish();
            }
        });
        mTwinsChek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                twinsStr = b ? "Yes" : "No";
                PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.TWINS, twinsStr);
            }
        });

        mCheckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwinsChek.isChecked()) {
                    mTwinsChek.setChecked(false);
                    twinsStr = "No";
                } else {
                    mTwinsChek.setChecked(true);
                    twinsStr = "Yes";
                }
                PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.TWINS, twinsStr);
            }
        });

        for (int i = 0; i < 40; i++) {
            weightList.add((float) i);
            systolicBloodPressure.add(i);
            diastolicBloodPressure.add(i);
        }

        dateofbirthEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    setDatePicker(dateofbirthEt);
                }
            }
        });
        dateofbirthEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setDatePicker(dateofbirthEt);
            }
        });
        mExpecteConceiveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setDatePicker(mExpecteConceiveDate);
            }
        });
        mExpecteConceiveDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setDatePicker(mExpecteConceiveDate);
                }

            }
        });
        mExpecteDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(mExpecteDeliveryDate);

            }
        });
        mExpecteDeliveryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    setDatePicker(mExpecteDeliveryDate);
                }
            }
        });
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WomanHomeActivity.this, HomeActivity.class));
                finish();

            }
        });
    }

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
        DatePickerViewAndroid datePicker = new DatePickerViewAndroid(WomanHomeActivity.this, dateToSet, "yyyy-MM-dd", new MyListener() {
            @Override
            public void onSet(String date) {
                String a[] = date.split("-");
                String newDate = a[2] + "-" + a[1] + "-" + a[0];
                DateTimeFormatter formatter1 = DateTimeFormat.forPattern(Constant.datePatternFullName);
                DateTime selectedDate1 = formatter1.parseDateTime(newDate);

                switch (textView.getId()) {
                    case R.id.dateofbirthEt:
                        dateOfBirthValidation(date);
                        break;
                    case R.id.expected_dateEt:
                        String d = selectedDate1.getYear() + "-" + selectedDate1.getMonthOfYear() + "-" + selectedDate1.getDayOfMonth();
                        DateTimeFormatter formatter2 = DateTimeFormat.forPattern(Constant.datePattern);
                        DateTime selected = formatter2.parseDateTime(d);
                        if (!selected.isBeforeNow()) {
                            Constant.setAlert(WomanHomeActivity.this, "Select valid date.");
                            return;
                        }
                        mExpecteConceiveDate.setText(d);
                        PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.START_DATE, d);
                        DateTime dt = selectedDate1.plusMonths(9);
                        mExpecteDeliveryDate.setText(dt.getYear() + "-" + dt.getMonthOfYear() + "-" + dt.getDayOfMonth());
                        break;
                    case R.id.delivery_dateEt:
                        String d3 = selectedDate1.getYear() + "-" + selectedDate1.getMonthOfYear() + "-" + selectedDate1.getDayOfMonth();
                        DateTimeFormatter formatter3 = DateTimeFormat.forPattern(Constant.datePattern);
                        DateTime selected3 = formatter3.parseDateTime(d3);
                        String expectedDate = mExpecteConceiveDate.getText().toString().trim();
                        if (!expectedDate.equals("")) {
                            DateTime expectedConceive = formatter3.parseDateTime(expectedDate);
                            Period p = Constant.getDuration(expectedConceive, selected3);
                            if (p.getYears() < 1) {
                                if (p.getMonths() < 8 || p.getMonths() > 10) {
                                    Constant.setAlert(WomanHomeActivity.this, "Select valid date.");
                                    return;
                                } else {
                                    mExpecteDeliveryDate.setText(selectedDate1.getYear() + "-" + selectedDate1.getMonthOfYear() + "-" + selectedDate1.getDayOfMonth());
                                }
                            } else {
                                Constant.setAlert(WomanHomeActivity.this, "Select valid date.");
                            }
                        }
                        break;
                }
            }
        });
        datePicker.setPicker();
        datePicker.show();
    }


    void dateOfBirthValidation(String dateSelected) {
        try {
            String a[] = dateSelected.split("-");
            String newDate = a[2] + "-" + a[1] + "-" + a[0];
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.datePatternFullName);
            SimpleDateFormat sdf2 = new SimpleDateFormat(Constant.datePattern);
            Date selectedDate = sdf.parse(newDate);

            Calendar calendar = Calendar.getInstance();
            Date currentDate = sdf2.parse(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
            if (selectedDate.after(currentDate)) {
                Constant.setAlert(WomanHomeActivity.this, "Please select valid date.");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePatternFullName);
            LocalDate date = formatter.parseLocalDate(newDate);

            Period age = Constant.getDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
            int Month = age.getMonths();
            int Year = age.getYears();
            ageStr = Year + "Y" + Month + "M";

            if (Year < 13) {
                Constant.setAlert(WomanHomeActivity.this, "Enter valid birth date.");
                return;
            } else if (Year == 0) {
                String m = (age.getMonths() >= 1) ? "Month" : "Months";
                ageEt.setText(age.getMonths() + m);
            } else if (Month == 0) {
                String m = (age.getYears() <= 1) ? "Year" : "Years";
                ageEt.setText(age.getYears() + " " + m);
            } else {
                String m = (Month >= 1) ? "Month" : "Months";
                String y = (Year <= 1) ? "Year" : "Years";
                ageEt.setText(Year + " " + y + "," + Month + " " + m);
            }
            DateTimeFormatter formatter1 = DateTimeFormat.forPattern(Constant.datePatternFullName);
            DateTime selectedDate1 = formatter1.parseDateTime(newDate);
            dateofbirthEt.setText(selectedDate1.getYear() + "-" + selectedDate1.getMonthOfYear() + "-" + selectedDate1.getDayOfMonth());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(WomanHomeActivity.this, errors.get(0).getCollatedErrorMessage(WomanHomeActivity.this));
    }

    @Override
    public void onValidationSucceeded() {

        DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePattern);
        try {
            DateTime conceciveDate = formatter.parseDateTime(mExpecteConceiveDate.getText().toString().trim());
            DateTime deliveryDate = formatter.parseDateTime(mExpecteDeliveryDate.getText().toString().trim());
            if (deliveryDate.isAfter(conceciveDate)) {
                int period = Constant.getPregnancyMonth(conceciveDate, deliveryDate);
                if (period > 9 || period < 7) {
                    Constant.setAlert(WomanHomeActivity.this, "The difference between conceive date & delivery date should be greater than 8 months.");
                } else {
                    DateTime dateOfBirth = formatter.parseDateTime(dateofbirthEt.getText().toString().trim());

                    Period p = Constant.getDuration(dateOfBirth, conceciveDate);
                    if (p.getYears() < 13) {
                        Constant.setAlert(WomanHomeActivity.this, "Select vaid concecive date");
                        return;
                    }

                    pregnancyMonth = "" + period;
                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.IS_FIRSTTIME_MOTHER, "No");
                    ActiveAndroidDBHelper activeAndroidDBHelper = ActiveAndroidDBHelper.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
                    nameStr = nameEt.getText().toString().trim();
                    lastNameStr = lastNameEt.getText().toString().trim();
                    dobStr = dateofbirthEt.getText().toString().trim();
                    ageStr = ageEt.getText().toString().trim();
                    extectedDateStr = mExpecteConceiveDate.getText().toString().trim();
                    expectedDeliveryDateStr = mExpecteDeliveryDate.getText().toString().trim();
                    doctorName = doctorNameEt.getText().toString().trim();
                    doctorNumber = doctorNumberEt.getText().toString().trim();
                    if (!doctorNumber.equals("")) {
                        if (doctorNumber.length() < 8 || doctorNumber.length() > 12) {
                            Constant.setAlert(WomanHomeActivity.this, "Please enter correct number.");
                            return;
                        }
                    }
                    activeAndroidDBHelper.addMotherDetail(nameStr, lastNameStr, currentDate, dobStr, ageStr, extectedDateStr, expectedDeliveryDateStr, pregnancyMonth, doctorName, doctorNumber);
                    Constant.type = 1;
                    startActivity(new Intent(WomanHomeActivity.this, MainFragmentActivity.class));
                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.MOTHER_NAME, nameStr);
                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.MOTHER_DOB, dobStr);
                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.MOTHER_AGE, ageStr);
                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.EXPECTED_DATE, extectedDateStr);
                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.EXPECTED_DELIVERY_DATE, expectedDeliveryDateStr);
                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.DOCTOR_NAME, doctorName);
                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.DOCTOR_NUMBER, doctorNumber);

                    //save the task list to preference
                    SharedPreferences prefs = getSharedPreferences("list_file", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    try {
                        editor.putString("weight_list", ObjectSerializer.serialize(weightList));
                        editor.putString("systolic_list", ObjectSerializer.serialize(systolicBloodPressure));
                        editor.putString("diastolic_list", ObjectSerializer.serialize(diastolicBloodPressure));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();

                    PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.MOTHER_FIRST_TIME, "Yes");


                    finish();
                }
            } else {
                Constant.setAlert(WomanHomeActivity.this, "Enter valid date.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        i3 = (i3 == 31) ? 20 : i3;
        if (aDate) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(Constant.datePattern);
                Date selectedDate = sdf.parse(+i + "-" + i2 + "-" + i3);
                Calendar calendar = Calendar.getInstance();
                Date currentDate = sdf.parse(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
                if (selectedDate.after(currentDate)) {
                    Constant.setAlert(WomanHomeActivity.this, "Please select valid date.");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Period age = Constant.getDate(i, i2, i3);
            int Month = age.getMonths();
            int Year = age.getYears();
            ageStr = Year + "Y" + Month + "M";

            if (Year < 12) {
                Constant.setAlert(WomanHomeActivity.this, "Enter valid birth date.");
                return;
            } else if (Year == 0) {
                String m = (age.getMonths() >= 1) ? "Month" : "Months";
                ageEt.setText(age.getMonths() + m);

            } else if (Month == 0) {
                String m = (age.getYears() <= 1) ? "Year" : "Years";
                ageEt.setText(age.getYears() + " " + m);

            } else {
                String m = (Month >= 1) ? "Month" : "Months";
                String y = (Year <= 1) ? "Year" : "Years";
                ageEt.setText(Year + " " + y + "," + Month + " " + m);

            }
            dateofbirthEt.setText(date.trim().toString());
        } else if (bDate) {
            mExpecteConceiveDate.setText(date.toString().trim());
            PreferenceConnector.writeString(WomanHomeActivity.this, PreferenceConnector.START_DATE, date);
        } else if (DeliveryDate) {
            mExpecteDeliveryDate.setText(date.toString().trim());
        }
        aDate = false;
        bDate = false;
        DeliveryDate = false;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WomanHomeActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.push_infromright_anim, R.anim.push_out_to_left_anim);
        finish();
    }

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
}
