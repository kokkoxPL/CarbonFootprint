package com.kokkoxpl.carbonfootprint.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;

import java.util.List;

@Dao
public interface DataValueDao {
    @Insert
    void insertApps(DataValue... dataValues);

    @Query("SELECT * FROM data_values")
    List<DataValue> getApps();
}