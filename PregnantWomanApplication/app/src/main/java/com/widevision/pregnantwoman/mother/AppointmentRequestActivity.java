package com.widevision.pregnantwoman.mother;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by newtrainee on 4/8/15.
 */
public class AppointmentRequestActivity extends HideKeyFragment implements TimePickerDialog.OnTimeSetListener, Validator.ValidationListener {


    @Bind(R.id.nameEdt)
    TextView mNameEdt;
    @NotEmpty(message = "Doctor name is required.")
    @Bind(R.id.doctorNameEdt)
    EditText mDoctorNameEdt;
    @NotEmpty(message = "Enter appointment Time.")
    @Bind(R.id.timeEdt)
    EditText mTimeEdt;
    @NotEmpty(message = "Enter appointment Date.")
    @Bind(R.id.dateEdt)
    EditText mDateEdt;
    @Bind(R.id.saveBtn)
    ImageView mSaveBtn;
 /*   @Bind(R.id.cancelBtn)
    ImageView mCancelBtn;*/

    private ActiveAndroidDBHelper helper;
    private Validator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_activity, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);

        helper = ActiveAndroidDBHelper.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);
        String motherNameStr = PreferenceConnector.readString(getActivity(), PreferenceConnector.MOTHER_NAME, "");
        mNameEdt.setText(motherNameStr);

        mDoctorNameEdt.setText(PreferenceConnector.readString(getActivity(), PreferenceConnector.DOCTOR_NAME, ""));


        mTimeEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setTimePicker();
                }
            }
        });
        mTimeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimePicker();
            }
        });
        mDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setDatePicker();
            }
        });
        mDateEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                                      setDatePicker();
                }
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
              return view;
    }


    void setTimePicker() {
        Calendar calendartime = Calendar.getInstance();
        int h = 0, m = 0;
        if (mTimeEdt.getText().toString().trim().equals("")) {
            h = calendartime.get(Calendar.HOUR_OF_DAY);
            m = calendartime.get(Calendar.MINUTE);
        } else {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.timePattern);
            DateTime selectedTime = formatter.parseDateTime(mTimeEdt.getText().toString().trim());
            h = selectedTime.getHourOfDay();
            m = selectedTime.getMinuteOfHour();
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(AppointmentRequestActivity.this, h, m, false);
        timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");


    }

    void setDatePicker() {

        String dateToSet = "";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        if (mDateEdt.getText().toString().trim().equals("")) {
            dateToSet = Constant.getDate();
        } else {
            DateTime selectedDate = formatter.parseDateTime(mDateEdt.getText().toString().trim());
            int year = selectedDate.getYear();
            int month = selectedDate.getMonthOfYear();
            int day = selectedDate.getDayOfMonth();
            dateToSet = year + "-" + month + "-" + day;
        }

        DatePickerViewAndroid datePicker = new DatePickerViewAndroid(getActivity(), dateToSet, "yyyy-MM-dd", new MyListener() {
            @Override
            public void onSet(String date) {
                String a[] = date.split("-");
                String newDate = a[2] + "-" + a[1] + "-" + a[0];
                DateTimeFormatter formatter1 = DateTimeFormat.forPattern(Constant.datePatternFullName);
                DateTimeFormatter formatter2 = DateTimeFormat.forPattern(Constant.datePattern);
                DateTime selectedDate1 = formatter1.parseDateTime(newDate);
                DateTime current = formatter2.parseDateTime(Constant.getDate());
                if (selectedDate1.isAfter(current.minusDays(1))) {
                    mDateEdt.setText(selectedDate1.getYear() + "-" + selectedDate1.getMonthOfYear() + "-" + selectedDate1.getDayOfMonth());
                } else {
                    Constant.setAlert(getActivity(), "Select valid date.");
                }
            }
        });
        datePicker.setPicker();
        datePicker.show();
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {
        String a = "" + i2;
        String c = "" + i;

        if (a.length() == 1) {
            a = "0" + a;
        }
        if (c.length() == 1) {
            c = "0" + c;
        }


        mTimeEdt.setText("" + c + ":" + a);
    }



    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(getActivity(), errors.get(0).getCollatedErrorMessage(getActivity()));
    }

    @Override
    public void onValidationSucceeded() {
        String name = mNameEdt.getText().toString().trim();
        String doctorName = mDoctorNameEdt.getText().toString().trim();
        String time = mTimeEdt.getText().toString().trim();
        String date = mDateEdt.getText().toString().trim();

        String a[] = time.split(":");

        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.dateTimePattern);
            DateTime dt = formatter.parseDateTime(date + " " + a[0] + ":" + a[1]);
            if (dt.isBeforeNow()) {
                Constant.setAlert(getActivity(), "Please select valid time.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        helper.addAppointment(name, doctorName, date, time, Constant.getCurrentTime(), "MOTHER");
        Constant.tagForNoteFragment = 2;
        MainFragmentActivity.fragmentManager.popBackStack();
        Constant.setAlert(getActivity(), "Appointment added successfully.");
    }
}
