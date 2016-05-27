package com.zekisanmobile.petsitter2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Pet extends RealmObject {

    @JsonProperty("app_id")
    @PrimaryKey
    private String id;

    private String name;

    private int age;

    @JsonProperty("age_text")
    private String ageText;

    private String size;

    private double weight;

    private String breed;

    @JsonProperty("pet_care")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PetCare petCare;

    @JsonProperty("photo")
    private PhotoUrl photoUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public PetCare getPetCare() {
        return petCare;
    }

    public void setPetCare(PetCare petCare) {
        this.petCare = petCare;
    }

    public PhotoUrl getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(PhotoUrl photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAgeText() {
        return ageText;
    }

    public void setAgeText(String ageText) {
        this.ageText = ageText;
    }
}
