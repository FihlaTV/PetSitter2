package com.zekisanmobile.petsitter2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Job extends RealmObject {

    @PrimaryKey
    @JsonProperty("app_id")
    private String id;

    @JsonProperty("date_start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="EST")
    Date dateStart;

    @JsonProperty("date_final")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="EST")
    Date dateFinal;

    @JsonProperty("time_start")
    String timeStart;

    @JsonProperty("time_final")
    String timeFinal;

    @JsonProperty("created_at")
    String createdAt;

    @JsonProperty("status_cd")
    int status;

    @JsonProperty("animals")
    RealmList<Animal> animals;

    @JsonProperty("total_value")
    double totalValue;

    Sitter sitter;

    Owner owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateFinal() {
        return dateFinal;
    }

    public void setDateFinal(Date dateFinal) {
        this.dateFinal = dateFinal;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeFinal() {
        return timeFinal;
    }

    public void setTimeFinal(String timeFinal) {
        this.timeFinal = timeFinal;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RealmList<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(RealmList<Animal> animals) {
        this.animals = animals;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public Sitter getSitter() {
        return sitter;
    }

    public void setSitter(Sitter sitter) {
        this.sitter = sitter;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
