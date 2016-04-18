package com.uidmadapp.urgenceidentification.model;

import java.io.Serializable;

/**
 * Created by nyaina on 17/04/2016.
 */
public class Maladie implements Serializable{
    String patient_id_patient;
    String id_maladie;
    String titre;
    String img;
    String description;
    String conseil_description;
    String conseil_video;
    String conseil_img;

    public String getPatient_id_patient() {
        return patient_id_patient;
    }

    public void setPatient_id_patient(String patient_id_patient) {
        this.patient_id_patient = patient_id_patient;
    }

    public String getId_maladie() {
        return id_maladie;
    }

    public void setId_maladie(String id_maladie) {
        this.id_maladie = id_maladie;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConseil_description() {
        return conseil_description;
    }

    public void setConseil_description(String conseil_description) {
        this.conseil_description = conseil_description;
    }

    public String getConseil_video() {
        return conseil_video;
    }

    public void setConseil_video(String conseil_video) {
        this.conseil_video = conseil_video;
    }

    public String getConseil_img() {
        return conseil_img;
    }

    public void setConseil_img(String conseil_img) {
        this.conseil_img = conseil_img;
    }

}
