package com.widevision.pregnantwoman.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Deepak on 08/04/15.
 */
@Table(name = "AppointmentRecordTable")
public class AppointmentRecordTable extends Model implements Serializable {

    @Column(name = "name")
    public String name;
    @Column(name = "doctorName")
    public String doctorName;
    @Column(name = "date")
    public String date;
    @Column(name = "time")
    public String time;
    @Column(name = "reminderDate")
    public String reminderDate;
    @Column(name = "reminderFor")
    public String reminderFor;

    public AppointmentRecordTable() {
        super();
    }

    public AppointmentRecordTable(String name, String doctorName, String date, String time, String reminderDate, String reminderFor) {

        super();
        this.name = name;
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.reminderDate = reminderDate;
        this.reminderFor = reminderFor;
    }
}
