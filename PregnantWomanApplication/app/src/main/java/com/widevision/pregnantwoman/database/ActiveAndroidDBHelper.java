package com.widevision.pregnantwoman.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.widevision.pregnantwoman.Bean.BabyDBBean;
import com.widevision.pregnantwoman.Bean.BabyHeightWeightBean;
import com.widevision.pregnantwoman.Bean.BabySleepBean;
import com.widevision.pregnantwoman.Bean.BabySleepRequiredBean;
import com.widevision.pregnantwoman.Bean.MotherHealthBean;
import com.widevision.pregnantwoman.Bean.VaccineBean;
import com.widevision.pregnantwoman.util.Constant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akhilesh on 08/04/15.
 */
public class ActiveAndroidDBHelper {

    public static ActiveAndroidDBHelper mDBHelper;

    private ActiveAndroidDBHelper() {

    }

    public static ActiveAndroidDBHelper getInstance() {
        if (mDBHelper == null) {
            if (mDBHelper == null) {
                mDBHelper = new ActiveAndroidDBHelper();
                return mDBHelper;
            }
        }
        return mDBHelper;
    }

    public void addMotherDetail(String name, String lastNameStr, String currentdate, String dob, String age, String expectedDate, String expectedDeliveryDate, String babbyMonth, String doctorName, String doctorNumber) {
        MotherRecordTable empTable = new MotherRecordTable(name, lastNameStr, currentdate, dob, age, expectedDate, expectedDeliveryDate, babbyMonth, doctorName, doctorNumber);
        empTable.save();
    }

    public List<MotherRecordTable> viewMother() {
        Select select = new Select();
        List<MotherRecordTable> list = select.all().from(MotherRecordTable.class).execute();
        return list;
    }

    public void updateMotherDetail(long _id, String _name, String lastNameStr, String _dob, String _age, String _expectedDate, String _expectedDeliveryDate, String _babbyMonth) {
        MotherRecordTable motherRecordTable = new Select().from(MotherRecordTable.class)
                .where("Id" + " = ?", _id).executeSingle();
        motherRecordTable.name = _name;
        motherRecordTable.lastName = lastNameStr;
        motherRecordTable.dob = _dob;
        motherRecordTable.age = _age;
        motherRecordTable.expecteConceiveDate = _expectedDate;
        motherRecordTable.expecteDeliveryDate = _expectedDeliveryDate;
        motherRecordTable.babbyMonth = _babbyMonth;
        motherRecordTable.save();
    }

    //start note

    public void addNote(String name, String note, String title, String dateTime) {
        NoteRecordTable noteRecordTable = new NoteRecordTable(name, note, title, dateTime);
        noteRecordTable.save();
    }

    public List<NoteRecordTable> viewAllNote() {
        Select select = new Select();
        List<NoteRecordTable> noteList = select.all().from(NoteRecordTable.class).execute();
        return noteList;
    }

    public void updateNote(String name, String note, String title, String dateTime, long id) {
        NoteRecordTable model = selectField("Id", id);
        model.dateTime = dateTime;
        model.note = note;
        model.title = title;
        model.name = name;
        model.save();
    }

    public void deleteNote(long id) {
        new Delete().from(NoteRecordTable.class).where("Id" + " = ?", id).executeSingle();

    }


    public NoteRecordTable selectField(String id, long fieldValue) {
        return new Select().from(NoteRecordTable.class)
                .where(id + " = ?", fieldValue).executeSingle();
    }

    // end note
    //start appointment table
    public void addAppointment(String name, String doctorName, String date, String time, String mobileTime, String reminderFor) {
        AppointmentRecordTable appointmentRecordTable = new AppointmentRecordTable(name, doctorName, date, time, mobileTime, reminderFor);
        appointmentRecordTable.save();
    }

