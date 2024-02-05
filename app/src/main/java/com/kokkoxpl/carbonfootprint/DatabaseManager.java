package com.kokkoxpl.carbonfootprint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // Database Information
    public static final String DB_NAME = "TEST.DB";
    public static final int DB_VERSION = 2;
    public final String ID = "id";

    public final String DATA_TABLE = "data";
    public final String DATA_NAME = "name";
    public final String DATA_COST = "cost";

    public final String RECORDS_TABLE = "records";
    public final String RECORDS_ID_OF_DATA = "data_id";
    public final String RECORDS_QUANTITY = "quantity";
    public final String RECORDS_DATE = "date";

    private DatabaseHelper databaseHelper;
    private final Context context;
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public DatabaseManager open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        // Queries
        private final String CREATE_DATA_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s FLOAT);",
                DATA_TABLE, ID, DATA_NAME, DATA_COST);

        private final String CREATE_RECORDS_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INT NOT NULL, %s INT, %s DATE NOT NULL);",
                RECORDS_TABLE, ID, RECORDS_ID_OF_DATA, RECORDS_QUANTITY, RECORDS_DATE);

        private final String FILL_DATA_TABLE = String.format("INSERT INTO %s (%s, %s) VALUES " +
                "('TikTok', 2.63), ('Twitch', 0.55), ('Facebook', 0.79), ('Reddit', 2.48), ('Twitter', 0.60)",
                DATA_TABLE, DATA_NAME, DATA_COST);

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DATA_TABLE);
            db.execSQL(CREATE_RECORDS_TABLE);
            db.execSQL(FILL_DATA_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + RECORDS_TABLE);
            onCreate(db);
        }
    }

    public List<Data> getData()
    {
        List<Data> data = new ArrayList<>();
        Cursor cursor = db.query(DATA_TABLE, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            data.add(new Data(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getFloat(2)));
        }

        cursor.close();
        return data;
    }

    public List<Record> getRecords(String date)
    {
        List<Record> records = new ArrayList<>();
        Cursor cursor = db.query(RECORDS_TABLE, null, String.format("%s = ?", RECORDS_DATE),
                new String[]{date}, null, null, null);

        // Do if no records
        if(!cursor.moveToFirst()) {
            insertNewRecords(date);
            cursor = db.query(RECORDS_TABLE, null, String.format("%s = ?", RECORDS_DATE),
                    new String[]{date}, null, null, null);
        }

        while (cursor.moveToNext()) {
            records.add(new Record(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3)));
        }

        cursor.close();
        return records;
    }

    public void insertNewRecords(String date) {
        List<Data> data = getData();

        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();

            // Default values
            contentValues.put(RECORDS_QUANTITY, 0);
            contentValues.put(RECORDS_DATE, date);

            for (Data value : data) {
                contentValues.put(RECORDS_ID_OF_DATA, value.getID());
                db.insert(RECORDS_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateRecords(List<Record> records) {
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();

            for (Record value : records) {
                contentValues.put(RECORDS_QUANTITY, value.getQuantity());
                db.update(RECORDS_TABLE, contentValues,
                        String.format("%s = %s", ID, value.getId()), null);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}