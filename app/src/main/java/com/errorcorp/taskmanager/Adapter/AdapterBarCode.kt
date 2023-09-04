package com.errorcorp.taskmanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Model.BarCode
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.MyApplication
import com.errorcorp.taskmanager.Util.Util
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AdapterBarCode(

    private val mValues: ArrayList<BarCode>,
    private val ctx: Context
    ) : RecyclerView.Adapter<AdapterBarCode.ViewHolder>() {

    private var mValuesMaster: ArrayList<BarCode> = ArrayList(mValues)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_codigo_qr, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val barcode = mValues[position]
        holder.tvname.setText(barcode.nombre)
        holder.tvcontenido.setText(barcode.valor)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(barcode.datemilles))
        holder.tvdate.setText(formattedDate)

        if(barcode.origen == BarCode.ORG_SCANED){
            if(Util.isFormat2D(barcode.format)){
                holder.ivicon.setImageResource(R.drawable.ic_qr_scan)
            } else {
                holder.ivicon.setImageResource(R.drawable.ic_bar_scan)
            }
            holder.vwidentity.setBackgroundColor(
                ContextCompat.getColor(MyApplication.getContext(), R.color.pink)
            )
        } else {
            if(Util.isFormat2D(barcode.format)){
                holder.ivicon.setImageResource(R.drawable.ic_qr_create)
            } else {
                holder.ivicon.setImageResource(R.drawable.ic_bar_create)
            }
            holder.vwidentity.setBackgroundColor(
                ContextCompat.getColor(MyApplication.getContext(), R.color.blue)
            )
        }

        holder.btnshow.setOnClickListener {
            if (holder.ctextras.visibility == View.VISIBLE){
                Util.collapse(holder.ctextras)
                holder.btnshow.setIconResource(R.drawable.ic_arrow_out_down)
            } else {
                Util.expand(holder.ctextras)
                holder.btnshow.setIconResource(R.drawable.ic_arrow_out_up)
            }
        }
        holder.btnopen.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vwidentity: View = view.findViewById(R.id.vwidentity)

        val ivicon: ImageView = view.findViewById(R.id.ivicon)

        val tvname: TextView = view.findViewById(R.id.tvname)
        val tvdate: TextView = view.findViewById(R.id.tvdate)
        val tvcontenido: TextView = view.findViewById(R.id.tvcontenido)

        val btnshow: MaterialButton  = view.findViewById(R.id.btnshow)
        val btnopen: MaterialButton  = view.findViewById(R.id.btnopen)

        val ctextras: LinearLayout = view.findViewById(R.id.ctextras)
    }
}