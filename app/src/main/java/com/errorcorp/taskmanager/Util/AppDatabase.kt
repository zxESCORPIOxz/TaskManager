package com.errorcorp.taskmanager.Util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.errorcorp.taskmanager.Model.Recordatorio

//@Database(entities = [Recordatorio::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
//    abstract fun recordatorioDao(): RecordatorioDao
}
