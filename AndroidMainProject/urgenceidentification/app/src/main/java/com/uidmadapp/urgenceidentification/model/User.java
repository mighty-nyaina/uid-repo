package com.uidmadapp.urgenceidentification.model;

/**
 * Created by nyaina on 11/04/2016.
 */

public class User {
    private int id_patient;
    private String nom;
    private String prenom;
    private int sexe;
    private String date_naissance; //sqlite not support Date field
    private String adresse;
    private String contact;
    private String email;
    private String contact_urgence;
    private String password;
    private String fb_id;
    private String photo;

    public int getId_patient() {
        return id_patient;
    }

    public void setId_patient(int id_patient) {
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

    public int getSexe() {
        return sexe;
    }

    public void setSexe(int sexe) {
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

    public User() {
    }

    public User(int id_patient, String nom, String prenom, int sexe, String date_naissance, String adresse, String contact, String email, String contact_urgence, String password, String fb_id, String photo) {
        this.id_patient = id_patient;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.date_naissance = date_naissance;
        this.adresse = adresse;
        this.contact = contact;
        this.email = email;
        this.contact_urgence = contact_urgence;
        this.password = password;
        this.fb_id = fb_id;
        this.photo = photo;
    }
}
