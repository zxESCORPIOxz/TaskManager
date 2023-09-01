package com.errorcorp.taskmanager.Util

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.room.Room

class MyApplication : Application() {
    companion object {
        private lateinit var INSTANCE: MyApplication
        private lateinit var DATABASE_INSTANCE: AppDatabase

        fun getInstance(): MyApplication {
            return INSTANCE
        }

        fun getContext(): Context {
            return INSTANCE
        }

        fun goURL(url:String) {
            val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            getContext().startActivity(openUrlIntent)
        }

        fun getDatabaseInstance(): AppDatabase {
            return DATABASE_INSTANCE
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        DATABASE_INSTANCE = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "taskmasterdb")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}