    public void updateAppointment(String id, String name, String doctorName, String date, String time, String mobileTime, String reminderFor) {
        Select select = new Select();
        AppointmentRecordTable item = select.from(AppointmentRecordTable.class).where("_Id" + " = ?", id).executeSingle();
        item.name = name;
        item.doctorName = doctorName;
        item.date = date;
        item.time = time;
        item.reminderDate = mobileTime;
        item.reminderFor = reminderFor;
        item.save();
    }

    public List<AppointmentRecordTable> viewAllAppointment() {
        try {
            Select select = new Select();
            List<AppointmentRecordTable> appointmentList = select.all().from(AppointmentRecordTable.class).execute();
            return appointmentList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteAppointment(long id) {
        new Delete().from(AppointmentRecordTable.class).where(id + " = ?", id).executeSingle();
    }

    public AppointmentRecordTable selectAppointment(String id, long fieldValue) {
        return new Select().from(AppointmentRecordTable.class)
                .where(id + " = ?", fieldValue).executeSingle();
    }

    //end appointment table
    //start HelthParameter table
    public void addHelthParameter(long id, String date, String weight, String systolicBloodPressure, String diastolicBloodPressure, float beforeMeal, float afterMeal, boolean preExistingDiabetes) {
        MotherHelthParameterTable helthParameterTable = new MotherHelthParameterTable(id, date, weight, systolicBloodPressure, diastolicBloodPressure, beforeMeal, afterMeal, preExistingDiabetes);
        helthParameterTable.save();
    }

    public void updateHelthParameter(long id, long motherId, String date, String weight, String systolicBloodPressure, String diastolicBloodPressure, float beforeMeal, float afterMeal, boolean preExistingDiabetes) {
        MotherHelthParameterTable item = new Select().from(MotherHelthParameterTable.class)
                .where("Id" + " = ?", id).executeSingle();
        item._Id = motherId;
        item._Date = date;
        item._Weight = weight;
        item._SystolicBloodPressure = systolicBloodPressure;
        item._DiastolicBloodPressure = diastolicBloodPressure;
        item._BloodSugarBeforeMeal = beforeMeal;
        item._BloodSugarAfterMeal = afterMeal;
        item._PreExistingDiabetes = preExistingDiabetes;
        item.save();
    }

    public List<MotherHelthParameterTable> viewAllHealthRecord() {
        return new Select().all().from(MotherHelthParameterTable.class).execute();
    }


    //end HelthParameter table

    public Cursor viewAll() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from  woman_weight_table", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }


    public List<MotherHealthBean> orderByDate() {

        List<MotherHealthBean> list = new ArrayList<>();
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(false, "MotherHealthRecordTable", null, null
                    , null, null, null, "Date", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null && cursor.moveToFirst()) {
            do {
                MotherHealthBean bean = new MotherHealthBean();
                bean.set_Id(cursor.getLong(cursor.getColumnIndex("_Id")));
                bean.set_Date(cursor.getString(cursor.getColumnIndex("Date")));
                bean.set_Weight(cursor.getString(cursor.getColumnIndex("Weight")));
                bean.set_SystolicBloodPressure(cursor.getString(cursor.getColumnIndex("SystolicBloodPressure")));
                bean.set_DiastolicBloodPressure(cursor.getString(cursor.getColumnIndex("DiastolicBloodPressure")));
                bean.set_BloodSugarBeforeMeal(cursor.getFloat(cursor.getColumnIndex("BloodSugarBeforeMeal")));
                bean.set_BloodSugarAfterMeal(cursor.getFloat(cursor.getColumnIndex("BloodSugarAfterMeal")));
                bean.set_PreExistingDiabetes(cursor.getInt(cursor.getColumnIndex("PreExistingDiabetes")) > 0);
                list.add(bean);
            } while (cursor.moveToNext());
        }
        return list;
    }


    public ArrayList<BabyHeightWeightBean> getBabyHeightWeightData() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        Cursor cursor = null;
        ArrayList<BabyHeightWeightBean> list = new ArrayList<>();
        try {
            cursor = db.rawQuery("select * from BabyHeightWeightTable", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    BabyHeightWeightBean bean = new BabyHeightWeightBean();
                    bean.setStage(cursor.getString(cursor.getColumnIndex("Stage")));
                    bean.setAge(cursor.getString(cursor.getColumnIndex("Age")));
                    bean.setBoys_Height(cursor.getFloat(cursor.getColumnIndex("Boys_Height")));
                    bean.setBoys_Weight(cursor.getFloat(cursor.getColumnIndex("Boys_Weight")));
                    bean.setGirls_Height(cursor.getFloat(cursor.getColumnIndex("Girls_Height")));
                    bean.setGirls_Weight(cursor.getFloat(cursor.getColumnIndex("Girls_Weight")));
                    list.add(bean);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }


    //Baby DataBase Start
    //start Baby Info table
    public void addBabyInfo(String firstName, String lastName, String date, String gender, float weight, int height, int circumference) {
        BabyInfoTable babyInfoTable = new BabyInfoTable(firstName, lastName, date, gender, weight, height, circumference);
        babyInfoTable.save();
    }

    //fetch all data from babyinfo

    public List<BabyInfoTable> babyDetail() {
        Select select = new Select();
        List<BabyInfoTable> nameDateList = select.all().from(BabyInfoTable.class).execute();
        return nameDateList;
    }

    public BabyInfoTable getBaby(String babyId) {
        Select select = new Select();
        return select.from(BabyInfoTable.class).where("Id = " + babyId).executeSingle();
    }

    //Update record In BabyinfoTable

    public void babyInfoUpdate(long babyid, String name, String lastName, String date, String gender, float weight, int height, int circumference) {

        long i = babyid;
        String babid = "id";
        BabyInfoTable babyupdate = new Select().from(BabyInfoTable.class).where(babid + " = ?", i).executeSingle();
        babyupdate.name = name;
        babyupdate.lastName = lastName;
        babyupdate.dobbaby = date;
        babyupdate.genderbaby = gender;
        babyupdate.weightbaby = weight;
        babyupdate.heightbaby = height;
        babyupdate.circumbaby = circumference;
        babyupdate.save();
    }


    //end Baby Info table

//------------------------------------------------------

    /*start Baby FoodIntake table*/
    public void addBabyFoodIntake(long babyid, String date, String intakeTime, String food, float quantity, String unit, String notetitlefood, String notefood) {
        BabyFoodTable babyFoodTable = new BabyFoodTable(babyid, date, intakeTime, food, quantity, unit, notetitlefood, notefood);
        babyFoodTable.save();
    }

    //Fetch Values Food Intake

    public List<BabyFoodTable> babyfoodDetail(long babyid) {
        long i = babyid;
        String babid = "BabyId";
        Select select = new Select();
        List<BabyFoodTable> foodList = select.from(BabyFoodTable.class).where(babid + " = ?", i).execute();
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        return foodList;
    }

    //fetch all data from babyfood by datefrom
    public List<BabyDBBean> babyfoodDetail(long babyid, String datefrom) {

        long i = babyid;
        List<BabyDBBean> foodList = new ArrayList<BabyDBBean>();

//        String selectQuery = "Select * from  BabyFoodIntakeRecord Where BabyId = "+ i +" And DateFoodIntake >= "+ datefrom ;
        String selectQuery = "Select * from  BabyFoodIntakeRecord Where BabyId = '" + i + "' And DateFoodIntake >= '" + datefrom + "'";

        SQLiteDatabase no = ActiveAndroid.getDatabase();
        Cursor cursor = no.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BabyDBBean babyfood = new BabyDBBean();
                babyfood.setId(cursor.getLong(cursor.getColumnIndex("Id")));
                babyfood.setDatefood(cursor.getString(cursor.getColumnIndex("DateFoodIntake")));
                babyfood.setCategoryfood(cursor.getString(cursor.getColumnIndex("FoodIntake")));
                babyfood.setNotetitlefood(cursor.getString(cursor.getColumnIndex("NoteFoodTitle")));
                babyfood.setNotedetailfood(cursor.getString(cursor.getColumnIndex("NoteDetailFood")));
                foodList.add(babyfood);
            } while (cursor.moveToNext());
        }

        return foodList;
    }

    //fetch all data from babyfood by datefrom
    public List<BabyDBBean> babyfoodDetail(long babyid, String datefrom, String food) {
        long i = babyid;
        List<BabyDBBean> foodList = new ArrayList<>();
//        String selectQuery = "Select * from  BabyFoodIntakeRecord Where BabyId = "+ i +" And DateFoodIntake >= "+ datefrom ;
        String selectQuery = "Select * from  BabyFoodIntakeRecord Where BabyId = '" + i + "' And DateFoodIntake = '" + datefrom + "' And FoodIntake = '" + food + "'";

        SQLiteDatabase no = ActiveAndroid.getDatabase();
        Cursor cursor = no.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BabyDBBean babyfood = new BabyDBBean();
                babyfood.setBabyid(cursor.getInt(cursor.getColumnIndex("BabyId")));
                babyfood.setDatefood(cursor.getString(cursor.getColumnIndex("DateFoodIntake")));
                babyfood.setCategoryfood(cursor.getString(cursor.getColumnIndex("FoodIntake")));
                babyfood.setQuantityfood(cursor.getString(cursor.getColumnIndex("Quantity")));
                babyfood.setIntakeTime(cursor.getString(cursor.getColumnIndex("FoodIntakeTime")));
                babyfood.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                foodList.add(babyfood);
            } while (cursor.moveToNext());
        }
        return foodList;
    }


    public List<BabyDBBean> orderDate(long babyid) {
        long i = babyid;
        List<BabyDBBean> foodList = new ArrayList<>();
        String query = "SELECT * FROM BabyFoodIntakeRecord Where BabyId = " + i + " ORDER BY date(DateFoodIntake) ASC Limit 1";
        SQLiteDatabase no = ActiveAndroid.getDatabase();
        Cursor cursor = no.rawQuery(query, null);
        BabyDBBean babyfood = new BabyDBBean();
        if (cursor != null && cursor.moveToLast()) {
            babyfood.setBabyid(cursor.getInt(cursor.getColumnIndex("BabyId")));
            babyfood.setDatefood(cursor.getString(cursor.getColumnIndex("DateFoodIntake")));
            babyfood.setCategoryfood(cursor.getString(cursor.getColumnIndex("FoodIntake")));
            babyfood.setQuantityfood(cursor.getString(cursor.getColumnIndex("Quantity")));
            babyfood.setIntakeTime(cursor.getString(cursor.getColumnIndex("FoodIntakeTime")));
            babyfood.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
        }
        String selectQuery = "Select * from  BabyFoodIntakeRecord Where BabyId = '" + i + "' And DateFoodIntake = '" + babyfood.getDatefood() + "' And FoodIntake = '" + babyfood.getCategoryfood() + "'";
        Cursor cursor2 = no.rawQuery(selectQuery, null);
        if (cursor2 != null && cursor2.moveToFirst()) {
            do {
                BabyDBBean item = new BabyDBBean();
                item.setBabyid(cursor.getInt(cursor.getColumnIndex("BabyId")));
                item.setDatefood(cursor.getString(cursor.getColumnIndex("DateFoodIntake")));
                item.setCategoryfood(cursor.getString(cursor.getColumnIndex("FoodIntake")));
                item.setQuantityfood(cursor.getString(cursor.getColumnIndex("Quantity")));
                item.setIntakeTime(cursor.getString(cursor.getColumnIndex("FoodIntakeTime")));
                item.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                foodList.add(babyfood);
            } while (cursor.moveToNext());
        }
        return foodList;
    }

    //    update Notelist Food
    public void babyFoodUpdate(long babyid, String title, String notedetailfood) {
        List<BabyFoodTable> foodListav = new ArrayList<>();
        long i = babyid;
        BabyFoodTable babyfoodupdate = new Select().from(BabyFoodTable.class).where("Id = ?", i).executeSingle();

        babyfoodupdate.notefood = notedetailfood;
        babyfoodupdate.notetitlefood = title;

        babyfoodupdate.save();
    }


