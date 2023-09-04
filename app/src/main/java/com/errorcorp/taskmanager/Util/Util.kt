package com.errorcorp.taskmanager.Util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.errorcorp.taskmanager.R
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_AZTEC
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_DATA_MATRIX
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_PDF417
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE

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
    fun isFormat2D(formato: Int): Boolean {
        val formatos2D = FORMAT_DATA_MATRIX or
                FORMAT_QR_CODE or
                FORMAT_PDF417 or
                FORMAT_AZTEC

        return (formatos2D and formato) != 0
    }
    fun getColorByCategory(contentType: String?):Int{
        when(contentType){
            ("other") -> {
                return ContextCompat.getColor(MyApplication.getContext(), R.color.gray_middle)
            }
            ("office") -> {
                return ContextCompat.getColor(MyApplication.getContext(), R.color.pink)
            }
            ("gmail") -> {
                return ContextCompat.getColor(MyApplication.getContext(), R.color.red)
            }
            ("github") -> {
                return ContextCompat.getColor(MyApplication.getContext(), R.color.black)
            }
            ("drive") -> {
                return ContextCompat.getColor(MyApplication.getContext(), R.color.yellow)
            }
            ("whatsapp") -> {
                return ContextCompat.getColor(MyApplication.getContext(), R.color.green)
            }
            ("facebook") -> {
                return ContextCompat.getColor(MyApplication.getContext(), R.color.blue)
            }
            else ->
                return ContextCompat.getColor(MyApplication.getContext(), R.color.gray_middle)
        }
    }
    fun expand(v: View) {
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        // Versiones anteriores de Android (antes de API 21) cancelan animaciones para vistas con una altura de 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Velocidad de expansión de 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()*3
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = (initialHeight * (1 - interpolatedTime)).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Velocidad de colapso de 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()*3
        v.startAnimation(a)
    }
}