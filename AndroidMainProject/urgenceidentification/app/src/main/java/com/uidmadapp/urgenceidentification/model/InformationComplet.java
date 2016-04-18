package com.uidmadapp.urgenceidentification.model;

import java.io.Serializable;

/**
 * Created by nyaina on 17/04/2016.
 */
public class InformationComplet implements Serializable{
    Hopital[] hopital_proche;
    Maladie[] maladie;
    Info_client[] info_client;
    String longitude;
    String latitude;

    public Hopital[] getHopital_proche() {
        return hopital_proche;
    }

    public void setHopital_proche(Hopital[] hopital_proche) {
        this.hopital_proche = hopital_proche;
    }

    public Maladie[] getMaladie() {
        return maladie;
    }

    public void setMaladie(Maladie[] maladie) {
        this.maladie = maladie;
    }

    public Info_client[] getInfo_client() {
        return info_client;
    }

    public void setInfo_client(Info_client[] info_client) {
        this.info_client = info_client;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