//    -----------------------------------------------------

    //Insert Food Intake Catecory

    public void addIntakeCategory(String category) {
        String tablename = "FoodIntakeCategoryTable";

        SQLiteDatabase db = ActiveAndroid.getDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put("Category", category);
        db.insert(tablename, null, contentValue);
    }


    //Fetch Food Intake Catecory
    public ArrayList<String> foodIntakeCategList() {

        // Select All Query
        String selectQuery = "Select * from  FoodIntakeCategoryTable";
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        ArrayList<String> IntakeCategoryList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                IntakeCategoryList.add(cursor.getString(cursor.getColumnIndex("category")));
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        // return contact list
        return IntakeCategoryList;
    }

    /*end Baby FoodIntake table*/

//------------------------------------------------------

    /*start Baby Height Weight table*/
    public void addBabyHeiWei(long babyid, String date, String gender, float weight, int height, String notetitleheiwei, String noteheiwei) {
        BabyHeiWeiTable babyHeiWeiTable = new BabyHeiWeiTable(babyid, date, gender, weight, height, notetitleheiwei, noteheiwei);
        babyHeiWeiTable.save();
    }

    public BabyInfoTable viewBabyById(long id) {
        BabyInfoTable item = new Select().from(BabyInfoTable.class).where("Id = ?", id).executeSingle();
        return item;
    }

    //fetch all data from babyheiwei

    public List<BabyDBBean> babyHeiWeiDeta(long babyid, String datefrom) {
        long i = babyid;

        List<BabyDBBean> heiweiList = new ArrayList<BabyDBBean>();
        String selectQuery = "Select * from  BabyHeightWeightRecord Where BabyId = '" + i + "' And DateOfMeasure >= '" + datefrom + "'";

        SQLiteDatabase no = ActiveAndroid.getDatabase();
        Cursor cursor = no.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BabyDBBean babyheiwei = new BabyDBBean();
                babyheiwei.setId(cursor.getLong(cursor.getColumnIndex("Id")));
                babyheiwei.setDateheiwei(cursor.getString(cursor.getColumnIndex("DateOfMeasure")));
                babyheiwei.setWeight(cursor.getFloat(cursor.getColumnIndex("WeightBaby")));
                babyheiwei.setHeight(cursor.getInt(cursor.getColumnIndex("HeightBaby")));
                babyheiwei.setNotetitleheiwei(cursor.getString(cursor.getColumnIndex("NoteTitleHeiWei")));
                babyheiwei.setNotedetailheiwei(cursor.getString(cursor.getColumnIndex("NoteDetailHeiWei")));
                heiweiList.add(babyheiwei);
            } while (cursor.moveToNext());
        }
        return heiweiList;
    }

    public List<BabyHeiWeiTable> babyHeightInfo(long id) {


        List<BabyHeiWeiTable> list = new Select().all().from(BabyHeiWeiTable.class).where("BabyId = ?", id).execute();

/*
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from BabyHeightWeightRecord where BabyId = ?", new String[]{"" + id});
            if (cursor != null && cursor.moveToFirst()) {
                BabyInfoBean item = new BabyInfoBean();
                item.setBabyHeight(cursor.getString(cursor.getInt(2)));
                item.setBabyWeight(cursor.getString(cursor.getInt(1)));
                item.setDate(cursor.getString(cursor.getInt(3)));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return list;
    }

    public List<String> babyWeightInfo(long id) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from BabyHeightWeightRecord Where BabyId = " + id, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndex("WeightBaby")));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //    update Notelist Food

    public void babyHeiWeiUpdate(long babyid, String title, String notedetailHeiWei) {
        List<BabyHeiWeiTable> heiWeiListav = new ArrayList<BabyHeiWeiTable>();
        long i = babyid;
        BabyHeiWeiTable babyHeiWeiupdate = new Select().from(BabyHeiWeiTable.class).where("Id = ?", i).executeSingle();

        babyHeiWeiupdate.noteheiwei = notedetailHeiWei;
        babyHeiWeiupdate.notetitleheiwei = title;

        babyHeiWeiupdate.save();
    }


    //    public List<BabyHeiWeiTable> babyHeiWeiDetail(long babyid) {
//        long i = babyid;
////        String babid = "BabyId";
//        Select select = new Select();
//        List<BabyHeiWeiTable> heiWeiList = select.all().from(BabyHeiWeiTable.class).where(" BabyId = '"+ i +"'").execute();
//        return heiWeiList;
//    }
    /*end Baby Height Weight table*

//------------------------------------------------------

    /*start Baby Height Weight table*/
    public void addBabySleepRecord(long babyid, String sleepdate, String starttimebaby, String endtimebaby, String notetitlesleepbaby, String notesleepbaby, float hours) {
        Select select = new Select();
        String dateSleep = "DateOfSleepBaby";
        BabySleepRecordTable list = select.all().from(BabySleepRecordTable.class).where(dateSleep + " = ?", sleepdate).executeSingle();
        if (list != null) {
            float h = list.Hours + hours;
            list.Hours = h;
            list.save();
        } else {
            BabySleepRecordTable babySleepRecordTable = new BabySleepRecordTable(babyid, sleepdate, starttimebaby, endtimebaby, notetitlesleepbaby, notesleepbaby, hours);
            babySleepRecordTable.save();
        }
    }
    //fetch all data from babysleep


    public List<BabySleepRecordTable> babySleepDetail(long babyid) {
        long i = babyid;
        String babid = "BabyId";
        Select select = new Select();
        List<BabySleepRecordTable> sleepList = select.all().from(BabySleepRecordTable.class).where(babid + " = ?", i).execute();
        return sleepList;
    }

    public List<BabySleepBean> babySleepRecordBetweenDates(String _StartDate, String _EndDate) {
        String query = "SELECT * FROM BabySleepRecord where DateOfSleepBaby between '" + _StartDate + "' and '" + _EndDate + "'" + " ORDER BY date(DateOfSleepBaby) ASC";
        ArrayList<BabySleepBean> list = new ArrayList<>();
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                BabySleepBean item = new BabySleepBean();
                item.setBabyid(cursor.getLong(cursor.getColumnIndex("BabyId")));
                item.setSleepdatebaby(cursor.getString(cursor.getColumnIndex("DateOfSleepBaby")));
                item.setStarttimebaby(cursor.getString(cursor.getColumnIndex("StartSleepBaby")));
                item.setEndtimebaby(cursor.getString(cursor.getColumnIndex("EndSleepBaby")));
                item.setTitlenotsleepbaby(cursor.getString(cursor.getColumnIndex("TitleNoteSleepBaby")));
                item.setNotesleepbaby(cursor.getString(cursor.getColumnIndex("NoteSleepBaby")));
                item.setHours(cursor.getFloat(cursor.getColumnIndex("Hours")));
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }


    //fetch all data from babysleep by datefrom
    public List<BabyDBBean> babySleepDeta(long babyid, String datefrom) {
        long i = babyid;

        List<BabyDBBean> sleepList = new ArrayList<>();
        String selectQuery = "Select * from  BabySleepRecord Where BabyId = " + i + " And DateOfSleepBaby >= " + datefrom;

        SQLiteDatabase no = ActiveAndroid.getDatabase();
        Cursor cursor = no.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BabyDBBean babysleep = new BabyDBBean();
                babysleep.setId(cursor.getLong(cursor.getColumnIndex("Id")));
                babysleep.setBabyid(cursor.getInt(cursor.getColumnIndex("BabyId")));
                babysleep.setDatesleep(cursor.getString(cursor.getColumnIndex("DateOfSleepBaby")));
                babysleep.setNotetitlesleep(cursor.getString(cursor.getColumnIndex("TitleNoteSleepBaby")));
                babysleep.setNotedetailsleep(cursor.getString(cursor.getColumnIndex("NoteSleepBaby")));

                sleepList.add(babysleep);
            } while (cursor.moveToNext());
        }

        return sleepList;
    }


    public ArrayList<BabySleepRequiredBean> babySleepRequiredData() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();

        ArrayList<BabySleepRequiredBean> list = new ArrayList<>();

        Cursor cursor = null;
        cursor = db.rawQuery("select * from BabySleepRecordTable", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BabySleepRequiredBean item = new BabySleepRequiredBean();
                item.setAge(cursor.getString(cursor.getColumnIndex("Age")));
                item.setHour(cursor.getString(cursor.getColumnIndex("TotalHours")));
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }


