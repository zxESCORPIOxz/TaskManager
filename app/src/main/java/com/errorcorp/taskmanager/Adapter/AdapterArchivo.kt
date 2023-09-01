package com.errorcorp.taskmanager.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Model.Archivo
import com.errorcorp.taskmanager.Model.CustomDate
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.CustomDialog
import com.errorcorp.taskmanager.Util.MyApplication
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.google.android.material.button.MaterialButton
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterArchivo(private val mValues: ArrayList<Archivo>, private val ctx: Context) : RecyclerView.Adapter<AdapterArchivo.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val archivo: Archivo = mValues[position]
        var url: String = ""

        holder.tvtitle.setText(archivo.nombre+"."+archivo.extension)
        setIcon(archivo.extension, holder)
        holder.tvsize.setText(archivo.sizeFormat)
        holder.ctview.setOnClickListener {
            CustomDialog.inicialization(ctx)
            CustomDialog.showLoad()
            CustomDialog.setAnimationEndListenerSuccess(object : CustomDialog.AnimationEndListener {
                override fun onAnimationEnd() {
                    CustomDialog.dismiss()

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    ctx.startActivity(intent)
                }
            })
            CustomDialog.setAnimationEndListenerFailed(object : CustomDialog.AnimationEndListener {
                override fun onAnimationEnd() {
                    CustomDialog.dismiss()
                }
            })
            val archivoRef = FirebaseStorage.getInstance()
                .reference
                .child("files/"+SharedPreferencesManager.getStringValue(Valor.DNI)+"/"+archivo.nombre)
            archivoRef.downloadUrl
                .addOnSuccessListener { uri ->
                    url = uri.toString()
                    CustomDialog.onSuccess()
                }
                .addOnFailureListener {
                    CustomDialog.onFailed()
                }
        }
    }

    private fun setIcon(contentType: String?, holder: ViewHolder) {
        when (contentType) {
            "pdf" -> {
                holder.ivicon.setImageResource(R.drawable.ic_pdf)
            }
            "doc" -> {
                holder.ivicon.setImageResource(R.drawable.ic_word)
            }
            "docx" -> {
                holder.ivicon.setImageResource(R.drawable.ic_word)
            }
            "xls" -> {
                holder.ivicon.setImageResource(R.drawable.ic_excel)
            }
            "xlsx" -> {
                holder.ivicon.setImageResource(R.drawable.ic_excel)
            }
            "ppt" -> {
                holder.ivicon.setImageResource(R.drawable.ic_power_point)
            }
            "pptx" -> {
                holder.ivicon.setImageResource(R.drawable.ic_power_point)
            }
            "zip" -> {
                holder.ivicon.setImageResource(R.drawable.ic_zip)
            }
            "rar" -> {
                holder.ivicon.setImageResource(R.drawable.ic_rar)
            }
            "svg" -> {
                holder.ivicon.setImageResource(R.drawable.ic_imagenes)
            }
            "png" -> {
                holder.ivicon.setImageResource(R.drawable.ic_imagenes)
            }
            "jpg" -> {
                holder.ivicon.setImageResource(R.drawable.ic_imagenes)
            }
            "apk" -> {
                holder.ivicon.setImageResource(R.drawable.ic_apk)
            }
            "txt" -> {
                holder.ivicon.setImageResource(R.drawable.ic_txt)
            }
            else -> {
                holder.ivicon.setImageResource(R.drawable.ic_file)
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ctview: CardView = view.findViewById(R.id.ctview)

        val ivicon: ImageView = view.findViewById(R.id.ivicon)

        val tvtitle: TextView = view.findViewById(R.id.tvtitle)
        val tvsize: TextView = view.findViewById(R.id.tvsize)
    }
}