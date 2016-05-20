package com.zekisanmobile.petsitter2.vo;

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

    private String size;

    private double weight;

    private String breed;

    @JsonProperty("pet_cares")
    private RealmList<PetCare> petCares;

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

    public RealmList<PetCare> getPetCares() {
        return petCares;
    }

    public void setPetCares(RealmList<PetCare> petCares) {
        this.petCares = petCares;
    }
}
