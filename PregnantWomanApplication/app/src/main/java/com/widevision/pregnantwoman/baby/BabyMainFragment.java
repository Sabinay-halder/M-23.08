package com.widevision.pregnantwoman.baby;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.widevision.pregnantwoman.MainFragmentActivityBaby;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mercury-two on 13/8/15.
 */
public class BabyMainFragment extends Fragment {

    @Bind(R.id.bttnbabyprofile)
    ImageView babyProfileBttn;
    @Bind(R.id.bttnfood)
    ImageView foodIntakeBttn;
    @Bind(R.id.bttnheightweight)
    ImageView heiWeiBttn;
    @Bind(R.id.bttnsleep)
    ImageView sleepRecordBttn;
    @Bind(R.id.bttnchart)
    ImageView chartBttn;
    @Bind(R.id.bttnnote)
    ImageView noteBttn;
    @Bind(R.id.bttnvaccin)
    ImageView vaccinBttn;
    @Bind(R.id.bttnappoint)
    ImageView appointBttn;
    @Bind(R.id.bttntips)
    ImageView tipsBttn;
    @Bind(R.id.fragmant)
    LinearLayout fragLL;
    long babyid;
    String name, date, gender;
    float weigh;
    int heigh, circu;

    @Bind(R.id.food_relative)
    RelativeLayout health_parameter;
    @Bind(R.id.height_weight_relative)
    RelativeLayout height_weight_relative;
    @Bind(R.id.sleep_relative)
    RelativeLayout sleep_relative;
    @Bind(R.id.chart_relative)
    RelativeLayout chart_relative;
    @Bind(R.id.notes_relative)
    RelativeLayout notes_relative;
    @Bind(R.id.vaccination_relative)
    RelativeLayout vaccination_relative;
    @Bind(R.id.appointment_relative)
    RelativeLayout appointment_relative;
    @Bind(R.id.tips_relative)
    RelativeLayout tips_relative;


    private ShowcaseView showcaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_mainfragmentactivity, container, false);
        ButterKnife.bind(this, view);


