package com.errorcorp.taskmanager.Util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.errorcorp.taskmanager.Adapter.AdapterRecordatorio
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
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


    fun showToast(text: String) {
        val toast = Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_SHORT)
        toast.show()
    }
    fun networkvalidate(): Boolean {
        val connectivityManager = MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
    }
    fun getResByExtension(contentType: String?):Int{
        when (contentType) {
            "pdf" -> {
                return (R.drawable.ic_pdf)
            }
            "doc" -> {
                return (R.drawable.ic_word)
            }
            "docx" -> {
                return (R.drawable.ic_word)
            }
            "xls" -> {
                return (R.drawable.ic_excel)
            }
            "xlsx" -> {
                return (R.drawable.ic_excel)
            }
            "ppt" -> {
                return (R.drawable.ic_power_point)
            }
            "pptx" -> {
                return (R.drawable.ic_power_point)
            }
            "zip" -> {
                return (R.drawable.ic_zip)
            }
            "rar" -> {
                return (R.drawable.ic_rar)
            }
            "svg" -> {
                return (R.drawable.ic_imagenes)
            }
            "png" -> {
                return (R.drawable.ic_imagenes)
            }
            "jpg" -> {
                return (R.drawable.ic_imagenes)
            }
            "apk" -> {
                return (R.drawable.ic_apk)
            }
            "txt" -> {
                return (R.drawable.ic_txt)
            }
            "mp3" -> {
                return (R.drawable.ic_music)
            }
            "wav" -> {
                return (R.drawable.ic_music)
            }
            "mp4" -> {
                return (R.drawable.ic_video)
            }
            "aab" -> {
                return (R.drawable.ic_aab)
            }
            else -> {
                return (R.drawable.ic_file)
            }
        }
    }

    fun getResByCategory(contentType: String?):Int{
        when(contentType){
            ("other") -> {
                return (R.drawable.ic_other)
            }
            ("office") -> {
                return (R.drawable.ic_office)
            }
            ("gmail") -> {
                return (R.drawable.ic_gmail)
            }
            ("github") -> {
                return (R.drawable.ic_github)
            }
            ("drive") -> {
                return (R.drawable.ic_drive)
            }
            ("whatsapp") -> {
                return (R.drawable.ic_whatsapp)
            }
            ("facebook") -> {
                return (R.drawable.ic_facebook)
            }
            else -> return (R.drawable.ic_tree_points)
        }
    }

}