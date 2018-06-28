package com.ceu.lavanderia.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Agendamento POJO.
 */
@IgnoreExtraProperties
public class Agendamento {

    public static final String FIELD_HORA = "hora";
    public static final String FIELD_DATA = "data";

    private String name;
    private String hora;
    private String data;
    private String photo;

    public Agendamento() {}

    public Agendamento(String name, String hora, String data, String photo) {
        this.name = name;
        this.hora = hora;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
