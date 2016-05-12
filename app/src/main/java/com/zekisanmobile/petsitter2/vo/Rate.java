package com.zekisanmobile.petsitter2.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Rate extends RealmObject {

    @PrimaryKey
    @JsonProperty("app_id")
    private String id;

    @JsonProperty("stars_qtd")
    private int starsQtd;

    @JsonProperty("owner_comment")
    private String ownerComment;

    @JsonProperty("sitter_comment")
    private String sitterComment;

    public int getStarsQtd() {
        return starsQtd;
    }

    public void setStarsQtd(int starsQtd) {
        this.starsQtd = starsQtd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerComment() {
        return ownerComment;
    }

    public void setOwnerComment(String ownerComment) {
        this.ownerComment = ownerComment;
    }

    public String getSitterComment() {
        return sitterComment;
    }

    public void setSitterComment(String sitterComment) {
        this.sitterComment = sitterComment;
    }
}
