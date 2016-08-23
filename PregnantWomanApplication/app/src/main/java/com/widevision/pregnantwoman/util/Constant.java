package com.widevision.pregnantwoman.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widevision.pregnantwoman.MainFragmentActivityBaby;
import com.widevision.pregnantwoman.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by newtrainee on 5/2/15.
 */
public class Constant {
    public static long babyId = 0;
    public static int height = 0, width = 0, tagFragment = 0, tagForNoteFragment = 0, tagForEditProfile = 0, type = 0;
    public static int mButtonTime = 600;
    public static boolean buttonEnable = true;
    public static int serviceDelayForReminder = 60000;
    public static int serviceDelayFORVaccine = 86400000;
    public static String dateTimePattern = "yyyy-MM-dd HH:mm", babyDOB = "", gender = "";
    public static String datePattern = "yyyy-MM-dd";
    public static String datePatternFullName = "yyyy-MMMM-dd";
    public static String timePattern = "HH:mm", babyName = "";
    public static String TAGFRAGMENT = "TAGFRAGMENT";
    public static String BabyVaccineTable = "BabyVaccineTable";

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Period getDate(int _year, int _mounth, int _date) {
        LocalDate birthdate = new LocalDate(_year, _mounth, _date);          //Birth date
        LocalDate now = new LocalDate();                    //Today's date
        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
//Now access the values as below

        return period;
    }

    public static int getPregnancyMonth(DateTime concecive, DateTime deleivery) {

        LocalDate startDate = new LocalDate(concecive.getYear(), concecive.getMonthOfYear(), concecive.getDayOfMonth());          //Birth date
        LocalDate endDate = new LocalDate(deleivery.getYear(), deleivery.getMonthOfYear(), deleivery.getDayOfMonth());                    //Today's date
        Period period = new Period(startDate, endDate, PeriodType.yearMonthDay());

        if (period.getYears() > 0) {
            return 10;
        }
        return period.getMonths();
    }

    public static Period getDuration(DateTime concecive, DateTime deleivery) {
        LocalDate startDate = new LocalDate(concecive.getYear(), concecive.getMonthOfYear(), concecive.getDayOfMonth());          //start date
        LocalDate endDate = new LocalDate(deleivery.getYear(), deleivery.getMonthOfYear(), deleivery.getDayOfMonth());                    //end's date
        Period period = new Period(startDate, endDate, PeriodType.yearMonthDay());
        return period;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setButtonEnable() {
        buttonEnable = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonEnable = true;
            }
        }, mButtonTime);
    }

    public static void setAlert(final Activity activity, final String msg) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView message = (TextView) dialog.findViewById(R.id.dialog_message);
        message.setText(msg);
        TextView okBtn = (TextView) dialog.findViewById(R.id.dialog_ok);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (msg.trim().equalsIgnoreCase(activity.getResources().getString(R.string.close_permission))) {
                    activity.finish();
                }
            }
        });

        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    public static void setDialog(final Activity activity, final String msg) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_view);
        dialog.getWindow().setLayout(Constant.width - 15, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.dialog_no_layout);
        TextView message = (TextView) dialog.findViewById(R.id.dialog_message);
        TextView okBtn = (TextView) dialog.findViewById(R.id.dialog_ok);
        TextView cancle = (TextView) dialog.findViewById(R.id.dialog_no);
        cancle.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        message.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                activity.finish();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static String getCurrentTime() {
        DateTime dateTime = new DateTime();
        return dateTime.toString(dateTimePattern);
    }

    public static String getDate() {
        DateTime dateTime = new DateTime();
        return dateTime.toString(datePattern);
    }

    public static String getTime() {
        DateTime dateTime = new DateTime();
        return dateTime.toString(timePattern);
    }


    public static void animateReavel(final Activity activity, final Animation fadeInAnimation, int cx, int cy, final View myView, final View myViewSecond, float finalRadius) {
       /* // get the final radius for the clipping circle
        float finalRadius = hypo(myView.getWidth(), myView.getHeight());*/
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                myView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd() {
                myViewSecond.setVisibility(View.VISIBLE);
                myViewSecond.startAnimation(fadeInAnimation);
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        animator.start();
    }

    public static float hypo(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    /*Baby Dialog*/
    public static void babysaveAlert(final Activity activity, final String msg) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
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
                    MainFragmentActivityBaby.fragmentManager.popBackStack();
                }
            });
        }
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

}
