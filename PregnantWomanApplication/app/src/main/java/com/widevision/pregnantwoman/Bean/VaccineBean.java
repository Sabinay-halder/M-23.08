package com.widevision.pregnantwoman.Bean;

/**
 * Created by Mercury-five on 30/10/15.
 */
public class VaccineBean {

    private int _id;
    private String _age;
    private String _vaccineName;
    private String _babyId;
    private String _vaccineId;
    private String _status;
    private String VaccineDate;
    public String getVaccineDate() {
        return VaccineDate;
    }

    public void setVaccineDate(String vaccineDate) {
        VaccineDate = vaccineDate;
    }



    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_vaccineId() {
        return _vaccineId;
    }

    public void set_vaccineId(String _vaccineId) {
        this._vaccineId = _vaccineId;
    }

    public String get_babyId() {
        return _babyId;
    }

    public void set_babyId(String _babyId) {
        this._babyId = _babyId;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_age() {
        return _age;
    }

    public void set_age(String _age) {
        this._age = _age;
    }

    public String get_vaccineName() {
        return _vaccineName;
    }

    public void set_vaccineName(String _vaccineName) {
        this._vaccineName = _vaccineName;
    }


}
