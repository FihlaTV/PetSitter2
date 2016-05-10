package com.zekisanmobile.petsitter2.api.body;

public class RateJobBody {

    String contact_id;

    int stars_qtd;

    String text;

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public int getStars_qtd() {
        return stars_qtd;
    }

    public void setStars_qtd(int stars_qtd) {
        this.stars_qtd = stars_qtd;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
