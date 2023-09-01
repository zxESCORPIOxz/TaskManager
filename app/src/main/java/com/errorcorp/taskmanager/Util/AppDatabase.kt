package com.errorcorp.taskmanager.Util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.errorcorp.taskmanager.Model.Archivo
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.Util.Dao.ArchivoDao

@Database(entities = [Archivo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun archivoDao(): ArchivoDao
}
