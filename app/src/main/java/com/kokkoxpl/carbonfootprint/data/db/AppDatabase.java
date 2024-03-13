package com.kokkoxpl.carbonfootprint.data.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kokkoxpl.carbonfootprint.data.db.dao.DataValueDao;
import com.kokkoxpl.carbonfootprint.data.db.dao.DataRecordDao;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataRecord;

@Database(
    entities = {DataValue.class, DataRecord.class}, exportSchema = false, version = 1
)

public abstract class AppDatabase extends RoomDatabase {
    private volatile static AppDatabase instance = null;
    public abstract DataValueDao dataValueDao();
    public abstract DataRecordDao dataRecordDao();

    public static synchronized AppDatabase newInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "data.db")
                    .allowMainThreadQueries()
                    .createFromAsset("db_template.db")
                    .build();
        }
        return instance;
    }
}