package com.widevision.pregnantwoman.mother;

import android.app.Dialog;
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
import com.widevision.pregnantwoman.MainFragmentActivity;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WomanMainActivity extends Fragment {

    @Bind(R.id.helth_btn)
    ImageView mHelthBtn;
    @Bind(R.id.addNote)
    ImageView mAddNote;
    @Bind(R.id.appointment_btn)
    ImageView mAppointmentBtn;
    @Bind(R.id.chart_btn)
    ImageView mChartBtn;
    @Bind(R.id.tips_btn)
    ImageView mTipsBtn;
    @Bind(R.id.health_parameter_relative)
    RelativeLayout health_parameter;
    @Bind(R.id.chart_relative)
    RelativeLayout chartRelative;
    @Bind(R.id.appointement_relative)
    RelativeLayout appointement_relative;
    @Bind(R.id.notes_relative)
    RelativeLayout notes_relative;
    @Bind(R.id.tips_relative)
    RelativeLayout tips_relative;
    private ShowcaseView showcaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragmentactivity, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(R.drawable.health_oerameter);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        health_parameter.setLayoutParams(layoutParams);

        BitmapDrawable bd1 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.tips);
        int height1 = bd1.getBitmap().getHeight();
        int width1 = bd1.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(width1, height1);
        layoutParams1.setMargins(10, 10, 10, 10);
        chartRelative.setLayoutParams(layoutParams1);


        BitmapDrawable bd2 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.request);
        int height2 = bd2.getBitmap().getHeight();
        int width2 = bd2.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(width2, height2);
        layoutParams2.setMargins(10, 10, 10, 10);
        appointement_relative.setLayoutParams(layoutParams2);


        BitmapDrawable bd3 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.notes);
        int height3 = bd3.getBitmap().getHeight();
        int width3 = bd3.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(width3, height3);
        layoutParams3.setMargins(10, 10, 10, 10);
        notes_relative.setLayoutParams(layoutParams3);


        BitmapDrawable bd4 = (BitmapDrawable) this.getResources().getDrawable(R.drawable.tips);
        int height4 = bd4.getBitmap().getHeight();
        int width4 = bd4.getBitmap().getWidth();
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(width4, height4);
        layoutParams4.setMargins(10, 10, 10, 10);
        tips_relative.setLayoutParams(layoutParams4);
        mHelthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK = 1;
                        showcaseView.hide();
                    }
                    HelthParameterActivityWomen myf = new HelthParameterActivityWomen();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        mAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK = 1;
                        showcaseView.hide();

                    }
                    AppointmentListActivity myf = new AppointmentListActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK = 1;
                        showcaseView.hide();

                    }
                    NotesListActivity myf = new NotesListActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        mChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK = 1;
                        showcaseView.hide();
                    }
                    selectChartDialog();
                }
            }
        });
        mTipsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    Constant.setButtonEnable();
                    if (showcaseView != null) {
                        Utils.WALKTHROUGH_ITEM_CLICK = 1;
                        showcaseView.hide();
                    }
                    TipsFragment myf = new TipsFragment();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

    }

    void selectChartDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogFadeAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.char_selection_popup);
        dialog.getWindow().setLayout(Constant.width - 10, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView cancel = (ImageView) dialog.findViewById(R.id.dialog_cancel);
        ImageView weight = (ImageView) dialog.findViewById(R.id.weight);
        ImageView bloodPressure = (ImageView) dialog.findViewById(R.id.blood_pressure);
        ImageView bloodSugar = (ImageView) dialog.findViewById(R.id.blood_sugar);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    dialog.dismiss();
                    Constant.setButtonEnable();
                    WeightChart myf = new WeightChart();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        bloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    dialog.dismiss();
                    Constant.setButtonEnable();
                    BloodPresureChart myf = new BloodPresureChart();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        bloodSugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.buttonEnable) {
                    dialog.dismiss();
                    Constant.setButtonEnable();
                    BloodSugarChart myf = new BloodSugarChart();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
                    transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        dialog.show();
    }

    private OnShowcaseEventListener listener = new OnShowcaseEventListener() {
        @Override
        public void onShowcaseViewHide(ShowcaseView showcaseView) {
            if (Utils.WALKTHROUGH_ITEM_CLICK == 0) {
                if (PreferenceConnector.readString(getActivity(), PreferenceConnector.HEALTH_PARAMETER, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.HEALTH_PARAMETER, R.id.helth_btn, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_health_parameter_btn), R.style.CustomShowcaseTheme, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.HEALTH_PARAMETER, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.CHART_MOTHER, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.CHART_MOTHER, R.id.chart_btn, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_chart_btn), R.style.CustomShowcaseTheme, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.CHART_MOTHER, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.APPOINTMENT_MOTHER, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.APPOINTMENT_MOTHER, R.id.appointment_btn, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_appointment_btn), R.style.CustomShowcaseTheme, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.APPOINTMENT_MOTHER, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.NOTES_MOTHER, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.NOTES_MOTHER, R.id.addNote, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_notes_btn), R.style.CustomShowcaseTheme, false, listener);
                } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.NOTES_MOTHER, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.TIPS_MOTHER, "No").equals("No")) {
                    showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.TIPS_MOTHER, R.id.tips_btn, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_tips_btn), R.style.CustomShowcaseTheme, false, listener);
                }
            } else {
                Utils.WALKTHROUGH_ITEM_CLICK = 0;
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

    @Override
    public void onResume() {
        super.onResume();

        if (PreferenceConnector.readString(getActivity(), PreferenceConnector.MOTHER_FIRST_TIME, "No").equals("No")) {
            if (PreferenceConnector.readString(getActivity(), PreferenceConnector.HEALTH_PARAMETER, "No").equals("No")) {
                showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.HEALTH_PARAMETER, R.id.helth_btn, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_health_parameter_btn), R.style.CustomShowcaseTheme, false, listener);
            } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.HEALTH_PARAMETER, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.CHART_MOTHER, "No").equals("No")) {
                showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.CHART_MOTHER, R.id.chart_btn, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_chart_btn), R.style.CustomShowcaseTheme, false, listener);
            } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.CHART_MOTHER, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.APPOINTMENT_MOTHER, "No").equals("No")) {
                showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.APPOINTMENT_MOTHER, R.id.appointment_btn, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_appointment_btn), R.style.CustomShowcaseTheme, false, listener);
            } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.APPOINTMENT_MOTHER, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.NOTES_MOTHER, "No").equals("No")) {
                showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.NOTES_MOTHER, R.id.addNote, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_notes_btn), R.style.CustomShowcaseTheme, false, listener);
            } else if (PreferenceConnector.readString(getActivity(), PreferenceConnector.NOTES_MOTHER, "No").equals("Yes") && PreferenceConnector.readString(getActivity(), PreferenceConnector.TIPS_MOTHER, "No").equals("No")) {
                showcaseView = Utils.showWalkThrough(getActivity(), PreferenceConnector.TIPS_MOTHER, R.id.tips_btn, R.layout.ok_button, "", getActivity().getResources().getString(R.string.mother_tips_btn), R.style.CustomShowcaseTheme, false, listener);
            }
        } else {
            HelthParameterActivityWomen myf = new HelthParameterActivityWomen();
            FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_out_dialog, R.anim.slide_up_dialog, R.anim.push_out_to_left_anim);
            transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}