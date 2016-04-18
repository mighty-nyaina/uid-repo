package com.uidmadapp.urgenceidentification.model;

import java.io.Serializable;

/**
 * Created by nyaina on 17/04/2016.
 */
public class Info_client implements Serializable{
    String id_tag;
    String id_patient;
    String nom;
    String prenom;
    String sexe;
    String date_naissance;
    String adresse;
    String contact;
    String email;
    String contact_urgence;
    String password;
    String fb_id;
    String photo;

    public String getId_tag() {
        return id_tag;
    }

    public void setId_tag(String id_tag) {
        this.id_tag = id_tag;
    }

    public String getId_patient() {
        return id_patient;
    }

    public void setId_patient(String id_patient) {
        this.id_patient = id_patient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_urgence() {
        return contact_urgence;
    }

    public void setContact_urgence(String contact_urgence) {
        this.contact_urgence = contact_urgence;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
