package com.errorcorp.taskmanager.Adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Util
import com.errorcorp.taskmanager.Util.Valor
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterRecordatorio(

    private val mValues: ArrayList<Recordatorio>,
    private val ctx: Context,
    private val view: View?
    ) : RecyclerView.Adapter<AdapterRecordatorio.ViewHolder>() {

    //Dialog
    private lateinit var dialog: Dialog

    private var mValuesMaster: ArrayList<Recordatorio> = ArrayList(mValues)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recordatorio, parent, false)
        return ViewHolder(view)
    }

    fun filtradoByText(filtro: String?) {
        mValues.clear()
        mValues.addAll(mValuesMaster)
        if (filtro!!.isNotEmpty()) {
            val modelaux = ArrayList<Recordatorio>()
            modelaux.addAll(mValues)
            mValues.clear()
            for (i in 0 until modelaux.size) {
                if (modelaux.get(i).titulo.contains(filtro)) {
                    mValues.add(modelaux[i])
                }
            }
        }
        notifyDataSetChanged()
    }
    fun filtradoByCategoria(filtro: String?) {
        mValues.clear()
        mValues.addAll(mValuesMaster)
        if(!filtro.equals("all")) {
            if (filtro!!.isNotEmpty()) {
                val modelaux = ArrayList<Recordatorio>()
                modelaux.addAll(mValues)
                mValues.clear()
                for (i in 0 until modelaux.size) {
                    if (modelaux.get(i).categoria.contains(filtro)) {
                        mValues.add(modelaux[i])
                    }
                }
            }
        }
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recordatorio = mValues[position]
        holder.tvtitle.setText(recordatorio.titulo)
        holder.tvdescription.setText(recordatorio.descripcion)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(recordatorio.fechaModificacion)
        holder.tvdate.setText(formattedDate)

        holder.rvFechas.adapter = AdapterFecha_item(recordatorio.fechasProgramadas, ctx)
        holder.ivicon.setImageResource(Util.getResByCategory(recordatorio.categoria))
        holder.btnshow.setOnClickListener {
            if (holder.ctextras.visibility == View.VISIBLE){
                Util.collapse(holder.ctextras)
                holder.btnshow.setIconResource(R.drawable.ic_arrow_out_down)
            } else {
                Util.expand(holder.ctextras)
                holder.btnshow.setIconResource(R.drawable.ic_arrow_out_up)
            }
        }
        holder.btnedit.setOnClickListener {
            if (view != null) {
                val bundle = Bundle().apply {
                    putString("ACTION", "ORIGIN")
                }
                Navigation.findNavController(view).navigate(R.id.action_nav_recordatorio_to_nav_registrar, bundle)
                Valor.RECORDATORIO = recordatorio
            }
        }
        holder.btndelete.setOnClickListener {
            deleteItem(recordatorio.id, position)
        }
    }

    fun deleteItem(id: String, position: Int){
        dialog = Dialog(ctx)
        dialog.setContentView(R.layout.dialog_delete)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btn_acept:MaterialButton = dialog.findViewById(R.id.btn_acept)
        btn_acept.setOnClickListener {
            FirebaseDatabase.getInstance()
                .getReference("Recordatorio")
                .child(SharedPreferencesManager.getStringValue(Valor.DNI).toString())
                .child(id)
                .removeValue()
                .addOnSuccessListener {
                    dialog.dismiss()

                    val toast = Toast.makeText(ctx, "Se elimino el recordatorio correctamente", Toast.LENGTH_SHORT)
                    toast.show()

                    mValues.removeAt(position)

                    notifyDataSetChanged()
                }
                .addOnFailureListener {
                    dialog.dismiss()

                    val toast = Toast.makeText(ctx, "No se pudo eliminar correctamente", Toast.LENGTH_SHORT)
                    toast.show()
                }
        }

        val btn_cancel:MaterialButton = dialog.findViewById(R.id.btn_cancel)
        btn_cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivicon: ImageView = view.findViewById(R.id.ivicon)

        val tvtitle: TextView = view.findViewById(R.id.tvtitle)
        val tvdate: TextView = view.findViewById(R.id.tvdate)
        val tvdescription: TextView = view.findViewById(R.id.tvdescription)

        val rvFechas: RecyclerView = view.findViewById(R.id.rvFechas)

        val btnedit: MaterialButton = view.findViewById(R.id.btnedit)
        val btndelete: MaterialButton  = view.findViewById(R.id.btndelete)
        val btnshow: MaterialButton  = view.findViewById(R.id.btnshow)

        val ctextras: LinearLayout = view.findViewById(R.id.ctextras)

        val ctitem: RelativeLayout = view.findViewById(R.id.ctitem)
    }
}