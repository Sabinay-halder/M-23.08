package com.widevision.pregnantwoman.baby;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.MainFragmentActivityBaby;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.BabyInfoTable;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BabyProfileUpdate extends HideKeyFragment implements DatePickerDialog.OnDateSetListener, Validator.ValidationListener {

    @Bind(R.id.back)
    LinearLayout backBttn;
    @NotEmpty(message = "Enter Name Of Baby.")
    @Bind(R.id.etfirstnamebaby)
    EditText firstnameET;
    @Bind(R.id.etlastnamebaby)
    EditText lastnameET;
    @NotEmpty(message = "Please Select Date of birth.")
    @Bind(R.id.etdateofbirthbaby)
    MaterialEditText dobET;
    @Bind(R.id.rggender)
    RadioGroup genderRG;
    @Bind(R.id.radiobtnboy)
    RadioButton boyRB;
    @Bind(R.id.radiobtngirl)
    RadioButton girlRB;

    @Bind(R.id.sbweightinfo)
    DiscreteSeekBar weightSB;
    @Bind(R.id.sbheightinfo)
    DiscreteSeekBar heightSB;
    @Bind(R.id.sbcircuminfo)
    DiscreteSeekBar circumSB;
    @Bind(R.id.weightTxt)
    TextView weightET;
    @Bind(R.id.heightTxt)
    TextView heightET;
    @Bind(R.id.circumferenceTxt)
    TextView circumET;

    @Bind(R.id.btnsavehomebaby)
    ImageView savebttn;
    @Bind(R.id.btncanclehomebaby)
    ImageView canclebttn;
    @Bind(R.id.rltxtview)
    RelativeLayout relativeLayout;


    private RadioButton genderbttn;
    private String name, firstname, lastname, date, weight, height, circum, gender;
    private float weigh;
    private int heigh, circu;
    private long babyid;

    private ActiveAndroidDBHelper helper;

    private Validator validator;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_activity_baby, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI(view.findViewById(R.id.babyhomeactivity));
        relativeLayout.setVisibility(View.GONE);
        backBttn.setVisibility(View.GONE);
        helper = ActiveAndroidDBHelper.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);


              /*Fetch Value From Database*/
        BabyInfoTable item = helper.getBaby("" + Constant.babyId);

        babyid = item.getId();
        firstname = item.name;
        lastname = item.lastName;
        date = item.dobbaby;
        gender = item.genderbaby;
        weigh = item.weightbaby;
        heigh = item.heightbaby;
        circu = item.circumbaby;
        firstnameET.setText(firstname);
        lastnameET.setText(lastname);
        dobET.setText(date);
        weightET.setText(String.valueOf(weigh));
        heightET.setText(String.valueOf(heigh));
        circumET.setText(String.valueOf(circu));

        weightSB.setProgress((int) weigh);
        heightSB.setProgress(heigh);
        circumSB.setProgress(circu);

        if (gender.equals("Boy")) {
            boyRB.setChecked(true);
        } else if (gender.equals("Girl")) {
            girlRB.setChecked(true);
        }

        /*Date picker*/

        dobET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    setDatePicker(dobET);
                }
            }
        });

        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(dobET);
            }
        });


        /*SeekBars*/
        weightSB.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                weightET.setText("" + i);
                return i;
            }
        });

        heightSB.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                heightET.setText("" + i);
                return i;
            }
        });

        circumSB.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int i) {
                circumET.setText("" + i);
                return i;
            }
        });
        /*Save Button onClick*/
        savebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        /*Cancle Button onClick*/
        canclebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainFragmentActivityBaby.fragmentManager.popBackStack();
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
        DatePickerViewAndroid datePicker = new DatePickerViewAndroid(getActivity(), dateToSet, "yyyy-MM-dd", new MyListener() {
            @Override
            public void onSet(String date) {
                switch (textView.getId()) {
                    case R.id.etdateofbirthbaby:
                        dateOfBirthValidation(date);
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
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePatternFullName);
            DateTime date = formatter.parseDateTime(newDate);
            if (date.isAfterNow()) {
                Constant.setAlert(getActivity(), "Please select valid date.");
                return;
            }
            dobET.setText(date.getYear() + "-" + date.getMonthOfYear() + "-" + date.getDayOfMonth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        String mount = String.valueOf(month + 1);
        String da = String.valueOf(day);

        if (mount.length() == 1) {
            mount = "0" + mount;
        }
        if (da.length() == 1) {
            da = "0" + da;
        }
        String datesave = year + "-" + mount + "-" + da;
        dobET.setText(datesave);
    }


    @Override
    public void onValidationSucceeded() {
        int selctid = genderRG.getCheckedRadioButtonId();
        genderbttn = (RadioButton) view.findViewById(selctid);

        firstname = firstnameET.getText().toString().trim();
        lastname = lastnameET.getText().toString();
        date = dobET.getText().toString().trim();
        gender = genderbttn.getText().toString().trim();
        weight = weightET.getText().toString().trim();
        height = heightET.getText().toString().trim();
        circum = circumET.getText().toString().trim();
        name = firstname + " " + lastname;
        weigh = Float.parseFloat(weight);
        heigh = Integer.parseInt(height);
        circu = Integer.parseInt(circum);
        setConfirmDialog(getActivity(), name, date, gender);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }

    /*Dialog box for confirmation*/
    public void setConfirmDialog(final Activity activity, String _name, String _dob, String _gender) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.baby_infoconfirmation_dialog);
        dialog.getWindow().setLayout(Constant.width - 10, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView nameTxt = (TextView) dialog.findViewById(R.id.name);
        TextView dobTxt = (TextView) dialog.findViewById(R.id.dob);
        TextView genderTxt = (TextView) dialog.findViewById(R.id.gender);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        ImageView okBttn = (ImageView) dialog.findViewById(R.id.confirm_yes);
        ImageView cancleBttn = (ImageView) dialog.findViewById(R.id.confirm_no);

        cancleBttn.setVisibility(View.VISIBLE);

        title.setText("UPDATE CONFIRMATION");
        nameTxt.setText(":- " + _name);
        dobTxt.setText(":- " + _dob);
        genderTxt.setText(":- " + _gender);
        /*Dialog Buttons*/
        okBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Constant.type = 2;
                Constant.babyName = name;
                Constant.babyDOB = date;
                Constant.gender = gender;
                      /*Update Data in database*/
                helper.babyInfoUpdate(babyid, firstname, lastname, date, gender, weigh, heigh, circu);
                MainFragmentActivityBaby.fragmentManager.popBackStack();
            }
        });
        cancleBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
