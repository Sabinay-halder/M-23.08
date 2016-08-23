package com.widevision.pregnantwoman.baby;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
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
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.Utils;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mercury-two on 18/8/15.
 */
public class BabySleepRecord extends HideKeyFragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, Validator.ValidationListener {

    @Bind(R.id.bttnotesleep)
    ImageView noteBTTN;
    @Bind(R.id.etnamesleep)
    TextView nameET;
    @NotEmpty(message = "Date Required.")
    @Bind(R.id.etdateofbirthsleep)
    EditText dateET;
    @NotEmpty(message = "Enter Start Time.")
    @Bind(R.id.etstarttimesleep)
    EditText startTimeET;
    @NotEmpty(message = "Enter End Time.")
    @Bind(R.id.etendtimesleep)
    EditText endTimeET;
    @Bind(R.id.bttnsavesleep)
    ImageView saveBTTN;
    @Bind(R.id.bttncancelsleep)
    ImageView cancelBTTN;

    private ActiveAndroidDBHelper helper;
    private Validator validator;

    private boolean aDate = false, bDate = false;
    private String startTime = null;
    private String endTime = null;
    private String name = "", babyDOB = "", startSlTime = "", endSlTime = "", notettitle = "", note = "";
    private long babyid;

    private int time = 0;
    private ShowcaseView showcaseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_sleep_record, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);

        helper = ActiveAndroidDBHelper.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);

        nameET.setText(Constant.babyName);

        babyDOB = Constant.babyDOB;
        babyid = Constant.babyId;
        /*Note Button*/
        noteBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        showcaseView.hide();
                    }
                    AddNoteFragmentBaby myf = new AddNoteFragmentBaby();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right, R.anim.slide_in_from_right, R.anim.slide_out_to_right);
                    transaction.replace(R.id.baby_sleeprecord, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

         /*Date picker*/
        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                aDate = b;
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

        startTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    time = 1;
                    Calendar calendartime = Calendar.getInstance();
                    int h = 0, m = 0;
                    if (startTimeET.getText().toString().trim().equals("")) {
                        h = calendartime.get(Calendar.HOUR_OF_DAY);
                        m = calendartime.get(Calendar.MINUTE);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.timePattern);
                        DateTime selectedTime = formatter.parseDateTime(startTimeET.getText().toString().trim());
                        h = selectedTime.getHourOfDay();
                        m = selectedTime.getMinuteOfHour();
                    }
                    TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(BabySleepRecord.this, h, m, false);
                    timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
                }
            }
        });

        startTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = 1;
                int h = 0, m = 0;
                Calendar calendartime = Calendar.getInstance();
                if (startTimeET.getText().toString().trim().equals("")) {
                    h = calendartime.get(Calendar.HOUR_OF_DAY);
                    m = calendartime.get(Calendar.MINUTE);
                } else {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.timePattern);
                    DateTime selectedTime = formatter.parseDateTime(startTimeET.getText().toString().trim());
                    h = selectedTime.getHourOfDay();
                    m = selectedTime.getMinuteOfHour();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(BabySleepRecord.this, h, m, false);
                timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });

        /*End Time*/
        endTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    time = 2;
                    int h = 0, m = 0;
                    Calendar calendartime = Calendar.getInstance();
                    if (endTimeET.getText().toString().trim().equals("")) {
                        h = calendartime.get(Calendar.HOUR_OF_DAY);
                        m = calendartime.get(Calendar.MINUTE);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.timePattern);
                        DateTime selectedTime = formatter.parseDateTime(endTimeET.getText().toString().trim());
                        h = selectedTime.getHourOfDay();
                        m = selectedTime.getMinuteOfHour();
                    }
                    TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(BabySleepRecord.this, h, m, false);
                    timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
                }
            }
        });

        endTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = 2;
                int h = 0, m = 0;
                Calendar calendartime = Calendar.getInstance();
                if (endTimeET.getText().toString().trim().equals("")) {
                    h = calendartime.get(Calendar.HOUR_OF_DAY);
                    m = calendartime.get(Calendar.MINUTE);
                } else {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.timePattern);
                    DateTime selectedTime = formatter.parseDateTime(endTimeET.getText().toString().trim());
                    h = selectedTime.getHourOfDay();
                    m = selectedTime.getMinuteOfHour();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(BabySleepRecord.this, h, m, false);
                timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });

        /*Save Button*/
        saveBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        /*Cancle Button*/
        cancelBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragmentActivityBaby.noteDescription = "";
                MainFragmentActivityBaby.noteTitle = "";
                MainFragmentActivityBaby.fragmentManager.popBackStack();
            }
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE_NOTES, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.FOODINTAKE_NOTES, R.id.bttnotesleep, R.layout.ok_button, "", getActivity().getResources().getString(R.string.baby_add_note_btn), R.style.CustomShowcaseTheme2, true, listener);
        }
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
                DateTime dt = formatter1.parseDateTime(Constant.getDate());
                DateTime birthDate = formatter1.parseDateTime(babyDOB);
                if (dateSelected.isBefore(dt.plus(1)) && dateSelected.isAfter(birthDate.minusDays(1))) {
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

    /*Date Set*/
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

    /*Time Set*/
    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {


        String h = "" + i;
        String m = "" + i2;

        if (!(h.length() > 1)) {
            h = "0" + h;
        }
        if (!(m.length() > 1)) {
            m = "0" + m;
        }

        if (time == 1) {
            startTime = h + ":" + m;
            startTimeET.setText(startTime);
        } else if (time == 2) {
            endTime = h + ":" + m;
            endTimeET.setText(endTime);
        }

    }

    /*Validation Set*/
    @Override
    public void onValidationSucceeded() {
        name = nameET.getText().toString().trim();
        babyDOB = dateET.getText().toString().trim();
        startSlTime = startTimeET.getText().toString().trim();
        endSlTime = endTimeET.getText().toString().trim();

        Log.e("sleepbaby", babyid + " " + name + " " + babyDOB + " " + startSlTime + " " + endSlTime + " " + notettitle + " " + note);

        DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.timePattern);
        DateTime startTime = formatter.parseDateTime(startSlTime);
        DateTime endTime = formatter.parseDateTime(endSlTime);
        Period period = new Period(startTime, endTime);
        int hours = period.getHours();
        if (hours <= 0) {
            if (hours < 0) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(Constant.datePattern);
                DateTime dateObject = dateTimeFormatter.parseDateTime(babyDOB);
                DateTime newdate = dateObject.plusDays(1);
                String newdateStr = newdate.toString(Constant.datePattern);
                DateTimeFormatter dateTimeFormatter1 = DateTimeFormat.forPattern(Constant.dateTimePattern);
                DateTime newDateTime = dateTimeFormatter1.parseDateTime(babyDOB + " " + startSlTime);
                DateTime newDateTime2 = dateTimeFormatter1.parseDateTime(newdateStr + " " + endSlTime);
                Period period1 = new Period(newDateTime, newDateTime2);
                int hours1 = period1.getHours();
                if (hours1 <= 0) {
                    Constant.babysaveAlert(getActivity(), "Insert correct time.");
                } else {
                    helper.addBabySleepRecord(babyid, babyDOB, startSlTime, endSlTime, MainFragmentActivityBaby.noteTitle, MainFragmentActivityBaby.noteDescription, hours1);
                    Constant.babysaveAlert(getActivity(), "Sleeping time added successfully.");
                    MainFragmentActivityBaby.noteDescription = "";
                    MainFragmentActivityBaby.noteTitle = "";
                }
            } else {
                Constant.babysaveAlert(getActivity(), "Insert correct time.");
            }
        } else {
            helper.addBabySleepRecord(babyid, babyDOB, startSlTime, endSlTime, MainFragmentActivityBaby.noteTitle, MainFragmentActivityBaby.noteDescription, hours);
            Constant.babysaveAlert(getActivity(), "Sleeping time added successfully.");
            MainFragmentActivityBaby.noteDescription = "";
            MainFragmentActivityBaby.noteTitle = "";
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(getActivity(), errors.get(0).getCollatedErrorMessage(getActivity()));
    }

    /*Dialog box for confirmation*/
    public void setNoteDialog(final Activity activity, String msg) {

        final Dialog dialog = new Dialog(activity, R.style.DialogAnimationRightToLeft);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_note_activity_baby);
        dialog.getWindow().setLayout(Constant.width - 10, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setupUI(getView());
        ImageView savebttn = (ImageView) dialog.findViewById(R.id.saveBt);
        ImageView cancelBt = (ImageView) dialog.findViewById(R.id.cancelBt);
        final EditText title = (EditText) dialog.findViewById(R.id.titleEdt);
        final EditText noteet = (EditText) dialog.findViewById(R.id.noteEdt);

        /*Dialog Buttons*/
        savebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                note = noteet.getText().toString();
                notettitle = title.getText().toString();

                if (note.trim().equals("") || notettitle.trim().equals("")) {
                    Toast.makeText(getActivity(), "All fields are required. ", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    dialog.dismiss();
                }
            }
        });
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
}
