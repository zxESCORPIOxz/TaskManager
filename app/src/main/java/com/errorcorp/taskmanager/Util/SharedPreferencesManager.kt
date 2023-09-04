package com.errorcorp.taskmanager.Util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val APP_SETTINGS_FILE = "TASKMASTER"

    private fun getSharedPreferences(): SharedPreferences {
        return MyApplication.getContext().getSharedPreferences(APP_SETTINGS_FILE, Context.MODE_PRIVATE)
    }

    fun setStringValue(key: String, value: String) {
        val editor = getSharedPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setIntValue(key: String, value: Int) {
        val editor = getSharedPreferences().edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun setBooleanValue(key: String, value: Boolean) {
        val editor = getSharedPreferences().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getStringValue(key: String): String? {
        return getSharedPreferences().getString(key, null)
    }

    fun getBooleanValue(key: String): Boolean {
        return getSharedPreferences().getBoolean(key, false)
    }

    fun getIntValue(key: String): Int {
        return getSharedPreferences().getInt(key, 0)
    }

    fun getIdNotfication(key: String): Int {
        var id: Int = 0
        id = getIntValue(key) + 1
        setIntValue(key, id)
        return id
    }

    fun getNameScanned(format: Int): String {
        var id: Int = 0
        id = getIntValue("numscanned") + 1
        setIntValue("numscanned", id)
        if (Util.isFormat2D(format))
            return "QR Escaneado " + id
        else
            return "BarCode escaneado " + id
    }
    fun getNameCreate(): String {
        var id: Int = 0
        id = getIntValue("numcreate") + 1
        setIntValue("numcreate", id)
        return "QR Creado " + id
    }
}
