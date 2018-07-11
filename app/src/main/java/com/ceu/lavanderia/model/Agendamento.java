package com.ceu.lavanderia.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Agendamento POJO.
 */
@IgnoreExtraProperties
public class Agendamento {

    private String name;
    private String userID;
    private String hora;
    private String data;
    private String photo;

    public Agendamento() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Date HoraFormatada(){
        DateFormat format = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(hora);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date DataFormatada(){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(data);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
