package com.widevision.pregnantwoman.mother;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.MotherRecordTable;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditMotherProfileActivity extends HideKeyFragment implements Validator.ValidationListener, DatePickerDialog.OnDateSetListener {

    @NotEmpty(message = "Name is required.")
    @Bind(R.id.nameEt)
    MaterialEditText nameEt;
    @NotEmpty(message = "Last Name is required.")
    @Bind(R.id.lastNameEt)
    MaterialEditText lastNameEt;
    @NotEmpty(message = "Please select Date of birth.")
    @Bind(R.id.dateofbirthEt)
    MaterialEditText dateofbirthEt;
    @NotEmpty(message = "Age is required.")
    @Bind(R.id.ageEt)
    MaterialEditText ageEt;
    @NotEmpty(message = "Please select Expected Date.")
    @Bind(R.id.expected_dateEt)
    MaterialEditText mExpecteConceiveDate;
    @NotEmpty(message = "Please select Expected Date.")
    @Bind(R.id.delivery_dateEt)
    EditText mExpecteDeliveryDate;
    @Bind(R.id.doctorNameEt)
    MaterialEditText doctorNameEt;
    @Bind(R.id.doctorNumberEt)
    MaterialEditText doctorNumberEt;
    @Bind(R.id.saveBt)
    ImageView mSaveBtn;
    @Bind(R.id.cancelBt)
    ImageView mCancelBtn;
    @Bind(R.id.twin)
    CheckBox mTwinsChek;
    @Bind(R.id.title)
    RelativeLayout mTitleLayout;

    private boolean aDate = false, bDate = false, DeliveryDate = false;
    private String doctorName = "", doctorNumber = "", nameStr = "", lastNameStr = "", dobStr = "", ageStr = "", extectedDateStr = "", pregnancyMonth = "", twinsStr = "", expectedDeliveryDateStr = "";
    private ActiveAndroidDBHelper helper;
    private List<MotherRecordTable> mList = new ArrayList<>();
    private Validator validator;
    private long motherId;

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Constant.hideSoftKeyboard(getActivity());
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_activity_women, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);

        mTitleLayout.setVisibility(View.GONE);
        helper = ActiveAndroidDBHelper.getInstance();
        mList.addAll(helper.viewMother());
        setData();
        validator = new Validator(this);
        validator.setValidationListener(this);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
        mTwinsChek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                twinsStr = b ? "Yes" : "No";
                PreferenceConnector.writeString(getActivity(), PreferenceConnector.TWINS, twinsStr);
            }
        });


        dateofbirthEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                aDate = b;
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
                bDate = b;
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
                DeliveryDate = b;
                if (b) {
                    setDatePicker(mExpecteDeliveryDate);
                }
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragmentActivity.fragmentManager.popBackStack();
            }
        });
        setupUI(view);
        return view;
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
        DatePickerViewAndroid datePicker = new DatePickerViewAndroid(getActivity(), dateToSet, "yyyy-MM-dd", new MyListener() {
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
                            Constant.setAlert(getActivity(), "Select valid date.");
                            return;
                        }
                        mExpecteConceiveDate.setText(d);
                        PreferenceConnector.writeString(getActivity(), PreferenceConnector.START_DATE, d);
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
                                    Constant.setAlert(getActivity(), "Select valid date.");
                                    return;
                                } else {
                                    mExpecteDeliveryDate.setText(selectedDate1.getYear() + "-" + selectedDate1.getMonthOfYear() + "-" + selectedDate1.getDayOfMonth());
                                }
                            } else {
                                Constant.setAlert(getActivity(), "Select valid date.");
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
                Constant.setAlert(getActivity(), "Please select valid date.");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePatternFullName);
            LocalDate date = formatter.parseLocalDate(newDate);

            Period age = Constant.getDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
            int Month = age.getMonths();
            int Year = age.getYears();
            ageStr = Year + "Y" + Month + "M";

            if (Year < 13) {
                Constant.setAlert(getActivity(), "Enter valid birth date.");
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
        Constant.setAlert(getActivity(), errors.get(0).getCollatedErrorMessage(getActivity()));
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
                    Constant.setAlert(getActivity(), "The difference between Conceive date & Delivery date should be greater than 8 Months.");
                } else {

                    DateTime dateOfBirth = formatter.parseDateTime(dateofbirthEt.getText().toString().trim());

                    Period p = Constant.getDuration(dateOfBirth, conceciveDate);
                    if (p.getYears() < 14) {
                        Constant.setAlert(getActivity(), "select vaid 'Concecive Date'");
                        return;
                    }
                    pregnancyMonth = "" + period;
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.IS_FIRSTTIME_BABY, "No");
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
                    activeAndroidDBHelper.updateMotherDetail(motherId, nameStr, lastNameStr, dobStr, ageStr, extectedDateStr, expectedDeliveryDateStr, pregnancyMonth);
                    startActivity(new Intent(getActivity(), MainFragmentActivity.class));
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.MOTHER_NAME, nameStr + " " + lastNameStr);
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.MOTHER_DOB, dobStr);
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.MOTHER_AGE, ageStr);
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.EXPECTED_DATE, extectedDateStr);
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.EXPECTED_DELIVERY_DATE, expectedDeliveryDateStr);
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.DOCTOR_NAME, doctorName);
                    PreferenceConnector.writeString(getActivity(), PreferenceConnector.DOCTOR_NUMBER, doctorNumber);
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainFragmentActivity.class));
                    Constant.tagForEditProfile = 1;
                }
            } else {
                Constant.setAlert(getActivity(), "Enter valid dates.");
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
                    Constant.setAlert(getActivity(), "please select valid date.");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dateofbirthEt.setText(date.trim().toString());
            Period age = Constant.getDate(i, i2, i3);
            int Month = age.getMonths();
            int Year = age.getYears();
            ageStr = Year + "Y" + Month + "M";
            if (Year == 0) {
                String m = (age.getMonths() >= 1) ? "Month" : "Months";
                ageEt.setText(age.getMonths() + m);
                return;
            } else if (Month == 0) {
                String m = (age.getYears() <= 1) ? "Year" : "Years";
                ageEt.setText(age.getYears() + "," + m);
                return;
            } else {
                String m = (Month >= 1) ? "Month" : "Months";
                String y = (Year <= 1) ? "Year" : "Years";
                ageEt.setText(Year + " " + y + "," + Month + " " + m);
                return;
            }
        } else if (bDate) {


            mExpecteConceiveDate.setText(date.toString().trim());
            PreferenceConnector.writeString(getActivity(), PreferenceConnector.START_DATE, date);
        } else if (DeliveryDate) {

            mExpecteDeliveryDate.setText(date.toString().trim());

        }
        aDate = false;
        bDate = false;
        DeliveryDate = false;
    }


    void setData() {
        MotherRecordTable item = mList.get(0);
        nameEt.setText(item.name);
        lastNameEt.setText(item.lastName);
        dateofbirthEt.setText(item.dob);
        ageEt.setText(item.age);
        mExpecteConceiveDate.setText(item.expecteConceiveDate);
        mExpecteDeliveryDate.setText(item.expecteDeliveryDate);
        pregnancyMonth = item.babbyMonth;
        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.TWINS, "No").trim().equals("Yes")) {
            mTwinsChek.setChecked(true);
        } else {
            mTwinsChek.setChecked(false);
        }
        motherId = item.getId();
        doctorNameEt.setText(PreferenceConnector.readString(getActivity(), PreferenceConnector.DOCTOR_NAME, ""));
        doctorNumberEt.setText(PreferenceConnector.readString(getActivity(), PreferenceConnector.DOCTOR_NUMBER, ""));
    }
}
