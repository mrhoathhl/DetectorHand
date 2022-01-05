package com.monster.handscan.protecthealth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.monster.handscan.protecthealth.model.DayChallengeModel;
import com.monster.handscan.protecthealth.model.ScanHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "users_db";

    // Contact table name
    private static final String TABLE_HISTORY = "historyScan";
    private static final String TABLE_CHALLENGE = "historyChallenge";

    // Country Table Columns names
    private static final String KEY_ID = "id";
    private static final String PERCENT = "percent";
    private static final String TIME = "time";
    private static final String IS_DAY = "isDay";
    private static final String IS_NIGHT = "isNight";
    private static final String TYPE = "type";
    Context context;

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
//        Toast.makeText(context, "SQLiteDBHelper onInit() method is invoked.", Toast.LENGTH_SHORT).show();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        Toast.makeText(context, "SQLiteDBHelper onCreate() method is invoked.", Toast.LENGTH_SHORT).show();
        String CREATE_SCAN_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + PERCENT + " TEXT,"
                + TIME + " TEXT,"
                + IS_DAY + " INTEGER,"
                + IS_NIGHT + " INTEGER" + ")";


        String CREATE_CHALLENGE_TABLE = "CREATE TABLE " + TABLE_CHALLENGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + PERCENT + " TEXT,"
                + TIME + " TEXT,"
                + IS_DAY + " INTEGER,"
                + IS_NIGHT + " INTEGER" + ")";
        db.execSQL(CREATE_CHALLENGE_TABLE);
        db.execSQL(CREATE_SCAN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Toast.makeText(context, "SQLiteDBHelper onUpgrade() method is invoked.", Toast.LENGTH_SHORT).show();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGE);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new historyModel
    public void addScanHistory(ScanHistoryModel historyModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERCENT, historyModel.getPercent()); // Country Name
        values.put(TIME, historyModel.getTime()); // Country Population
        values.put(IS_DAY, historyModel.isDay()); // Country Population
        values.put(IS_NIGHT, historyModel.isNight()); // Country Population

        // Inserting Row
        db.insert(TABLE_HISTORY, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Countries
    public List<ScanHistoryModel> getAllScanHistories() {
        List<ScanHistoryModel> countryList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScanHistoryModel historyModel = new ScanHistoryModel();
                historyModel.setId(Integer.parseInt(cursor.getString(0)));
                historyModel.setPercent(cursor.getString(1));
                historyModel.setTime(cursor.getString(2));
                historyModel.setDay(cursor.getInt(3) == 1);
                historyModel.setNight(cursor.getInt(4) == 1);
                // Adding historyModel to list
                countryList.add(historyModel);
            } while (cursor.moveToNext());
        }

        // return historyModel list
        return countryList;
    }

    // Deleting all countries
    public void deleteAllScanHistories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, null, null);
        db.close();
    }


    // Adding new historyModel
    public void addChallengeHistory(ScanHistoryModel historyModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERCENT, historyModel.getPercent()); // Country Name
        values.put(TIME, historyModel.getTime()); // Country Population
        values.put(IS_DAY, historyModel.isDay()); // Country Population
        values.put(IS_NIGHT, historyModel.isNight()); // Country Population

        // Inserting Row
        db.insert(TABLE_CHALLENGE, null, values);
        db.close(); // Closing database connection
    }

    public List<ScanHistoryModel> getAllHistoriesChallenge() {
        List<ScanHistoryModel> countryList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHALLENGE + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScanHistoryModel historyModel = new ScanHistoryModel();
                historyModel.setId(Integer.parseInt(cursor.getString(0)));
                historyModel.setPercent(cursor.getString(1));
                historyModel.setTime(cursor.getString(2));
                historyModel.setDay(cursor.getInt(3) == 1);
                historyModel.setNight(cursor.getInt(4) == 1);
                // Adding historyModel to list
                countryList.add(historyModel);
            } while (cursor.moveToNext());
        }

        // return historyModel list
        return countryList;
    }

    // Deleting all countries
    public void deleteAllChallengeHistories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHALLENGE, null, null);
        db.close();
    }
}