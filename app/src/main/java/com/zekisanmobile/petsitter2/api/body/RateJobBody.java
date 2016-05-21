package com.zekisanmobile.petsitter2.api.body;

public class RateJobBody {

    // owner id
    String owner_app_id;

    String contact_app_id;

    String rate_app_id;

    int stars_qtd;

    String owner_comment;

    public String getOwner_app_id() {
        return owner_app_id;
    }

    public void setOwner_app_id(String owner_app_id) {
        this.owner_app_id = owner_app_id;
    }

    public String getContact_app_id() {
        return contact_app_id;
    }

    public void setContact_app_id(String contact_app_id) {
        this.contact_app_id = contact_app_id;
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
