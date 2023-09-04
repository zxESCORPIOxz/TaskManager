package com.errorcorp.taskmanager.Util.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.errorcorp.taskmanager.Model.BarCode

@Dao
interface BarCodeDao {
    @Insert
    fun insertBarCode(barcode: BarCode)

    @Update
    fun updateBarCode(barcode: BarCode)

    @Delete
    fun deleteBarCode(barcode: BarCode)

    @Query("SELECT * FROM barcode")
    fun getAllBarCode(): List<BarCode>

    @Transaction
    @Query("SELECT COUNT(*) FROM barcode")
    fun getCount(): Int
}