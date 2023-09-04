package com.errorcorp.taskmanager.Util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.errorcorp.taskmanager.Model.Archivo
import com.errorcorp.taskmanager.Model.BarCode
import com.errorcorp.taskmanager.Util.Dao.ArchivoDao
import com.errorcorp.taskmanager.Util.Dao.BarCodeDao

@Database(entities = [Archivo::class, BarCode::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun archivoDao(): ArchivoDao
    abstract fun barcodeDao(): BarCodeDao
}
