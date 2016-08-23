package com.widevision.pregnantwoman.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Akhilesh on 08/04/15.
 */
@Table(name = "NoteRecordTable")
public class NoteRecordTable extends Model implements Serializable {

    @Column(name = "name")
    public String name;
    @Column(name = "note")
    public String note;
    @Column(name = "dateTime")
    public String dateTime;
    @Column(name = "title")
    public String title;

    public NoteRecordTable() {
        super();
    }

    public NoteRecordTable(String name, String note, String title, String dateTime) {

        super();
        this.name = name;
        this.note = note;
        this.title = title;
        this.dateTime = dateTime;

    }
}
