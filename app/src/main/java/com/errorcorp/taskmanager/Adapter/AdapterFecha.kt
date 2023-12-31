package com.errorcorp.taskmanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Model.CustomDate
import com.errorcorp.taskmanager.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterFecha(private val mValues: ArrayList<CustomDate>, private val ctx: Context) : RecyclerView.Adapter<AdapterFecha.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fecha, parent, false)
        return ViewHolder(view)
    }
    fun getList(): ArrayList<CustomDate>{
        return mValues
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customDate: CustomDate = mValues[position]

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(customDate.date)
        holder.tvfecha.setText(formattedDate)
        if ( customDate.recibido ) {
            holder.ivicon.setImageResource(R.drawable.ic_active)
        } else {
            holder.ivicon.setImageResource(R.drawable.ic_inactive)
        }

        holder.btndelete.setOnClickListener {
            mValues.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivicon: ImageView = view.findViewById(R.id.ivicon)

        val tvfecha: TextView = view.findViewById(R.id.tvfecha)

        val btndelete: MaterialButton = view.findViewById(R.id.btndelete)
    }
}