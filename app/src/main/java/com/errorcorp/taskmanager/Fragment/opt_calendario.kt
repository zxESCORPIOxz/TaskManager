package com.errorcorp.taskmanager.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.errorcorp.taskmanager.Model.CustomDate
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.CustomDialog
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Util
import com.errorcorp.taskmanager.Util.Valor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class opt_calendario : Fragment() {

    //CalendarView
    private lateinit var calendarView: CalendarView

    //ArrayList
    private var list_recordatorio = ArrayList<Recordatorio>()

    private val daysSelected = ArrayList<Calendar>()
    private val events = mutableListOf<EventDay>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_opt_calendario, container, false)

        calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        calendarView.setSelectionBackground(R.drawable.rec_red_orange)

        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val selectedDay = eventDay.calendar
                daysSelected.add(selectedDay)

                calendarView.setHighlightedDays(daysSelected)

                val day = selectedDay.get(Calendar.DAY_OF_MONTH)
                val month = selectedDay.get(Calendar.MONTH) + 1
                val year = selectedDay.get(Calendar.YEAR)

                val formattedDate = String.format("%02d/%02d/%04d", day, month, year)

                Util.showToast(formattedDate+" - "+calendarView.selectedDates.size)
            }
        })
        listRecordatorios()
        return view
    }

    fun listRecordatorios() {
        CustomDialog.inicialization(requireContext())
        CustomDialog.showLoad(R.raw.anim_load_record)
        FirebaseDatabase.getInstance()
            .getReference("Recordatorio")
            .child(SharedPreferencesManager.getStringValue(Valor.DNI).toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list_recordatorio.clear()

                    Log.i("DATOS", snapshot.toString())

                    for ( snapshotchild in snapshot.children ){
                        val cls: Recordatorio? = snapshotchild.getValue(Recordatorio::class.java)

                        if (cls != null) {
                            list_recordatorio.add(cls)

                            cls.fechasProgramadas.forEach { e:CustomDate ->
                                val calendar = Calendar.getInstance()
                                calendar.time = e.date
                                Util.showToast( e.date.year.toString() + "-" + e.date.month + "-" + e.date.day)
                                daysSelected.add(calendar)

                                events.add(EventDay(calendar, Util.getResByCategory(cls.categoria)))
                            }
                        }
                    }
                    calendarView.setHighlightedDays(daysSelected)
                    calendarView.setEvents(events)
                    CustomDialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    CustomDialog.onFailed()
                }
            })
    }
}
