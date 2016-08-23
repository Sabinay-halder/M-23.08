package com.widevision.pregnantwoman.baby;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.MainFragmentActivityBaby;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.Utils;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mercury-two on 17/8/15.
 */
public class BabyHeightWeight extends HideKeyFragment implements DatePickerDialog.OnDateSetListener, Validator.ValidationListener {

    @Bind(R.id.bttnoteheiwei)
    ImageView noteBTTN;
    @Bind(R.id.etnameheiwei)
    TextView nameET;

    @NotEmpty(message = "Date Required.")
    @Bind(R.id.etdatemeasureheiwei)
    EditText dateMeasureEt;
    @NotEmpty(message = "Weight Required.")
    @Bind(R.id.etweightheiwei)
    TextView weightET;
    @NotEmpty(message = "Height Required.")
    @Bind(R.id.etheightheiwei)
    TextView heightET;
    @Bind(R.id.sbweightheiwei)
    DiscreteSeekBar weightSB;
    @Bind(R.id.sbheightheiwei)
    DiscreteSeekBar heightSB;
    @Bind(R.id.bttnsaveheiwei)
    ImageView saveBTTN;
    @Bind(R.id.bttncancleheiwei)
    ImageView cancelBTTN;

    private ActiveAndroidDBHelper helper;
    private Validator validator;

    private boolean aDate = false, bDate = false;
    private String name, babyDOB, datemeasure, weight, height, notettitle, note;
    private float weigh;
    private int heig;
    private long babyid;
    private ShowcaseView showcaseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_height_weightfragment, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);

        helper = ActiveAndroidDBHelper.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);


        nameET.setText(Constant.babyName);

        /*Fetch Value From Dialog List*/

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
                    transaction.replace(R.id.baby_heightWeight, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

         /*Date picker*/
        dateMeasureEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                aDate = b;
                if (b) {
                    setDatePicker(dateMeasureEt);
                }
            }
        });

        dateMeasureEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(dateMeasureEt);
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
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.FOODINTAKE_NOTES, R.id.bttnoteheiwei, R.layout.ok_button, "", getActivity().getResources().getString(R.string.baby_add_note_btn), R.style.CustomShowcaseTheme2, true, listener);
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
                DateTime s = formatter.parseDateTime(newDate).minusDays(1);
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
        dateMeasureEt.setText(datesave);
    }

    /*Validation Set*/
    @Override
    public void onValidationSucceeded() {

        name = nameET.getText().toString().trim();
        datemeasure = dateMeasureEt.getText().toString().trim();
        weight = weightET.getText().toString().trim();
        height = heightET.getText().toString().trim();

        weigh = Float.parseFloat(weight);
        heig = Integer.parseInt(height);

        //  Log.e("HeiWeibaby", babyid + " " + name + " " + date + " " + datemeasure + " " + gender + " " + weight + " " + height + " " + notettitle + " " + note);

        helper.addBabyHeiWei(babyid, datemeasure, Constant.gender, weigh, heig, MainFragmentActivityBaby.noteDescription, MainFragmentActivityBaby.noteTitle);
        Constant.babysaveAlert(getActivity(), "Weight and Height added successfully.");
        MainFragmentActivityBaby.noteDescription = "";
        MainFragmentActivityBaby.noteTitle = "";

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
        dialog.getWindow().setLayout(Constant.width - 10, ViewGroup.LayoutParams.FILL_PARENT);
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
                    Constant.setAlert(getActivity(), "All fields are required.");
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
