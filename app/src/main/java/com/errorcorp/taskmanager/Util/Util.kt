package com.errorcorp.taskmanager.Util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.errorcorp.taskmanager.Adapter.AdapterRecordatorio
import com.errorcorp.taskmanager.Model.Recordatorio
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date
import java.util.regex.Pattern

object Util {
    fun hideKeyboard(view: View) {
        val imm = MyApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}