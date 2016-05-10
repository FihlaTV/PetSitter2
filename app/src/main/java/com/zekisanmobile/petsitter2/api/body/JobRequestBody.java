package com.zekisanmobile.petsitter2.api.body;

import java.util.List;

public class JobRequestBody {

    long sitter_id;

    String app_id, date_start, date_final, time_start, time_final;

    double total_value;

    List<AnimalBody> animal_contacts;

    public long getSitter_id() {
        return sitter_id;
    }

    public void setSitter_id(long sitter_id) {
        this.sitter_id = sitter_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_final() {
        return date_final;
    }

    public void setDate_final(String date_final) {
        this.date_final = date_final;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_final() {
        return time_final;
    }

    public void setTime_final(String time_final) {
        this.time_final = time_final;
    }

    public double getTotal_value() {
        return total_value;
    }

    public void setTotal_value(double total_value) {
        this.total_value = total_value;
    }

    public List<AnimalBody> getAnimal_contacts() {
        return animal_contacts;
    }

    public void setAnimal_contacts(List<AnimalBody> animal_contacts) {
        this.animal_contacts = animal_contacts;
    }

}
