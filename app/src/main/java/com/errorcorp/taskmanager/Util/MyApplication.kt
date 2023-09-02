package com.errorcorp.taskmanager.Util

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.room.Room
import com.errorcorp.taskmanager.Activity.LoginActivity
import com.errorcorp.taskmanager.Activity.SplashActivity
import com.errorcorp.taskmanager.R

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