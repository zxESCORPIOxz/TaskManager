package com.errorcorp.taskmanager.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Model.Archivo
import com.errorcorp.taskmanager.Model.CustomDate
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.CustomDialog
import com.errorcorp.taskmanager.Util.MyApplication
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Util
import com.errorcorp.taskmanager.Util.Valor
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterArchivo(private val mValues: ArrayList<Archivo>, private val ctx: Context) : RecyclerView.Adapter<AdapterArchivo.ViewHolder>() {


    //Dialog
    private lateinit var dialog: Dialog

    private var mValuesMaster: ArrayList<Archivo> = ArrayList(mValues)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val archivo: Archivo = mValues[position]

        holder.tvtitle.setText(archivo.nombre+"."+archivo.extension)
        setIcon(archivo.extension, holder)
        holder.tvsize.setText(archivo.sizeFormat)
        holder.ctview.setOnClickListener {
            CustomDialog.inicialization(ctx)
            CustomDialog.showLoad(R.raw.anim_url_load)
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
                    archivo.url = uri.toString()

                    CustomDialog.dismiss()

                    dialoginfo(archivo, position)
                }
                .addOnFailureListener {
                    CustomDialog.onFailed()
                }
        }
    }

    fun filtradoByCategoria(filtro: Int?) {
        mValues.clear()
        mValues.addAll(mValuesMaster)
        if(filtro != R.drawable.ic_all) {
            val modelaux = ArrayList<Archivo>()
            modelaux.addAll(mValues)
            mValues.clear()
            for (i in 0 until modelaux.size) {
                if (Util.getResByExtension(modelaux.get(i).extension) == filtro) {
                    mValues.add(modelaux[i])
                }
            }
        }
        notifyDataSetChanged()
    }
    fun dialoginfo(archivo: Archivo, position: Int){
        dialog = Dialog(ctx)
        dialog.setContentView(R.layout.dialog_file)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ivicon:ImageView = dialog.findViewById(R.id.ivicon)
        setIconDialog(archivo.extension, ivicon)

        val tvtitle:TextView = dialog.findViewById(R.id.tvtitle)
        tvtitle.setText(archivo.nombre+"."+archivo.extension)
        val tvsize:TextView = dialog.findViewById(R.id.tvsize)
        tvsize.setText(archivo.sizeFormat)
        val tvurl:TextView = dialog.findViewById(R.id.tvurl)
        tvurl.setText(archivo.url)

        val btnopen:MaterialButton = dialog.findViewById(R.id.btnopen)
        btnopen.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(archivo.url))
            ctx.startActivity(intent)
        }

        val btncopy:MaterialButton = dialog.findViewById(R.id.btncopy)
        btncopy.setOnClickListener {
            val clipboardManager = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Texto Copiado", archivo.url)
            clipboardManager.setPrimaryClip(clipData)
            Util.showToast("Copiado")
        }

        val btnshare:MaterialButton = dialog.findViewById(R.id.btnshare)
        btnshare.setOnClickListener {
            compartirTexto(archivo.url)
        }

        val btndelete:MaterialButton = dialog.findViewById(R.id.btndelete)
        btndelete.setOnClickListener {
            dialog.dismiss()
            CustomDialog.inicialization(ctx)
            CustomDialog.setAnimationEndListenerSuccess(object : CustomDialog.AnimationEndListener {
                override fun onAnimationEnd() {
                    MyApplication.getDatabaseInstance().archivoDao().deleteArchivo(archivo)
                    mValues.removeAt(position)
                    notifyDataSetChanged()
                    CustomDialog.dismiss()
                    dialog.dismiss()
                }
            })
            CustomDialog.setAnimationEndListenerFailed(object : CustomDialog.AnimationEndListener {
                override fun onAnimationEnd() {
                    CustomDialog.dismiss()
                    dialog.show()
                }
            })
            CustomDialog.showLoad(R.raw.anim_delete)

            val archivoABorrar = FirebaseStorage.getInstance().reference
                .child("files/"+SharedPreferencesManager.getStringValue(Valor.DNI)+"/"+archivo.nombre)

            archivoABorrar.delete()
                .addOnSuccessListener(OnSuccessListener<Void> {
                    CustomDialog.onSuccess()
                })
                .addOnFailureListener(OnFailureListener {
                    CustomDialog.onFailed()
                })
        }

        val btnclose:MaterialButton = dialog.findViewById(R.id.btnclose)
        btnclose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }
    private fun setIconDialog(contentType: String?, ivicon: ImageView) {
        ivicon.setImageResource(Util.getResByExtension(contentType))
    }
    private fun setIcon(contentType: String?, holder: ViewHolder) {
        holder.ivicon.setImageResource(Util.getResByExtension(contentType))
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
    private fun compartirTexto(texto: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, texto)

        val activities: List<ResolveInfo> = ctx.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        if (activities.isNotEmpty()) {
            val chooser = Intent.createChooser(intent, "Compartir con")
            ctx.startActivity(chooser)
        }
    }

    fun filtradoByText(filtro: String?) {
        mValues.clear()
        mValues.addAll(mValuesMaster)
        if (filtro!!.isNotEmpty()) {
            val modelaux = ArrayList<Archivo>()
            modelaux.addAll(mValues)
            mValues.clear()
            for (i in 0 until modelaux.size) {
                if (modelaux.get(i).nombre.contains(filtro)) {
                    mValues.add(modelaux[i])
                }
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ctview: CardView = view.findViewById(R.id.ctview)
        val ivicon: ImageView = view.findViewById(R.id.ivicon)
        val tvtitle: TextView = view.findViewById(R.id.tvtitle)
        val tvsize: TextView = view.findViewById(R.id.tvsize)
    }
}