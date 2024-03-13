package com.kokkoxpl.carbonfootprint.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.MapColumn;
import androidx.room.Query;
import androidx.room.Transaction;

import com.kokkoxpl.carbonfootprint.data.db.entities.DataRecord;

import java.util.List;
import java.util.Map;

@Dao
public interface DataRecordDao {
    @Insert()
    void insertRecord(DataRecord dataRecord);

    @Query("UPDATE data_records SET quantity = :quantity WHERE id = :id AND quantity != :quantity")
    void updateRecord(int id, int quantity);

    @Transaction
    default void updateRecords(List<DataRecord> dataRecords) {
        for (DataRecord dataRecord : dataRecords) {
            if (dataRecord.getId() == 0 && dataRecord.getQuantity() != 0) {
                insertRecord(dataRecord);
            } else {
                updateRecord(dataRecord.getId(), dataRecord.getQuantity());
            }
        }
    }

    @Query("SELECT data_values.id as dataValueId, data_records.* FROM data_values " +
            "JOIN data_records ON data_values.id = data_records.idOfData WHERE data_records.date = :date")
    Map<@MapColumn(columnName = "dataValueId") Integer, DataRecord> getRecordsMapByDate(String date);

    @Query("SELECT data_values.name AS dataValueName, (data_values.cost * SUM(data_records.quantity)) AS dataValueSum FROM data_values " +
            "JOIN data_records ON data_values.id = data_records.idOfData WHERE date BETWEEN :fromDate AND :toDate GROUP BY data_records.idOfData")
    Map<@MapColumn(columnName = "dataValueName") String, @MapColumn(columnName = "dataValueSum") Float> getRecordsMapByDateRange(String fromDate, String toDate);

    @Query("SELECT data_records.date AS dataRecordDate, data_values.name AS dataValueName, (data_values.cost * SUM(data_records.quantity)) AS dataRecordSum " +
            "FROM data_values JOIN data_records ON data_values.id = data_records.idOfData WHERE date BETWEEN :fromDate AND :toDate GROUP BY data_records.idOfData, data_records.date ORDER BY data_records.date")
    Map<@MapColumn(columnName = "dataRecordDate") String, Map<@MapColumn(columnName = "dataValueName") String, @MapColumn(columnName = "dataRecordSum") Float>> getRecordsMapByDateRangeGroup(String fromDate, String toDate);
}