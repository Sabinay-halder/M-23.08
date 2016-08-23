package com.widevision.pregnantwoman.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by mercury-two on 19/8/15.
 */
@Table(name = "BabySleepRecord")
public class BabySleepRecordTable extends Model {

    @Column(name = "BabyId")
    public long babyid;

    @Column(name = "DateOfSleepBaby")
    public String sleepdatebaby;

    @Column(name = "StartSleepBaby")
    public String starttimebaby;

    @Column(name = "EndSleepBaby")
    public String endtimebaby;

    @Column(name = "TitleNoteSleepBaby")
    public String titlenotsleepbaby;

    @Column(name = "NoteSleepBaby")
    public String notesleepbaby;

    @Column(name = "Hours")
    public float Hours;


    public BabySleepRecordTable() {
        super();
    }

    /*Insert baby info*/
    public BabySleepRecordTable(long babyid, String sleepdatebaby, String starttimebaby, String endtimebaby, String titlenotsleepbaby, String notesleepbaby, float Hours) {

        super();
        this.babyid = babyid;
        this.sleepdatebaby = sleepdatebaby;
        this.starttimebaby = starttimebaby;
        this.endtimebaby = endtimebaby;
        this.titlenotsleepbaby = titlenotsleepbaby;
        this.notesleepbaby = notesleepbaby;
        this.Hours = Hours;
    }
}
