package com.errorcorp.taskmanager.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.errorcorp.taskmanager.Adapter.AdapterCalendario
import com.errorcorp.taskmanager.Model.CustomDate
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.CustomDialog
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Util
import com.errorcorp.taskmanager.Util.Valor
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import java.util.Date

class opt_calendario : Fragment() {

    //CalendarView
    private lateinit var calendarView: CalendarView
    private lateinit var tvfecha: TextView

    //ArrayList
    private var list_recordatorio = ArrayList<Recordatorio>()

    private val daysSelected = ArrayList<Calendar>()

    private val events = mutableListOf<EventDay>()

    //RecyclerView
    private lateinit var rvList: RecyclerView

    //AdapterRecordatorio
    private lateinit var adapterCalendario: AdapterCalendario

    //Button
    private lateinit var btnadd: MaterialButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_opt_calendario, container, false)

        rvList = view.findViewById(R.id.rvList)

        btnadd = view.findViewById(R.id.btnadd)
        btnadd.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_calendario_to_nav_registrar)
        }

        tvfecha = view.findViewById(R.id.tvfecha)

        calendarView = view.findViewById(R.id.calendarView)

        calendarView.setSelectionBackground(R.drawable.rec_red_orange)

        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val selectedDay = eventDay.calendar

                adapterCalendario.filtradoByFecha(selectedDay)

                val day = selectedDay.get(Calendar.DAY_OF_MONTH)
                val month = selectedDay.get(Calendar.MONTH) + 1
                val year = selectedDay.get(Calendar.YEAR)

                tvfecha.setText(String.format("%02d/%02d/%04d", day, month, year))
            }
        })
        listRecordatorios()
        return view
    }

    fun listRecordatorios() {
        CustomDialog.inicialization(requireContext())
        CustomDialog.showLoad(R.raw.anim_load_record)
        CustomDialog.setAnimationEndListenerSuccess(object : CustomDialog.AnimationEndListener {
            override fun onAnimationEnd() {
                CustomDialog.dismiss()
            }
        })
        FirebaseDatabase.getInstance()
            .getReference("Recordatorio")
            .child(SharedPreferencesManager.getStringValue(Valor.DNI).toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list_recordatorio.clear()

                    for ( snapshotchild in snapshot.children ){
                        val cls: Recordatorio? = snapshotchild.getValue(Recordatorio::class.java)

                        if (cls != null) {
                            list_recordatorio.add(cls)

                            cls.fechasProgramadas.forEach { e:CustomDate ->
                                val calendar = Calendar.getInstance()
                                calendar.time = e.date
                                daysSelected.add(calendar)
                                events.add(EventDay(calendar, Util.getResByCategory(cls.categoria)))
                            }
                        }
                    }
                    calendarView.setHighlightedDays(daysSelected)
                    calendarView.setEvents(events)

                    list_recordatorio.reverse()
                    adapterCalendario = AdapterCalendario(list_recordatorio, requireContext(), rvList)
                    rvList.adapter = adapterCalendario

                    val calendar = Calendar.getInstance()
                    calendar.time = Date()
                    adapterCalendario.filtradoByFecha(calendar)

                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val month = calendar.get(Calendar.MONTH) + 1
                    val year = calendar.get(Calendar.YEAR)

                   tvfecha.setText(String.format("%02d/%02d/%04d", day, month, year))

                    CustomDialog.onSuccess(R.raw.anim_on_download)
                }

                override fun onCancelled(error: DatabaseError) {
                    CustomDialog.onFailed()
                }
            })
    }
}
