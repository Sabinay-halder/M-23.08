package com.widevision.pregnantwoman.baby;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.HomeActivity;
import com.widevision.pregnantwoman.MainFragmentActivityBaby;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.BabyInfoTable;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.model.HideKeyActivity;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mercury-two on 13/8/15.
 */
public class BabyHomeActivity extends HideKeyActivity implements DatePickerDialog.OnDateSetListener, Validator.ValidationListener {


    @Bind(R.id.back)
    LinearLayout backBttn;
    @NotEmpty(message = "Enter name of baby.")
    @Bind(R.id.etfirstnamebaby)
    EditText firstnameET;
    @Bind(R.id.etlastnamebaby)
    EditText lastnameET;
    @NotEmpty(message = "Please select date of birth.")
    @Bind(R.id.etdateofbirthbaby)
    MaterialEditText dobET;
    @Bind(R.id.rggender)
    RadioGroup genderRG;

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

    private RadioButton genderbttn;
    private String name, firstname, lastname, date, weight, height, circum, gender;
    private float weigh;
    private int heigh, circu;
    private boolean aDate = false, bDate = false;

    private ActiveAndroidDBHelper helper;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_baby);
        ButterKnife.bind(this);
        setupUI(findViewById(R.id.babyhomeactivity));

        helper = ActiveAndroidDBHelper.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);

        /*back button*/
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        /*Date picker*/
        dobET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                aDate = b;
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
                onBackPressed();
            }

        });
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
        dobET.setText(datesave);
    }


    @Override
    public void onValidationSucceeded() {
        int selctid = genderRG.getCheckedRadioButtonId();
        genderbttn = (RadioButton) findViewById(selctid);

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

        setConfirmDialog(BabyHomeActivity.this, name, date, gender);
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
        DatePickerViewAndroid datePicker = new DatePickerViewAndroid(BabyHomeActivity.this, dateToSet, "yyyy-MM-dd", new MyListener() {
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
                Constant.setAlert(BabyHomeActivity.this, "Please select valid date.");
                return;
            }
            dobET.setText(date.getYear() + "-" + date.getMonthOfYear() + "-" + date.getDayOfMonth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(BabyHomeActivity.this, errors.get(0).getCollatedErrorMessage(BabyHomeActivity.this));
    }

    /*Dialog box for confirmation*/
    public void setConfirmDialog(Activity activity, String _name, String _dob, String _gender) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.baby_infoconfirmation_dialog);
        dialog.getWindow().setLayout(Constant.width - 20, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView nameTxt = (TextView) dialog.findViewById(R.id.name);
        TextView dobTxt = (TextView) dialog.findViewById(R.id.dob);
        TextView genderTxt = (TextView) dialog.findViewById(R.id.gender);
        ImageView okBttn = (ImageView) dialog.findViewById(R.id.confirm_yes);
        ImageView cancleBttn = (ImageView) dialog.findViewById(R.id.confirm_no);

        cancleBttn.setVisibility(View.VISIBLE);


        title.setText("CONFIRMATION");
        nameTxt.setText(":- " + _name);
        dobTxt.setText(":- " + _dob);
        genderTxt.setText(":- " + _gender);
        /*Dialog Buttons*/
        okBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                PreferenceConnector.writeString(BabyHomeActivity.this, PreferenceConnector.IS_FIRSTTIME_BABY, "No");

                      /*Insert Data in database*/
                helper.addBabyInfo(firstname, lastname, date, gender, weigh, heigh, circu);
                Constant.type = 2;

                Intent ok = new Intent(getApplicationContext(), MainFragmentActivityBaby.class);

                Constant.babyName = name;
                Constant.babyDOB = date;
                Constant.gender = gender;
                    /*Fetch Value From Database*/
                List<BabyInfoTable> list = helper.babyDetail();
                if (list.size() != 0) {
                    BabyInfoTable item = list.get(list.size() - 1);
                    long babyId = item.getId();
                    helper.setVaccineStatusTable("" + babyId, date);
                    Constant.babyId = babyId;

                }

                startActivity(ok);
                finish();

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BabyHomeActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.push_infromright_anim, R.anim.push_out_to_left_anim);
        finish();
    }
}


