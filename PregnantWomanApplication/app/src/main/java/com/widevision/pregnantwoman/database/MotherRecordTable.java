package com.widevision.pregnantwoman.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Akhilesh on 08/04/15.
 */
@Table(name = "MotherRecordTable")
public class MotherRecordTable extends Model {

    @Column(name = "name")
    public String lastName;
    @Column(name = "lastName")
    public String name;
    @Column(name = "insertionDate")
    public String insertionDate;
    @Column(name = "dob")
    public String dob;
    @Column(name = "age")
    public String age;
    @Column(name = "expecteConceiveDate")
    public String expecteConceiveDate;
    @Column(name = "expecteDeliveryDate")
    public String expecteDeliveryDate;
    @Column(name = "babbyMonth")
    public String babbyMonth;
    @Column(name = "doctorName")
    public String doctorName;
    @Column(name = "doctorNumber")
    public String doctorNumber;

    public MotherRecordTable() {
        super();
    }

    public MotherRecordTable(String name, String lastNameStr, String insertionDate, String dob, String age, String expecteConceiveDate, String expecteDeliveryDate, String babbyMonth, String doctorName, String doctorNumber) {

        super();
        this.name = name;
        this.lastName = lastNameStr;
        this.insertionDate = insertionDate;
        this.dob = dob;
        this.age = age;
        this.expecteConceiveDate = expecteConceiveDate;
        this.expecteDeliveryDate = expecteDeliveryDate;
        this.babbyMonth = babbyMonth;
        this.doctorName = doctorName;
        this.doctorNumber = doctorNumber;
    }
}
