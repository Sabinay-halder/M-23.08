package com.widevision.pregnantwoman.baby;

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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.widevision.pregnantwoman.MainFragmentActivityBaby;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BabyAppointmentRequest extends HideKeyFragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, Validator.ValidationListener {

    @NotEmpty(message = "Name is required.")
    @Bind(R.id.nameEdt)
    TextView nameET;
    @NotEmpty(message = "Doctor name is required.")
    @Bind(R.id.doctorNameEdt)
    EditText mDoctorNameEdt;
    @NotEmpty(message = "Enter appointment time.")
    @Bind(R.id.timeEdt)
    EditText mTimeEdt;
    @NotEmpty(message = "Enter appointment date.")
    @Bind(R.id.dateEdt)
    EditText dateET;
    @Bind(R.id.saveBtn)
    ImageView mSaveBtn;
    @Bind(R.id.cancelBtn)
    ImageView mCancelBtn;

    private ActiveAndroidDBHelper helper;
    private Validator validator;
    private String name = "", babyDOB = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_appointment_activity, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);

        helper = ActiveAndroidDBHelper.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);


        /*Fetch Value From Dialog List*/

        nameET.setText(Constant.babyName);
        babyDOB = Constant.babyDOB;


         /*Date picker*/
        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    setDatePicker(dateET);
                }
            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(dateET);
            }
        });

        /*Start Time */
        mTimeEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
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
                    TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(BabyAppointmentRequest.this, h, m, false);
                    timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
                }
            }
        });

        mTimeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(BabyAppointmentRequest.this, h, m, false);
                timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });

        /*Save Button*/
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

         /*Cancle Button*/
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragmentActivityBaby.fragmentManager.popBackStack();
            }
        });
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
                DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePatternFullName);
                DateTime dateSelected = formatter.parseDateTime(newDate);
                DateTimeFormatter formatter1 = DateTimeFormat.forPattern(Constant.datePattern);
                DateTime s = formatter.parseDateTime(Constant.getDate()).minusDays(1);
                if (dateSelected.isAfter(s)) {
                    String d = dateSelected.getYear() + "-" + dateSelected.getMonthOfYear() + "-" + dateSelected.getDayOfMonth();
                    textView.setText(d);
                } else {
                    Constant.setAlert(getActivity(), "Select valid date.");
                }
            }
        });
        datePicker.setPicker();
        datePicker.show();
    }


    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int min) {
        String h = String.valueOf(hour);
        String m = String.valueOf(min);
        if (h.length() == 1) {
            h = "0" + h;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        mTimeEdt.setText("" + h + ":" + m);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String mount = String.valueOf(month + 1);
        String da = String.valueOf(day);

        if (mount.length() < 2) {
            mount = "0" + mount;
        }
        if (da.length() < 2) {
            da = "0" + da;
        }
        String datesave = year + "-" + mount + "-" + da;
        dateET.setText(datesave);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(getActivity(), errors.get(0).getCollatedErrorMessage(getActivity()));
    }

    @Override
    public void onValidationSucceeded() {
        String name = nameET.getText().toString().trim();
        String doctorName = mDoctorNameEdt.getText().toString().trim();
        String time = mTimeEdt.getText().toString().trim();
        String date = dateET.getText().toString().trim();
        helper.addAppointment(name, doctorName, date, time, Constant.getCurrentTime(), "BABY");
        Constant.babysaveAlert(getActivity(), "Appointment added successfully.");
    }
}
