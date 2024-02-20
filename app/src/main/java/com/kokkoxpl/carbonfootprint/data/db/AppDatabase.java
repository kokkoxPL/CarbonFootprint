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
    public abstract DataValueDao appDao();
    public abstract DataRecordDao recordDao();

    public static AppDatabase newInstance(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "data.db")
                .createFromAsset("db_template.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}