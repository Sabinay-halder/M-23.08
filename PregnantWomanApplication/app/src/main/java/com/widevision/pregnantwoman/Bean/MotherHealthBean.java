package com.widevision.pregnantwoman.Bean;

/**
 * Created by newtrainee on 4/9/15.
 */
public class MotherHealthBean {


    public long _Id;

    public String get_Date() {
        return _Date;
    }

    public void set_Date(String _Date) {
        this._Date = _Date;
    }

    public String get_Weight() {
        return _Weight;
    }

    public void set_Weight(String _Weight) {
        this._Weight = _Weight;
    }

    public long get_Id() {
        return _Id;
    }

    public void set_Id(long _Id) {
        this._Id = _Id;
    }

    public String get_SystolicBloodPressure() {
        return _SystolicBloodPressure;
    }

    public void set_SystolicBloodPressure(String _SystolicBloodPressure) {
        this._SystolicBloodPressure = _SystolicBloodPressure;
    }

    public String get_DiastolicBloodPressure() {
        return _DiastolicBloodPressure;
    }

    public void set_DiastolicBloodPressure(String _DiastolicBloodPressure) {
        this._DiastolicBloodPressure = _DiastolicBloodPressure;
    }

    public float get_BloodSugarBeforeMeal() {
        return _BloodSugarBeforeMeal;
    }

    public void set_BloodSugarBeforeMeal(float _BloodSugarBeforeMeal) {
        this._BloodSugarBeforeMeal = _BloodSugarBeforeMeal;
    }

    public float get_BloodSugarAfterMeal() {
        return _BloodSugarAfterMeal;
    }

    public void set_BloodSugarAfterMeal(float _BloodSugarAfterMeal) {
        this._BloodSugarAfterMeal = _BloodSugarAfterMeal;
    }

    public boolean is_PreExistingDiabetes() {
        return _PreExistingDiabetes;
    }

    public void set_PreExistingDiabetes(boolean _PreExistingDiabetes) {
        this._PreExistingDiabetes = _PreExistingDiabetes;
    }

    public String _Date;
    public String _Weight;
    public String _SystolicBloodPressure;
    public String _DiastolicBloodPressure;
    public float _BloodSugarBeforeMeal;
    public float _BloodSugarAfterMeal;
    public boolean _PreExistingDiabetes;
}
