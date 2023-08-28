package com.errorcorp.taskmanager.Fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.Navigation
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class frg_registrar : Fragment() , View.OnClickListener {

    //Button
    private lateinit var btnregistrar: Button

    //EditText
    private lateinit var ettitle: EditText
    private lateinit var etdescription: EditText

    //TextView
    private lateinit var tvfecha: TextView

    //Objetos
    private lateinit var recordatorio: Recordatorio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_frg_registrar, container, false)

        ettitle = view.findViewById(R.id.ettitle)
        etdescription = view.findViewById(R.id.etdescription)
        tvfecha = view.findViewById(R.id.tvfecha)
        tvfecha.setOnClickListener(this)
        btnregistrar = view.findViewById(R.id.btnregistrar)
        btnregistrar.setOnClickListener(this)

        recordatorio = Recordatorio()

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btnregistrar) -> {

            }
            (R.id.tvfecha) -> {
                openDateTimeSelectionDialog()
            }
        }
    }

    private fun openDateTimeSelectionDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        var selectedDate: Date? = null

        // Date Picker Dialog
        val datePickerDialog = context?.let {
            DatePickerDialog(it, { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)

                // Time Picker Dialog
                val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedCalendar.set(Calendar.MINUTE, minute)

                    selectedDate = selectedCalendar.time

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val formattedDateTime = dateFormat.format(selectedDate)

                    var fecha_select:Date
                    fecha_select = selectedDate as Date
                    recordatorio.fechasProgramadas.add(fecha_select)
                    if (tvfecha.text.isNotEmpty()){
                        tvfecha.setText(tvfecha.text.toString() + "\n" + formattedDateTime)
                    } else {
                        tvfecha.setText(formattedDateTime)
                    }
                }, currentHour, currentMinute, true)

                timePickerDialog.setTitle("Seleccione una hora")
                timePickerDialog.show()
                val btntime1: Button? = timePickerDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
                btntime1?.setTextColor(Color.WHITE)
                val btntime2: Button? = timePickerDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)
                btntime2?.setTextColor(Color.WHITE)
            }, currentYear, currentMonth, currentDay)
        }
        datePickerDialog?.setTitle("Seleccione una fecha")
        datePickerDialog?.datePicker?.minDate = System.currentTimeMillis() - 1000
        datePickerDialog?.show()
        val btndate1: Button? = datePickerDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
        btndate1?.setTextColor(Color.WHITE)
        val btndate2: Button? = datePickerDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)
        btndate2?.setTextColor(Color.WHITE)
    }
}