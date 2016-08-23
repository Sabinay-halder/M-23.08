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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.widevision.pregnantwoman.kankan.wheel.widget.view.PickerView;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BabyFoodIntake extends HideKeyFragment implements Validator.ValidationListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.bttnotefoodintake)
    ImageView noteBTTN;

    @Bind(R.id.etnamefoodin)
    TextView nameET;
    @NotEmpty(message = "Date Required.")
    @Bind(R.id.etdateofbirthfoodin)
    EditText dateET;
    @NotEmpty(message = "Time Required.")
    @Bind(R.id.etintaketime)
    EditText intakeTimeET;

    @Bind(R.id.spintakefoodin)
    TextView foodInSP;
    @NotEmpty(message = "Other Intake Required.")
    @Bind(R.id.etotherintake)
    EditText otherintake;
    @NotEmpty(message = "Quantity Required.")
    @Bind(R.id.etquantityfoodin)
    EditText quantityET;
    @Bind(R.id.spquantity)
    TextView quantitySP;
    @Bind(R.id.bttnsave)
    ImageView saveBTTN;
    @Bind(R.id.bttncancel)
    ImageView cancelBTTN;

    ArrayAdapter<String> quantityAdapter;
    private ActiveAndroidDBHelper helper;
    private Validator validator;
    private boolean otherin = false, aDate = false, bDate = false;
    private String name = "", babyDOB = "", quantity = "", notettitle = "", note = "", unit = "", other = "", foodintake = "", intakeTime = "";
    private Long babyid;
    private float quantint;
    private Animation animation;
    private String fullquantity[] = {"Mililiter", "Cup", "Tablespoon(Tbsp)", "Gram", "Ounce", "Slice"};
    private String milli[] = {"Mililiter"};
    private String quantitys[] = {"Gram", "Ounce"};
    private String liquid[] = {"Milliliter", "Cup", "Tablespoon(Tbsp)"};
    private int selected_value = 0;
    private int selectedArray = 1;
    String[] stockArr;
    private ShowcaseView showcaseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_food_intakefragactiv, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);
        helper = ActiveAndroidDBHelper.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);
        babyid = Constant.babyId;

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_right);

        nameET.setText(Constant.babyName);
        babyDOB = Constant.babyDOB;
         /*Note Button*/
        noteBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  noteBTTN.startAnimation(animation);
                //    setNoteDialog(getActivity(), "");
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        showcaseView.hide();
                    }
                    AddNoteFragmentBaby myf = new AddNoteFragmentBaby();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right, R.anim.slide_in_from_right, R.anim.slide_out_to_right);
                    transaction.replace(R.id.parent_foodIntake, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        otherintake.setVisibility(View.GONE);
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

        /*Time Intake*/
        intakeTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Calendar calendartime = Calendar.getInstance();
                    int h = 0, m = 0;
                    if (intakeTimeET.getText().toString().trim().equals("")) {
                        h = calendartime.get(Calendar.HOUR_OF_DAY);
                        m = calendartime.get(Calendar.MINUTE);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.timePattern);
                        DateTime selectedTime = formatter.parseDateTime(intakeTimeET.getText().toString().trim());
                        h = selectedTime.getHourOfDay();
                        m = selectedTime.getMinuteOfHour();
                    }
                    TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(BabyFoodIntake.this, h, m, false);
                    timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
                }
            }
        });
        intakeTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendartime = Calendar.getInstance();
                int h = 0, m = 0;
                if (intakeTimeET.getText().toString().trim().equals("")) {
                    h = calendartime.get(Calendar.HOUR_OF_DAY);
                    m = calendartime.get(Calendar.MINUTE);
                } else {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.timePattern);
                    DateTime selectedTime = formatter.parseDateTime(intakeTimeET.getText().toString().trim());
                    h = selectedTime.getHourOfDay();
                    m = selectedTime.getMinuteOfHour();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog().newInstance(BabyFoodIntake.this, h, m, false);
                timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });


        /*Intake Spinner */
        /*Fetch Value For spinner*/
        final ArrayList<String> foodlist = helper.foodIntakeCategList();
        foodInSP.setText(foodlist.get(0));
        stockArr = new String[foodlist.size()];
        stockArr = foodlist.toArray(stockArr);
        foodInSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerView pickerView = new PickerView(getActivity(), stockArr, 0, new MyListener() {
                    @Override
                    public void onSet(String cat) {
                        setValue(cat);
                    }
                });
                pickerView.setPicker();
            }
        });

        quantitySP.setText(liquid[0]);
        quantitySP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (selectedArray) {
                    case 1:
                        setPicker(liquid);
                        break;
                    case 2:
                        setPicker(milli);
                        break;
                    case 3:
                        setPicker(quantitys);
                        break;
                    case 4:
                        setPicker(fullquantity);
                        break;
                    default:
                        break;
                }
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
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.FOODINTAKE_NOTES, R.id.bttnotefoodintake, R.layout.ok_button, "", getActivity().getResources().getString(R.string.baby_add_note_btn), R.style.CustomShowcaseTheme2, true, listener);
        }
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

    void setPicker(String[] list) {
        PickerView pickerView = new PickerView(getActivity(), list, 0, new MyListener() {
            @Override
            public void onSet(String cat) {
                quantitySP.setText(cat);
            }
        });
        pickerView.setPicker();
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
        String h = String.valueOf(i);
        String m = String.valueOf(i2);

        if (h.length() < 2) {
            h = "0" + h;
        }
        if (m.length() < 2) {
            m = "0" + m;
        }

        intakeTime = h + ":" + m;
        intakeTimeET.setText(intakeTime);
    }

    /*Validation Set*/
    @Override
    public void onValidationSucceeded() {
        name = nameET.getText().toString().trim();
        babyDOB = dateET.getText().toString().trim();
        intakeTime = intakeTimeET.getText().toString().trim();
        /*Condition To take Value Either from Spinner or from Edittext*/
        if (otherin) {
            foodintake = otherintake.getText().toString().trim();
            other = otherintake.getText().toString().trim();
        } else {
            foodintake = foodInSP.getText().toString().trim();
        }
        String food = foodintake;
        quantity = quantityET.getText().toString().trim();
        quantint = Float.parseFloat(quantity);
        if (quantint < 0) {
            Constant.setAlert(getActivity(), "Enter valid quantity.");
            return;
        }
        unit = quantitySP.getText().toString().trim();
        quantint = convertUnit(unit, quantint);
        helper.addBabyFoodIntake(babyid, babyDOB, intakeTime, food, quantint, unit, MainFragmentActivityBaby.noteTitle, MainFragmentActivityBaby.noteDescription);

        if (other.length() != 0) {
            helper.addIntakeCategory(other);
        }
        MainFragmentActivityBaby.noteDescription = "";
        MainFragmentActivityBaby.noteTitle = "";
        Constant.babysaveAlert(getActivity(), "Food detail added successfully.");
    }


    float convertUnit(String unitCase, float quantity) {

        switch (unitCase) {
            case "Cup":
                unit = "Milliliter";
                return (float) (quantity * 236.58);
            case "Tablespoon(Tbsp)":
                unit = "Milliliter";
                return quantity * 15;
            case "Ounce":
                unit = "Gram";
                return quantity * 28;
            case "Slice":
                unit = "Gram";
                return quantity * 14;
            case "Milliliter":
                unit = "Milliliter";
                return quantity;
            case "Gram":
                unit = "Gram";
                return quantity;
            default:
                return quantity;
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(getActivity(), errors.get(0).getCollatedErrorMessage(getActivity()));
    }

    /*Dialog box for confirmation*/
    public void setNoteDialog(final Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlide);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_note_activity_baby);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ImageView savebttn = (ImageView) dialog.findViewById(R.id.saveBt);
        ImageView cancelBt = (ImageView) dialog.findViewById(R.id.cancelBt);
        final EditText title = (EditText) dialog.findViewById(R.id.titleEdt);
        final EditText noteet = (EditText) dialog.findViewById(R.id.noteEdt);

        final LinearLayout parentsecond_addNote = (LinearLayout) dialog.findViewById(R.id.parentsecond_addNote);

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

        setupUI(parentsecond_addNote);
    }

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

    private void setValue(String category) {
        if (category.equals("Milk") || category.equals("Water") || category.equals("Juice")) {
            otherintake.setVisibility(View.GONE);
            otherin = false;
            selectedArray = 1;
            quantitySP.setText(liquid[0]);
        } else if (category.equals("Breast Milk")) {
            otherintake.setVisibility(View.GONE);
            otherin = false;
            quantitySP.setText(milli[0]);
            selectedArray = 2;
        } else if (category.equals("Cereal")) {
            otherintake.setVisibility(View.GONE);
            otherin = false;
            quantitySP.setText(quantitys[0]);
            selectedArray = 3;
        } else {
            otherintake.setVisibility(View.VISIBLE);
            otherin = true;
            quantitySP.setText(fullquantity[0]);
            selectedArray = 4;
        }
    }


}