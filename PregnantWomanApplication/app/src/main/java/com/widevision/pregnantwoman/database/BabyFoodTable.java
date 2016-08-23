package com.widevision.pregnantwoman.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by mercury-two on 18/8/15.
 */
@Table(name = "BabyFoodIntakeRecord")
public class BabyFoodTable extends Model {

    @Column(name = "BabyId")
    public long babyid;

    @Column(name = "DateFoodIntake")
    public String date;

    @Column(name = "FoodIntakeTime")
    public String intakeTime;

    @Column(name = "FoodIntake")
    public String food;

    @Column(name = "Quantity")
    public float quantity;

    @Column(name = "Unit")
    public String unit;

    @Column(name = "NoteFoodTitle")
    public String notetitlefood;

    @Column(name = "NoteDetailFood")
    public String notefood;



    public BabyFoodTable() {
        super();
    }

    /*Insert baby fooddetail*/
    public BabyFoodTable(long babyid, String date, String intakeTime, String food, float quantity, String unit, String notetitlefood, String notefood) {

        super();
        this.babyid = babyid;
        this.date = date;
        this.intakeTime = intakeTime;
        this.food = food;
        this.quantity = quantity;
        this.unit = unit;
        this.notetitlefood = notetitlefood;
        this.notefood = notefood;
    }
}

//List<BabyFoodTable> foodlist = helper.babyfoodDetail(babyid, datenote);
//
//if (foodlist != null && foodlist.size() != 0) {
//        BabyNoteAdapterList.FoodAdapter babyfoodNoteAdapter = new BabyNoteAdapterList.FoodAdapter(getActivity(), foodlist);
//        noteLV.setAdapter(babyfoodNoteAdapter);
//        } else if (foodlist == null && foodlist.size() == 0) {
//
//        Constant.babysaveAlert(getActivity(), "No Record Found.");
//        }


//                if (name.trim().equals("") || datenote.trim().equals("")
//                        || categorys.trim().equals("")){
////                    babyGoAlert(getActivity(), "Insert All Fields.");
//                    Toast.makeText(getActivity(), "All Fields are Required. ", LENGTH_SHORT).show();
//
//                } else if (categorys.equals("Food") ){
//
//                    List<BabyFoodTable> foodlist = helper.babyfoodDetail(babyid, datenote);
//
//                    if (foodlist != null && foodlist.size() != 0) {
//                        BabyNoteAdapterList.FoodAdapter babyfoodNoteAdapter = new BabyNoteAdapterList.FoodAdapter(getActivity(), foodlist);
//                        noteLV.setAdapter(babyfoodNoteAdapter);
//                    } else if (foodlist == null && foodlist.size() == 0) {
//
//                        Constant.babysaveAlert(getActivity(), "No Record Found.");
//                    }
//
//
//                }  else if (categorys.equals("Sleep") ){
//
//                    List<BabySleepRecordTable> sleeplist = helper.babySleepDetail(babyid);
//
//                    if (sleeplist != null && sleeplist.size() != 0) {
//                        BabyNoteAdapterList.SleepAdapter babysleepNoteAdapter = new BabyNoteAdapterList.SleepAdapter(getActivity(), sleeplist);
//                        noteLV.setAdapter(babysleepNoteAdapter);
//                    } else if (sleeplist == null && sleeplist.size() == 0) {
//
//                        Constant.babysaveAlert(getActivity(), "No Record Found.");
//                    }
//
//                } else if (categorys.equals("Height") || categorys.equals("Weight") ){
//
//                    List<BabyHeiWeiTable> heilist = helper.babyHeiWeiDetail(babyid);
//
//                    if (heilist != null && heilist.size() != 0) {
//                        BabyNoteAdapterList.HeiWeiAdapter babyheiNoteAdapter = new BabyNoteAdapterList.HeiWeiAdapter(getActivity(), heilist);
//                        noteLV.setAdapter(babyheiNoteAdapter);
//                    } else if (heilist == null && heilist.size() == 0) {
//
//                        Constant.babysaveAlert(getActivity(), "No Record Found.");
//                    }
//
//                } else {
//                    babyGoAlert(getActivity(), "No Record Found. ");
//                }
