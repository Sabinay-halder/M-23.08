package com.widevision.pregnantwoman.baby;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.widevision.pregnantwoman.Bean.BabyDBBean;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.DatePickerViewAndroid;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.OnWheelChangedListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.WheelView;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_SHORT;

public class BabyNotes extends HideKeyFragment implements DatePickerDialog.OnDateSetListener, Validator.ValidationListener {

    @Bind(R.id.etnamenote)
    TextView nameET;
    @NotEmpty(message = "Date required.")
    @Bind(R.id.etdatefromnote)
    EditText dateET;
    @Bind(R.id.spcategorynote)
    TextView categorySP;
    @Bind(R.id.bttngonote)
    ImageView goBTTN;
    @Bind(R.id.llnotetablelist)
    LinearLayout noteListtableLL;

    private boolean addView = false;
    private ActiveAndroidDBHelper helper;
    private Validator validator;
    private boolean otherin = false, aDate = false, bDate = false;
    String done;
    int a = 0;
    private String name, babyDOB, datenote, notes, categorys, dateFood, dateHeiWei, dateSleep, noteFoodDetail, noteheiweiDetail, noteSleepdetail;
    private long babyid;
    String currentDateandTime;
    private int selected_value = 0;
    List<BabyDBBean> foodlist;
    List<BabyDBBean> heiWeilist;
    List<BabyDBBean> sleeplist;
    private String category[] = {"Food", "Height-Weight", "Sleep"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_notes, container, false);
        ButterKnife.bind(this, view);
        helper = ActiveAndroidDBHelper.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);

        noteListtableLL.setVisibility(View.GONE);

        nameET.setText(Constant.babyName);
        babyDOB = Constant.babyDOB;
        babyid = Constant.babyId;
//        dateET.setText("2015-08-12");
        /*Date picker*/
        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                aDate = b;
                if (b) {
                   /* DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePattern);
                    DateTime dt = formatter.parseDateTime(babyDOB);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, dt.getDayOfMonth());
                    calendar.set(Calendar.MONTH, dt.getMonthOfYear() - 1);
                    calendar.set(Calendar.YEAR, dt.getYear());
                    Calendar now = Calendar.getInstance();
                    int year = 0, month = 0, day = 0;
                    if (dateET.getText().toString().trim().equals("")) {
                        year = now.get(Calendar.YEAR);
                        month = now.get(Calendar.MONTH);
                        day = now.get(Calendar.DAY_OF_MONTH);
                    } else {
                        DateTime selectedDate = formatter.parseDateTime(dateET.getText().toString().trim());
                        year = selectedDate.getYear();
                        month = selectedDate.getMonthOfYear() - 1;
                        day = selectedDate.getDayOfMonth();
                    }

                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            BabyNotes.this,
                            year,
                            month,
                            day
                    );

                    dpd.setMinDate(calendar);
                    dpd.setMaxDate(now);
                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");*/
                    setDatePicker(dateET);
                }
            }
        });
