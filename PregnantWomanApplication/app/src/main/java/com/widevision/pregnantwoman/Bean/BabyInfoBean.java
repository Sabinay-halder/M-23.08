package com.widevision.pregnantwoman.Bean;

/**
 * Created by Mercury-five on 21/09/15.
 */

/*Bean for get baby height and weight info that user inserted*/
public class BabyInfoBean {

    public String babyHeight;
    public String babyWeight;
    public String date;

    public String getBabyHeight() {
        return babyHeight;
    }

    public void setBabyHeight(String babyHeight) {
        this.babyHeight = babyHeight;
    }

    public String getBabyWeight() {
        return babyWeight;
    }

    public void setBabyWeight(String babyWeight) {
        this.babyWeight = babyWeight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
