package com.errorcorp.taskmanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterRecordatorio(private val mValues: ArrayList<Recordatorio>, private val ctx: Context) : RecyclerView.Adapter<AdapterRecordatorio.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recordatorio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recordatorio = mValues[position]
        holder.tvtitle.setText(recordatorio.titulo)
        holder.tvdescription.setText(recordatorio.descripcion)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(recordatorio.fechaModificacion)
        holder.tvdate.setText(formattedDate)

        holder.btnshow.setOnClickListener {
            if (holder.ctextras.visibility == View.VISIBLE){
                holder.ctextras.visibility = View.GONE
                holder.btnshow.setIconResource(R.drawable.ic_arrow_out_down)
            } else {
                holder.ctextras.visibility = View.VISIBLE
                holder.btnshow.setIconResource(R.drawable.ic_arrow_out_up)
            }
        }
    }

    private fun removeAt(position: Int) {
//        try {
//            MyApplication.getDatabaseInstance().codigoDao().delete(mValues[position])
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        mValues.toMutableList().removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, mValues.size)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvtitle: TextView = view.findViewById(R.id.tvtitle)
        val tvdate: TextView = view.findViewById(R.id.tvdate)
        val tvdescription: TextView = view.findViewById(R.id.tvdescription)

        val btnedit: MaterialButton = view.findViewById(R.id.btnedit)
        val btndelete: MaterialButton  = view.findViewById(R.id.btndelete)
        val btnopen: MaterialButton  = view.findViewById(R.id.btnopen)
        val btnshow: MaterialButton  = view.findViewById(R.id.btnshow)

        val ctextras: LinearLayout = view.findViewById(R.id.ctextras)
    }
}