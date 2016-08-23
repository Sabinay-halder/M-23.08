package com.widevision.pregnantwoman.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by mercury-two on 17/8/15.
 */
@Table(name = "BabyInfoRecord")
public class BabyInfoTable extends Model {

    @Column(name = "NameOfBaby")
    public String name;
    @Column(name = "lastName")
    public String lastName;
    @Column(name = "DateofBirthBaby")
    public String dobbaby;

    @Column(name = "GenderBaby")
    public String genderbaby;

    @Column(name = "WeightBaby")
    public float weightbaby;

    @Column(name = "HeightBaby")
    public int heightbaby;

    @Column(name = "CircumferenceofHeadBaby")
    public int circumbaby;

    public BabyInfoTable() {
        super();
    }

    /*Insert baby info*/
    public BabyInfoTable(String firstname, String lastName, String dobbaby, String genderbaby, float weightbaby, int heightbaby, int circumbaby) {

        super();
        this.name = firstname;
        this.lastName = lastName;
        this.dobbaby = dobbaby;
        this.genderbaby = genderbaby;
        this.weightbaby = weightbaby;
        this.heightbaby = heightbaby;
        this.circumbaby = circumbaby;
    }
}
