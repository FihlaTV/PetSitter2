package com.zekisanmobile.petsitter2.vo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class State extends RealmObject {

    @PrimaryKey
    private long id;

    private String sigla;

    private String nome;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
