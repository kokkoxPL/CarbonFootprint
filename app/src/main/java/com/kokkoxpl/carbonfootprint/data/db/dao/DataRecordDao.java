package com.kokkoxpl.carbonfootprint.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kokkoxpl.carbonfootprint.data.db.entities.DataRecord;

import java.util.List;

@Dao
public interface DataRecordDao {
    @Insert()
    void insertRecords(DataRecord... record);

    @Update
    void updateRecords(DataRecord... records);

    @Query("SELECT * FROM data_records")
    List<DataRecord> getRecords();

    @Query("SELECT * FROM data_records WHERE date BETWEEN :fromDate AND :toDate")
    List<DataRecord> getRecordsByDate(String fromDate, String toDate);
}