//        SELECT * FROM BabyFoodIntakeRecord WHERE BabyId="1" AND DateFoodIntake >= "2015-08-15" ;


        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteListtableLL.setVisibility(View.GONE);
                setDatePicker(dateET);
               /* aDate = true;
                DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePattern);
                DateTime dt = formatter.parseDateTime(babyDOB);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, dt.getDayOfMonth());
                calendar.set(Calendar.MONTH, dt.getMonthOfYear() - 1);
                calendar.set(Calendar.YEAR, dt.getYear());
                Calendar now = Calendar.getInstance();
                int year = 0, month = 0, day = 0;
                if (dateET.getText().toString().trim().equals("")) {
                    year = now.get(Calendar.YEAR);
                    month = now.get(Calendar.MONTH);
                    day = now.get(Calendar.DAY_OF_MONTH);
                } else {
                    DateTime selectedDate = formatter.parseDateTime(dateET.getText().toString().trim());
                    year = selectedDate.getYear();
                    month = selectedDate.getMonthOfYear() - 1;
                    day = selectedDate.getDayOfMonth();
                }

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        BabyNotes.this,
                        year,
                        month,
                        day
                );
                dpd.setMinDate(calendar);
                dpd.setMaxDate(now);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");*/
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat(Constant.datePattern);
        currentDateandTime = sdf.format(new Date());

        /*Category Spinner */
        categorySP.setText(category[0]);
        done = "food";
        categorySP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPicker(category);
            }
        });


         /*Go Button*/
        goBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                datenote = dateET.getText().toString();
                categorys = categorySP.getText().toString();
                addView = true;
                if (name.trim().equals("") || datenote.trim().equals("")
                        || categorys.trim().equals("")) {
//                    babyGoAlert(getActivity(), "Insert All Fields.");
                    Toast.makeText(getActivity(), "All fields are required. ", LENGTH_SHORT).show();
                } else if (done.equals("food")) {
                    noteListtableLL.setVisibility(View.VISIBLE);
                    if (foodlist != null) {
                        foodlist.clear();
                    }
                    a = 1;
                    foodlist = helper.babyfoodDetail(babyid, datenote);
                    if (foodlist != null && foodlist.size() != 0) {
                        /*BabyNoteAdapterList.FoodAdapter babyfoodNoteAdapter = new BabyNoteAdapterList.FoodAdapter(getActivity(), foodlist);
                        noteLV.setAdapter(babyfoodNoteAdapter);*/
                        /*noteListtableLL*/
                        int size = foodlist.size();
                        for (int i = 0; i < size; i++) {
                            addFoodView(getActivity(), foodlist.get(i));
                        }
                    } else if (foodlist == null || foodlist.size() == 0) {
                        Constant.setAlert(getActivity(), "No record found.");
                        noteListtableLL.removeAllViews();
                    }
                } else if (done.equals("heiwei")) {
                    noteListtableLL.setVisibility(View.VISIBLE);
                    if (heiWeilist != null) {
                        heiWeilist.clear();
                    }
                    a = 2;
                    heiWeilist = helper.babyHeiWeiDeta(babyid, datenote);
                    if (heiWeilist != null && heiWeilist.size() != 0) {
                       /* BabyNoteAdapterList.HeiWeiAdapter babyheiNoteAdapter = new BabyNoteAdapterList.HeiWeiAdapter(getActivity(), heiWeilist);
                        noteLV.setAdapter(babyheiNoteAdapter);*/
                        int size = heiWeilist.size();
                        for (int i = 0; i < size; i++) {
                            addFoodView(getActivity(), heiWeilist.get(i));
                        }
                    } else if (heiWeilist == null || heiWeilist.size() == 0) {
                        noteListtableLL.removeAllViews();
                        Constant.setAlert(getActivity(), "No record found.");
                    }
                } else if (done.equals("sleep")) {
                    noteListtableLL.setVisibility(View.VISIBLE);
                    if (sleeplist != null) {
                        sleeplist.clear();
                    }
                    sleeplist = helper.babySleepDeta(babyid, datenote);
                    a = 3;
                    if (sleeplist != null && sleeplist.size() != 0) {
                        /*BabyNoteAdapterList.SleepAdapter babysleepNoteAdapter = new BabyNoteAdapterList.SleepAdapter(getActivity(), sleeplist);
                        noteLV.setAdapter(babysleepNoteAdapter);*/
                        int size = sleeplist.size();
                        for (int i = 0; i < size; i++) {
                            addFoodView(getActivity(), sleeplist.get(i));
                        }
                    } else if (sleeplist == null || sleeplist.size() == 0) {
                        Constant.setAlert(getActivity(), "No record found.");
                        noteListtableLL.removeAllViews();
                    }
                }
            }
        });
        return view;
    }

    void noteDialog(Context context, final BabyDBBean item) {

        final Dialog dialog = new Dialog(context, R.style.DialogFadeAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_note_dialog_baby);
        dialog.getWindow().setLayout(Constant.width - 20, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView title = (TextView) dialog.findViewById(R.id.titleTxt);
        final EditText titleEdt = (EditText) dialog.findViewById(R.id.titleEdt);
        final TextView noteTxt = (TextView) dialog.findViewById(R.id.noteTxt);
        final ImageView updateTxt = (ImageView) dialog.findViewById(R.id.saveBt);
        final ImageView edtBtn = (ImageView) dialog.findViewById(R.id.edit_img);
        final ImageView cancleBt = (ImageView) dialog.findViewById(R.id.cancleBt);
        final EditText noteEdt = (EditText) dialog.findViewById(R.id.noteEdt);
        switch (a) {
            case 1:
                if (item.getNotedetailfood() == null || item.getNotetitlefood().length() == 0) {
                    title.setText(item.getDatefood());
                    titleEdt.setText(item.getDatefood());
                } else {
                    title.setText(item.getNotetitlefood());
                    titleEdt.setText(item.getNotetitlefood());
                }
                noteTxt.setText(item.getNotedetailfood());
                noteEdt.setText(item.getNotedetailfood());

                if (item.getNotedetailfood() == null || item.getNotedetailfood().length() == 0) {
                    noteEdt.setVisibility(View.VISIBLE);
                    updateTxt.setVisibility(View.VISIBLE);
                    noteTxt.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (item.getNotetitleheiwei() == null || item.getNotetitleheiwei().length() == 0) {
                    title.setText(item.getNotedetailheiwei());
                    titleEdt.setText(item.getNotedetailheiwei());
                } else {
                    title.setText(item.getNotetitleheiwei());
                    titleEdt.setText(item.getNotetitleheiwei());
                }

                if (item.getNotedetailheiwei() == null || item.getNotetitleheiwei().length() == 0) {
                    noteEdt.setVisibility(View.VISIBLE);
                    updateTxt.setVisibility(View.VISIBLE);
                    noteTxt.setVisibility(View.GONE);
                } else {
                    noteTxt.setText(item.getNotedetailheiwei());
                    noteEdt.setText(item.getNotedetailheiwei());
                }
                break;
            case 3:
                if (item.getNotedetailsleep() == null || item.getNotedetailsleep().trim().length() == 0) {
                    title.setText(item.getDatesleep());
                    titleEdt.setText(item.getDatesleep());
                } else {
                    title.setText(item.getNotetitlefood());
                    titleEdt.setText(item.getNotetitlefood());
                }

                if (item.getNotedetailsleep() == null || item.getNotedetailsleep().length() == 0) {
                    noteEdt.setVisibility(View.VISIBLE);
                    updateTxt.setVisibility(View.VISIBLE);
                    noteTxt.setVisibility(View.GONE);
                } else {
                    noteTxt.setText(item.getNotedetailsleep());
                    noteEdt.setText(item.getNotedetailsleep());
                }
                break;
        }

        edtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteEdt.setVisibility(View.VISIBLE);
                updateTxt.setVisibility(View.VISIBLE);
                titleEdt.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);
                noteTxt.setVisibility(View.GONE);

            }
        });
        updateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String note = noteEdt.getText().toString().trim();
                String title = titleEdt.getText().toString().trim();
                long id = item.getId();
                if (a == 1) {
                    helper.babyFoodUpdate(id, title, note);
                    noteListtableLL.setVisibility(View.GONE);
                } else if (a == 2) {
                    helper.babyHeiWeiUpdate(id, title, note);
                    noteListtableLL.setVisibility(View.GONE);
                } else if (a == 3) {
                    helper.babySleepUpdate(id, title, note);
                    noteListtableLL.setVisibility(View.GONE);
                }
                dialog.dismiss();
                babyGoAlert(getActivity(), "update successfully.");
            }
        });
        cancleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
                DateTime dt = formatter1.parseDateTime(babyDOB);
                if (!dateSelected.isAfterNow() && dt.isBefore(dateSelected)) {
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

    /*Validation Set*/
    @Override
    public void onValidationSucceeded() {
        name = nameET.getText().toString().trim();
        datenote = dateET.getText().toString().trim();
        categorys = categorySP.getText().toString().trim();

        /*Condition To take Value Either from Spinner or from Edittext*/
        if (otherin == true) {
            //        notes = notedetailTV.getText().toString().trim();
        } else {

        }
        if (a == 1) {
            helper.babyFoodUpdate(babyid, dateFood, notes);
            noteListtableLL.setVisibility(View.GONE);

        } else if (a == 2) {
            helper.babyHeiWeiUpdate(babyid, dateHeiWei, notes);
            noteListtableLL.setVisibility(View.GONE);

        } else if (a == 3) {
            helper.babyFoodUpdate(babyid, dateFood, notes);
            noteListtableLL.setVisibility(View.GONE);

        }
        Log.e("data of food", " " + babyid + " " + name + " " + datenote + " " + currentDateandTime + " " + categorys + " " + dateFood + "" + notes + " " + noteFoodDetail);


        babyGoAlert(getActivity(), "Food detail added successfully.");
//        MainFragmentActivity.fragmentManager.popBackStack();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Constant.setAlert(getActivity(), errors.get(0).getCollatedErrorMessage(getActivity()));
    }


    /*add view row for each item*/
    void addFoodView(Context context, final BabyDBBean item) {
        if (addView) {
            noteListtableLL.removeAllViews();
            addView = false;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.baby_note_list, noteListtableLL, false);
        TextView categoryTxt = (TextView) rootView.findViewById(R.id.categoryTxt);
        TextView dateTxt = (TextView) rootView.findViewById(R.id.dateTxt);
        TextView descriptionTxt = (TextView) rootView.findViewById(R.id.descriptionTxt);
        LinearLayout descriptionLayout = (LinearLayout) rootView.findViewById(R.id.descriptionLayout);

        switch (a) {
            case 1:
                if (item.getNotetitlefood() == null) {
                    categoryTxt.setText("Notes Title");
                } else {
                    if (item.getNotetitlefood().trim().equals("")) {
                        categoryTxt.setText("Notes Title");
                    } else {
                        categoryTxt.setText(item.getNotetitlefood());
                    }
                }
                dateTxt.setText(item.getDatefood());
                descriptionTxt.setText("FOOD");
                descriptionLayout.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case 2:
                if (item.getNotetitleheiwei() == null) {
                    categoryTxt.setText("Note Title");
                } else {
                    if (item.getNotetitleheiwei().trim().equals("")) {
                        categoryTxt.setText("Note Title");
                    } else {
                        categoryTxt.setText(item.getNotetitleheiwei());
                    }
                }
                dateTxt.setText(item.getDateheiwei());
                descriptionTxt.setText("HEIGHT-WEIGHT");
                descriptionLayout.setBackgroundColor(getResources().getColor(R.color.h));
                break;
            case 3:
                if (item.getNotetitlesleep() == null) {
                    categoryTxt.setText("Note Title");
                } else {
                    if (item.getNotetitlesleep().trim().equals("")) {
                        categoryTxt.setText("Note Title");
                    } else {
                        categoryTxt.setText(item.getNotetitlesleep());
                    }
                }
                dateTxt.setText(item.getDatesleep());
                descriptionTxt.setText("SLEEP");
                descriptionLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                break;
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherin = true;
                if (a == 1) {
                   /* notedetailLL.setVisibility(View.VISIBLE);*/
                    /*BabyDBBean item = foodlist.get(i);*/

                    dateFood = item.getDatefood();
                    noteFoodDetail = item.getNotedetailfood();
                    /*notedetailTV.setText(noteFoodDetail);*/
                    noteDialog(getActivity(), item);
                } else if (a == 2) {
                    dateHeiWei = item.getDateheiwei();
                    noteheiweiDetail = item.getNotedetailheiwei();
                    noteDialog(getActivity(), item);
                    /*notedetailLL.setVisibility(View.VISIBLE);*/
                    /*BabyDBBean item = heiWeilist.get(i);*/


                   /* notedetailTV.setText(noteheiweiDetail);*/
                } else if (a == 3) {
                    dateSleep = item.getDatesleep();
                    noteSleepdetail = item.getNotedetailsleep();
                    noteDialog(getActivity(), item);
                  /*  notedetailLL.setVisibility(View.VISIBLE);*/
                    /*BabyDBBean item = sleeplist.get(i);*/


                  /*  notedetailTV.setText(noteSleepdetail);*/
                }
            }
        });
        noteListtableLL.addView(rootView);
    }


    /*Baby Dialog*/
    public static void babyGoAlert(final Activity activity, final String msg) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView message = (TextView) dialog.findViewById(R.id.dialog_message);
        message.setText(msg);
        TextView okBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
        if (msg.equals("No Record Found")) {
            okBtn.setVisibility(View.GONE);
        } else {
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }


    private void setPicker(final String[] strArr) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogAnimation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picker_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        final WheelView country = (WheelView) dialog.findViewById(R.id.cityWheel);
        country.setVisibleItems(3);
        final CountryAdapterSecond adapter = new CountryAdapterSecond(getActivity(), strArr);
        country.setViewAdapter(adapter);

        LinearLayout titleLayout = (LinearLayout) dialog.findViewById(R.id.pickerTitleLayout);
        ImageView doneButton = (ImageView) dialog.findViewById(R.id.pickerDone);
        ImageView closeButton = (ImageView) dialog.findViewById(R.id.pickerClose);
        final TextView textView = (TextView) dialog.findViewById(R.id.selectedTxt);
        titleLayout.setBackgroundColor(getResources().getColor(R.color.heading));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                String category = strArr[selected_value];
                categorySP.setText(category);

                noteListtableLL.setVisibility(View.GONE);
                if (selected_value == 0) {
                    done = "food";
                } else if (selected_value == 1) {
                    done = "heiwei";
                } else if (selected_value == 2) {
                    done = "sleep";
                }


            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selected_value = newValue;
                textView.setText(strArr[selected_value]);
                textView.setTextColor(getResources().getColor(R.color.text_color));
                adapter.notifyDataChangedEvent();
            }
        });
        selected_value = 0;
        country.setCurrentItem(0);
        textView.setText(strArr[0]);
        textView.setTextColor(getResources().getColor(R.color.text_color));

        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    /**
     * Adapter for picker
     */
    private class CountryAdapterSecond extends AbstractWheelTextAdapter {
        String[] countries;

        /**
         * Constructor
         */
        protected CountryAdapterSecond(Context context, String[] countries) {
            super(context, R.layout.row_wheel_layout, NO_RESOURCE);
            this.countries = countries;
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView img = (TextView) view.findViewById(R.id.country_name);
            if (selected_value == index) {
                img.setTextColor(Color.parseColor("#000023"));
            } else {
                img.setTextColor(Color.parseColor("#A1242E76"));
            }
            img.setText(countries[index]);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
}

