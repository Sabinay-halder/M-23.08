package com.widevision.pregnantwoman.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by mercury-two on 18/8/15.
 */
@Table(name = "BabyHeightWeightRecord")
public class BabyHeiWeiTable extends Model {

    @Column(name = "BabyId")
    public long babyid;

    @Column(name = "DateOfMeasure")
    public String dateofmeasure;

    @Column(name = "GenderOfBirth")
    public String gender;


    @Column(name = "WeightBaby")
    public float weightbaby;

    @Column(name = "HeightBaby")
    public int heightbaby;

    @Column(name = "NoteTitleHeiWei")
    public String notetitleheiwei;

    @Column(name = "NoteDetailHeiWei")
    public String noteheiwei;

    public BabyHeiWeiTable() {
        super();
    }

    /*Insert baby info*/
    public BabyHeiWeiTable(long babyid, String dateofmeasure, String gender, float weightbaby, int heightbaby, String notetitleheiwei, String noteheiwei ) {

        super();
        this.babyid     = babyid;
      this.dateofmeasure= dateofmeasure;
        this.gender     = gender;
        this.weightbaby = weightbaby;
        this.heightbaby = heightbaby;
    this.notetitleheiwei= notetitleheiwei;
        this.noteheiwei = noteheiwei;
    }
}
