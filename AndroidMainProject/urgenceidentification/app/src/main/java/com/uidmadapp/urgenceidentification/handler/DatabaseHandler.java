package com.uidmadapp.urgenceidentification.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uidmadapp.urgenceidentification.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nyaina on 11/04/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "uidlocal";
    private static final String TABLE_USER = "user";
    private static final String KEY_ID_PATIENT = "id_patient";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_SEXE = "sexe";
    private static final String KEY_DATE_NAISSANCE = "date_naissance";
    private static final String KEY_ADRESSE = "adresse";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CONTACT_URGENCE = "contact_urgence";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FB_ID = "fb_id";
    private static final String KEY_PHOTO = "photo";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE "+TABLE_USER+" ( " +
                ""+KEY_ID_PATIENT+" INTEGER PRIMARY KEY, " +
                ""+KEY_NOM+" TEXT, "+
                ""+KEY_PRENOM+" TEXT, "+
                ""+KEY_SEXE+" INTEGER, "+
                ""+KEY_DATE_NAISSANCE+" TEXT, "+
                ""+KEY_ADRESSE+" TEXT, "+
                ""+KEY_CONTACT+" TEXT, "+
                ""+KEY_EMAIL+" TEXT, "+
                ""+KEY_CONTACT_URGENCE+" TEXT, "+
                ""+KEY_PASSWORD+" TEXT, "+
                ""+KEY_FB_ID+" TEXT, "+
                ""+KEY_PHOTO+" TEXT )";
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_PATIENT, user.getId_patient());
        values.put(KEY_NOM, user.getNom());
        values.put(KEY_PRENOM, user.getPrenom());
        values.put(KEY_SEXE, user.getSexe());
        values.put(KEY_DATE_NAISSANCE, user.getDate_naissance());
        values.put(KEY_ADRESSE, user.getAdresse());
        values.put(KEY_CONTACT, user.getContact());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_CONTACT_URGENCE, user.getContact_urgence());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_FB_ID, user.getFb_id());
        values.put(KEY_PHOTO, user.getPhoto());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_ID_PATIENT,
                        KEY_NOM, KEY_PRENOM, KEY_SEXE, KEY_DATE_NAISSANCE, KEY_ADRESSE, KEY_CONTACT, KEY_EMAIL, KEY_CONTACT_URGENCE,KEY_PASSWORD,KEY_FB_ID,KEY_PHOTO }, KEY_ID_PATIENT + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11));
        return user;
    }

    public List<User> getAllUser() {
        List<User> userList = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, user.getNom());
        values.put(KEY_PRENOM, user.getPrenom());
        values.put(KEY_DATE_NAISSANCE, user.getDate_naissance());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_FB_ID, user.getFb_id());

        return db.update(TABLE_USER, values, KEY_ID_PATIENT + " = ?",
                new String[] { String.valueOf(user.getId_patient()) });
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID_PATIENT + " = ?",
                new String[]{String.valueOf(user.getId_patient())});
        db.close();
    }

    public void deleteAllUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
    }

    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
}
