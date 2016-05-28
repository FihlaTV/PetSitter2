package com.zekisanmobile.petsitter2.api.body;

public class CreatePetBody {

    private String owner_app_id;
    private long animal_id;

    private String app_id;
    private String name;
    private int age;
    private String age_text;
    private String size;
    private double weight;
    private String breed;
    private String pet_care;

    public String getOwner_app_id() {
        return owner_app_id;
    }

    public void setOwner_app_id(String owner_app_id) {
        this.owner_app_id = owner_app_id;
    }

    public long getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(long animal_id) {
        this.animal_id = animal_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
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

    public String getAge_text() {
        return age_text;
    }

    public void setAge_text(String age_text) {
        this.age_text = age_text;
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

    public String getPet_care() {
        return pet_care;
    }

    public void setPet_care(String pet_care) {
        this.pet_care = pet_care;
    }
}
