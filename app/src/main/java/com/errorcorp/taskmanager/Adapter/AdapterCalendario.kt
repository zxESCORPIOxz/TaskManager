package com.errorcorp.taskmanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.Util
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AdapterCalendario(

    private val mValues: ArrayList<Recordatorio>,
    private val ctx: Context,
    private val rvlist: RecyclerView?
    ) : RecyclerView.Adapter<AdapterCalendario.ViewHolder>() {

    private var mValuesMaster: ArrayList<Recordatorio> = ArrayList(mValues)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar, parent, false)
        return ViewHolder(view)
    }

    fun filtradoByFecha(filtro: Calendar?) {
        mValues.clear()
        mValues.addAll(mValuesMaster)
        if (filtro != null) {
            val modelaux = ArrayList<Recordatorio>()
            modelaux.addAll(mValues)
            mValues.clear()
            for (i in 0 until modelaux.size) {
                for(it in modelaux.get(i).fechasProgramadas){
                    if (compare_fechas(filtro, it.date)) {
                        mValues.add(modelaux[i])
                        break
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun compare_fechas(calendar: Calendar?, date: Date): Boolean {
        val calendar2 = Calendar.getInstance()
        calendar2.time = date

        return (calendar!![Calendar.YEAR] == calendar2[Calendar.YEAR] &&
                calendar[Calendar.MONTH] == calendar2[Calendar.MONTH] &&
                calendar[Calendar.DAY_OF_MONTH] == calendar2[Calendar.DAY_OF_MONTH])
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

        holder.vwidentity.setBackgroundColor(Util.getColorByCategory(recordatorio.categoria))

        holder.btnshow.setOnClickListener {
            if (holder.ctextras.visibility == View.VISIBLE){
                Util.collapse(holder.ctextras)
                holder.btnshow.setIconResource(R.drawable.ic_arrow_out_down)
            } else {
                Util.expand(holder.ctextras)
                holder.btnshow.setIconResource(R.drawable.ic_arrow_out_up)
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vwidentity: View = view.findViewById(R.id.vwidentity)

        val ivicon: ImageView = view.findViewById(R.id.ivicon)

        val tvtitle: TextView = view.findViewById(R.id.tvtitle)
        val tvdate: TextView = view.findViewById(R.id.tvdate)
        val tvdescription: TextView = view.findViewById(R.id.tvdescription)

        val rvFechas: RecyclerView = view.findViewById(R.id.rvFechas)

        val btnshow: MaterialButton  = view.findViewById(R.id.btnshow)

        val ctextras: LinearLayout = view.findViewById(R.id.ctextras)

        val ctcontent: RelativeLayout = view.findViewById(R.id.ctcontent)
    }
}