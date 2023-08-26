package com.errorcorp.taskmanager.Util

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object {
        private lateinit var INSTANCE: MyApplication
//        private lateinit var DATABASE_INSTANCE: AppDatabase

        fun getInstance(): MyApplication {
            return INSTANCE
        }

        fun getContext(): Context {
            return INSTANCE
        }

//        fun getDatabaseInstance(): AppDatabase {
//            return DATABASE_INSTANCE
//        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
//        DATABASE_INSTANCE = Room.databaseBuilder(this, AppDatabase::class.java, "taskmasterdb")
//            .allowMainThreadQueries()
//            .fallbackToDestructiveMigration()
//            .addMigrations(AppDatabase.MIGRATION_1_2)
//            .build()
    }
}