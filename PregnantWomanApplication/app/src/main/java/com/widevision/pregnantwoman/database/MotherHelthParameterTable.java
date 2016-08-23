package com.widevision.pregnantwoman.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "MotherHealthRecordTable")
public class MotherHelthParameterTable extends Model {

    @Column(name = "_Id")
    public long _Id;
    @Column(name = "Date")
    public String _Date;
    @Column(name = "Weight")
    public String _Weight;
    @Column(name = "SystolicBloodPressure")
    public String _SystolicBloodPressure;
    @Column(name = "DiastolicBloodPressure")
    public String _DiastolicBloodPressure;
    @Column(name = "BloodSugarBeforeMeal")
    public float _BloodSugarBeforeMeal;
    @Column(name = "BloodSugarAfterMeal")
    public float _BloodSugarAfterMeal;
    @Column(name = "PreExistingDiabetes")
    public boolean _PreExistingDiabetes;

    public MotherHelthParameterTable() {
        super();
    }

    public MotherHelthParameterTable(long _id, String _Date, String _weight, String _systolicBloodPressure, String _diastolicBloodPressure, float _beforeMeal, float _afterMeal, boolean PreExistingDiabetes) {

        super();
        this._Id = _id;
        this._Date = _Date;
        this._Weight = _weight;
        this._SystolicBloodPressure = _systolicBloodPressure;
        this._DiastolicBloodPressure = _diastolicBloodPressure;
        this._BloodSugarBeforeMeal = _beforeMeal;
        this._BloodSugarAfterMeal = _afterMeal;
        this._PreExistingDiabetes = PreExistingDiabetes;
    }
}
