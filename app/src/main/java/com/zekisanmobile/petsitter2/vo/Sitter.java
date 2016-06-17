package com.zekisanmobile.petsitter2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Sitter extends RealmObject {

    @PrimaryKey
    @JsonProperty("app_id")
    private String id;

    String name;

    String surname;

    String street;

    String address_number;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String complement;

    String cep;

    String district;

    String city;

    String state;

    @JsonProperty("about_me")
    String aboutMe;

    @JsonProperty("value_hour")
    float valueHour;

    float latitude;

    float longitude;

    String phone;

    RealmList<Animal> animals;

    @JsonProperty("photo")
    private PhotoUrl photoUrl;

    @JsonProperty("profile_photos")
    RealmList<PhotoUrl> profilePhotos;

    @JsonProperty("rate_avg")
    int rateAvg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("contacts")
    RealmList<Job> jobs;

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public float getValueHour() {
        return valueHour;
    }

    public void setValueHour(float valueHour) {
        this.valueHour = valueHour;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RealmList<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(RealmList<Animal> animals) {
        this.animals = animals;
    }

    public PhotoUrl getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(PhotoUrl photoUrl) {
        this.photoUrl = photoUrl;
    }

    public RealmList<PhotoUrl> getProfilePhotos() {
        return profilePhotos;
    }

    public void setProfilePhotos(RealmList<PhotoUrl> profilePhotos) {
        this.profilePhotos = profilePhotos;
    }

    public int getRateAvg() {
        return rateAvg;
    }

    public void setRateAvg(int rateAvg) {
        this.rateAvg = rateAvg;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress_number() {
        return address_number;
    }

    public void setAddress_number(String address_number) {
        this.address_number = address_number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public RealmList<Job> getJobs() {
        return jobs;
    }

    public void setJobs(RealmList<Job> jobs) {
        this.jobs = jobs;
    }
}
