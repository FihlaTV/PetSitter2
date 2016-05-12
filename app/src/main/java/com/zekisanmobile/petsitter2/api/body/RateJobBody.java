package com.zekisanmobile.petsitter2.api.body;

public class RateJobBody {

    String app_id;

    String rate_app_id;

    int stars_qtd;

    String owner_comment;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getRate_app_id() {
        return rate_app_id;
    }

    public void setRate_app_id(String rate_app_id) {
        this.rate_app_id = rate_app_id;
    }

    public int getStars_qtd() {
        return stars_qtd;
    }

    public void setStars_qtd(int stars_qtd) {
        this.stars_qtd = stars_qtd;
    }

    public String getOwner_comment() {
        return owner_comment;
    }

    public void setOwner_comment(String owner_comment) {
        this.owner_comment = owner_comment;
    }
}