//    update Notelist Food

    public void babySleepUpdate(long babyid, String title, String notedetailSleep) {

        long i = babyid;
        BabySleepRecordTable babySleepupdate = new Select().from(BabySleepRecordTable.class).where("Id = ?", i).executeSingle();

        babySleepupdate.notesleepbaby = notedetailSleep;
        babySleepupdate.titlenotsleepbaby = title;

        babySleepupdate.save();
    }


    public ArrayList<VaccineBean> getVaccineList() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();

        ArrayList<VaccineBean> list = new ArrayList<>();
        Cursor cursor = null;
        cursor = db.rawQuery("select * from VaccinationTable", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                VaccineBean bean = new VaccineBean();
                bean.set_id(cursor.getInt(cursor.getColumnIndex("Id")));
                bean.set_age(cursor.getString(cursor.getColumnIndex("Age")));
                bean.set_vaccineName(cursor.getString(cursor.getColumnIndex("Vaccine")));
                bean.set_status("FALSE");
                list.add(bean);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void setVaccineStatusTable(String _babyId, String _birthDate) {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> listDates = new ArrayList<>();
        Cursor cursor = null;
        cursor = db.rawQuery("select * from VaccinationTable", null);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(Constant.datePattern);
        DateTime birthDate = dateTimeFormatter.parseDateTime(_birthDate);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursor.getInt(cursor.getColumnIndex("Id")));
                listDates.add(cursor.getInt(cursor.getColumnIndex("Mounth")));
            } while (cursor.moveToNext());
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int item = list.get(i);
            DateTime date = birthDate.plusMonths(listDates.get(i));
            String vaccineDate = date.toString(Constant.datePattern);
            ContentValues contentValues = new ContentValues();
            contentValues.put("babyId", _babyId);
            contentValues.put("vaccineId", item);
            contentValues.put("status", "FALSE");
            contentValues.put("VaccineDate", vaccineDate);
            db.insert("VaccinationStatusTable", null, contentValues);
        }
    }


    public ArrayList<VaccineBean> getVaccineStatusList(String _babyid) {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        ArrayList<VaccineBean> list = new ArrayList<>();
        Cursor cursor = null;
        cursor = db.rawQuery("select * from VaccinationStatusTable where babyId = " + _babyid, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                VaccineBean bean = new VaccineBean();
                bean.set_vaccineId(cursor.getString(cursor.getColumnIndex("vaccineId")));
                bean.set_status(cursor.getString(cursor.getColumnIndex("status")));
                bean.set_babyId(cursor.getString(cursor.getColumnIndex("babyId")));
                bean.setVaccineDate(cursor.getString(cursor.getColumnIndex("VaccineDate")));
                list.add(bean);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void updateBabayvaccineStatus(String _babyId, String vaccineId, String status) {
        SQLiteDatabase db = ActiveAndroid.getDatabase();

        db.execSQL("UPDATE VaccinationStatusTable SET status = " + "'" + status + "'" + " where babyId = '" + _babyId + "' and vaccineId = '" + vaccineId + "'");

    }
}
