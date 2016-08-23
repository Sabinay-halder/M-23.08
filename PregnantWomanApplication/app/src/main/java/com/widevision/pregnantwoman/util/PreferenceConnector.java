package com.widevision.pregnantwoman.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;

public class PreferenceConnector {
    public static final String PREF_NAME = "Mother";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String IS_FIRSTTIME_MOTHER = "isFirstTimeMother";
    public static final String IS_FIRSTTIME_BABY = "isFirstTimeBaby";
    public static final String MOTHER_NAME = "MOTHER_NAME";
    public static final String MOTHER_DOB = "MOTHER_DOB";
    public static final String MOTHER_AGE = "MOTHER_AGE";
    public static final String EXPECTED_DATE = "EXPECTED_DATE";
    public static final String EXPECTED_DELIVERY_DATE = "EXPECTED_DELIVERY_DATE";
    public static final String DOCTOR_NAME = "DOCTOR_NAME";
    public static final String DOCTOR_NUMBER = "DOCTOR_NUMBER";
    public static final String START_DATE = "START_DATE";
    public static final String WEEK = "week";
    public static final String WEIGHT_LIST = "weight_list";
    public static final String TWINS = "TWINS";
    public static final String MOTHER_BTN = "MOTHER_BTN";
    public static final String BABY_BTN = "BABY_BTN";
    public static final String HEALTH_PARAMETER = "HEALTH_PARAMETER";
    public static final String CHART_MOTHER = "CHART_MOTHER";
    public static final String APPOINTMENT_MOTHER = "APPOINTMENT_MOTHER";
    public static final String NOTES_MOTHER = "NOTES_MOTHER";
    public static final String TIPS_MOTHER = "TIPS_MOTHER";
    public static final String MENU_MOTHER = "MENU_MOTHER";
    public static final String BLOOD_SUGAR_CHART = "BLOOD_SUGAR_CHART";
    public static final String START_WEEK = "START_WEEK";
    public static final String START_WEIGHT = "START_WEIGHT";


    public static final String FOODINTAKE = "FOODINTAKE";
    public static final String HEIFHT_WEIGHT = "HEIFHT_WEIGHT";
    public static final String SLEEP_RECORD = "SLEEP_RECORD";
    public static final String CHART_BABY = "CHART_BABY";
    public static final String NOTES_BABY = "NOTES_BABY";
    public static final String VACCINATION_BABY = "VACCINATION_BABY";
    public static final String APPOINTMENT_BABY = "APPOINTMENT_BABY";
    public static final String TIPS_BABY = "TIPS_BABY";
    public static final String MENU_BABY = "MENU_BABY";

    public static final String FOODINTAKE_NOTES = "FOODINTAKE_NOTES";
    public static final String FOODINTAKE_DATE = "FOODINTAKE_DATE";
    public static final String FOODINTAKE_TYPE = "FOODINTAKE_TYPE";
    public static final String SLEEPCHART = "SLEEPCHART";


    public static final String MOTHER_FIRST_TIME = "MOTHER_FIRST_TIME";

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeBoolean(Context context, String key, Boolean value) {
        getEditor(context).putBoolean(key, value).commit();

    }

    public static Boolean readBoolean(Context context, String key,
                                      Boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void clear(Context context) {
        getEditor(context).clear().commit();
    }

    public static void writeList(Context context, String key, ArrayList<Integer> list) {
        try {
            getEditor(context).putString(key, ObjectSerializer.serialize(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> readList(Context context, String key, ArrayList<Integer> list) {
        try {
            return (ArrayList<Integer>) ObjectSerializer.deserialize(getPreferences(context).getString(key, ObjectSerializer.serialize(new ArrayList<Integer>())));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