//        fragLL.startAnimation(animRotate);
        babyProfileBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    Intent in = new Intent(getActivity(), BabyProfileUpdate.class);
                    startActivity(in);
                }
            }
        });


        foodIntakeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK_BABY = 1;
                        showcaseView.hide();
                    }
                    BabyFoodIntake food = new BabyFoodIntake();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.framebaby, food, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            }
        });
        heiWeiBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK_BABY = 1;
                        showcaseView.hide();
                    }
                    BabyHeightWeight heiwei = new BabyHeightWeight();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.framebaby, heiwei, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        sleepRecordBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK_BABY = 1;
                        showcaseView.hide();
                    }
                    BabySleepRecord sleepR = new BabySleepRecord();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.replace(R.id.framebaby, sleepR, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        chartBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    selectChartDialog();
                }
            }
        });

        noteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK_BABY = 1;
                        showcaseView.hide();
                    }
                    BabyNotes note = new BabyNotes();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.framebaby, note, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        vaccinBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK_BABY = 1;
                        showcaseView.hide();
                    }
                    BabyVaccination vaccin = new BabyVaccination();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.replace(R.id.framebaby, vaccin, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        appointBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK_BABY = 1;
                        showcaseView.hide();
                    }
                    BabyAppointmentRequest babyapoint = new BabyAppointmentRequest();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.replace(R.id.framebaby, babyapoint, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        tipsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK_BABY = 1;
                        showcaseView.hide();
                    }
                    BabyHealthTips health = new BabyHealthTips();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.replace(R.id.framebaby, health, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(R.drawable.baby_food);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(5, 5, 5, 5);
        health_parameter.setLayoutParams(layoutParams);

        BitmapDrawable bd1 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.baby_height_weight);
        int height1 = bd1.getBitmap().getHeight();
        int width1 = bd1.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(width1, height1);
        layoutParams1.setMargins(5, 5, 5, 5);
        height_weight_relative.setLayoutParams(layoutParams1);

        BitmapDrawable bd2 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.baby_sleep);
        int height2 = bd2.getBitmap().getHeight();
        int width2 = bd2.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(width2, height2);
        layoutParams2.setMargins(5, 5, 5, 5);
        sleep_relative.setLayoutParams(layoutParams2);

        BitmapDrawable bd3 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.baby_chart);
        int height3 = bd3.getBitmap().getHeight();
        int width3 = bd3.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(width3, height3);
        layoutParams3.setMargins(5, 5, 5, 5);
        chart_relative.setLayoutParams(layoutParams3);

        BitmapDrawable bd4 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.baby_notes);
        int height4 = bd4.getBitmap().getHeight();
        int width4 = bd4.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(width4, height4);
        layoutParams4.setMargins(5, 5, 5, 5);
        notes_relative.setLayoutParams(layoutParams4);

        BitmapDrawable bd5 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.baby_vaccination);
        int height5 = bd5.getBitmap().getHeight();
        int width5 = bd5.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(width5, height5);
        layoutParams5.setMargins(5, 5, 5, 5);
        vaccination_relative.setLayoutParams(layoutParams5);

        BitmapDrawable bd6 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.baby_appointment);
        int height6 = bd6.getBitmap().getHeight();
        int width6 = bd6.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(width6, height6);
        layoutParams6.setMargins(5, 5, 5, 5);
        appointment_relative.setLayoutParams(layoutParams6);

        BitmapDrawable bd7 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.baby_tips);
        int height7 = bd7.getBitmap().getHeight();
        int width7 = bd7.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(width7, height7);
        layoutParams7.setMargins(5, 5, 5, 5);
        tips_relative.setLayoutParams(layoutParams7);
    }

    void selectChartDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogFadeAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.char_selection_popup_baby);
        dialog.getWindow().setLayout(Constant.width - 10, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView cancel = (ImageView) dialog.findViewById(R.id.dialog_cancel);
        ImageView heightWeightChart = (ImageView) dialog.findViewById(R.id.weight);
        ImageView sleepRecordChart = (ImageView) dialog.findViewById(R.id.blood_sugar);
        ImageView foodIntakeChart = (ImageView) dialog.findViewById(R.id.blood_pressure);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        heightWeightChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    dialog.dismiss();
                    Constant.setButtonEnable();
                    BabyHeightWeightChart myf = new BabyHeightWeightChart();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.replace(R.id.framebaby, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        sleepRecordChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    dialog.dismiss();
                    Constant.setButtonEnable();
                    BabySleepChart myf = new BabySleepChart();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.replace(R.id.framebaby, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        foodIntakeChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    dialog.dismiss();
                    Constant.setButtonEnable();
                    BabyFoodIntakeChart myf = new BabyFoodIntakeChart();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.replace(R.id.framebaby, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.FOODINTAKE, R.id.bttnfood, R.layout.ok_button, "", getActivity().getString(R.string.baby_foodintake_btn), R.style.CustomShowcaseTheme2, false, listener);
        } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.HEIFHT_WEIGHT, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.HEIFHT_WEIGHT, R.id.bttnheightweight, R.layout.ok_button, "", getActivity().getString(R.string.baby_heightweight_btn), R.style.CustomShowcaseTheme2, false, listener);
        } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.HEIFHT_WEIGHT, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.SLEEP_RECORD, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.SLEEP_RECORD, R.id.bttnsleep, R.layout.ok_button, "", getActivity().getString(R.string.baby_sleep_btn), R.style.CustomShowcaseTheme2, false, listener);
        } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.SLEEP_RECORD, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.CHART_BABY, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.CHART_BABY, R.id.bttnchart, R.layout.ok_button, "", getActivity().getString(R.string.baby_chart_btn), R.style.CustomShowcaseTheme2, false, listener);
        } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.CHART_BABY, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.NOTES_BABY, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.NOTES_BABY, R.id.bttnnote, R.layout.ok_button, "", getActivity().getString(R.string.baby_notes_btn), R.style.CustomShowcaseTheme2, false, listener);
        } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.NOTES_BABY, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.VACCINATION_BABY, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.VACCINATION_BABY, R.id.bttnvaccin, R.layout.ok_button, "", getActivity().getString(R.string.baby_vaccine_btn), R.style.CustomShowcaseTheme2, false, listener);
        } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.VACCINATION_BABY, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.APPOINTMENT_BABY, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.APPOINTMENT_BABY, R.id.bttnappoint, R.layout.ok_button, "", getActivity().getString(R.string.baby_appointment_btn), R.style.CustomShowcaseTheme2, false, listener);
        } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.APPOINTMENT_BABY, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.TIPS_BABY, "No").equals("No")) {
            showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.TIPS_BABY, R.id.bttntips, R.layout.ok_button, "", getActivity().getString(R.string.baby_tips_btn), R.style.CustomShowcaseTheme2, false, listener);
        }
    }

    private OnShowcaseEventListener listener = new OnShowcaseEventListener() {
        @Override
        public void onShowcaseViewHide(ShowcaseView showcaseView) {
            if (Utils.WALKTHROUGH_ITEM_CLICK_BABY == 0) {
                if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.FOODINTAKE, R.id.bttnfood, R.layout.ok_button, "", getActivity().getString(R.string.baby_foodintake_btn), R.style.CustomShowcaseTheme2, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.FOODINTAKE, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.HEIFHT_WEIGHT, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.HEIFHT_WEIGHT, R.id.bttnheightweight, R.layout.ok_button, "", getActivity().getString(R.string.baby_heightweight_btn), R.style.CustomShowcaseTheme2, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.HEIFHT_WEIGHT, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.SLEEP_RECORD, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.SLEEP_RECORD, R.id.bttnsleep, R.layout.ok_button, "", getActivity().getString(R.string.baby_sleep_btn), R.style.CustomShowcaseTheme2, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.SLEEP_RECORD, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.CHART_BABY, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.CHART_BABY, R.id.bttnchart, R.layout.ok_button, "", getActivity().getString(R.string.baby_chart_btn), R.style.CustomShowcaseTheme2, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.CHART_BABY, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.NOTES_BABY, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.NOTES_BABY, R.id.bttnnote, R.layout.ok_button, "", getActivity().getString(R.string.baby_notes_btn), R.style.CustomShowcaseTheme2, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.NOTES_BABY, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.VACCINATION_BABY, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.VACCINATION_BABY, R.id.bttnvaccin, R.layout.ok_button, "", getActivity().getString(R.string.baby_vaccine_btn), R.style.CustomShowcaseTheme2, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.VACCINATION_BABY, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.APPOINTMENT_BABY, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.APPOINTMENT_BABY, R.id.bttnappoint, R.layout.ok_button, "", getActivity().getString(R.string.baby_appointment_btn), R.style.CustomShowcaseTheme2, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.APPOINTMENT_BABY, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.TIPS_BABY, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.TIPS_BABY, R.id.bttntips, R.layout.ok_button, "", getActivity().getString(R.string.baby_tips_btn), R.style.CustomShowcaseTheme2, false, listener);
                }
            } else {
                Utils.WALKTHROUGH_ITEM_CLICK_BABY = 0;
            }
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
