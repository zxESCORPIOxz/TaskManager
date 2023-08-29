package com.errorcorp.taskmanager.Fragment

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.errorcorp.taskmanager.Activity.MainActivity
import com.errorcorp.taskmanager.Adapter.AdapterFecha
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class frg_registrar : Fragment() , View.OnClickListener {

    //Dialog
    private lateinit var dialog: Dialog

    //Lotties
    private lateinit var loadAnimationView: LottieAnimationView
    private lateinit var successAnimationView: LottieAnimationView
    private lateinit var errorAnimationView: LottieAnimationView


    //RecyclerView
    private lateinit var rvFechas: RecyclerView

    //Adapters
    private lateinit var adapterFecha: AdapterFecha

    //Button
    private lateinit var btnregistrar: Button
    private lateinit var btnaddfecha: MaterialButton

    //EditText
    private lateinit var ettitle: EditText
    private lateinit var etdescription: EditText

    //Objetos
    private lateinit var recordatorio: Recordatorio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_frg_registrar, container, false)
        if ( arguments?.getString("ACTION").equals("ORIGIN") ){
            recordatorio = Valor.RECORDATORIO
        } else {
            recordatorio = Recordatorio()
        }

        dialog = context?.let { Dialog(it) }!!
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        loadAnimationView = dialog.findViewById<LottieAnimationView>(R.id.lfload)
        successAnimationView = dialog.findViewById<LottieAnimationView>(R.id.lfsuccess)
        errorAnimationView = dialog.findViewById<LottieAnimationView>(R.id.lferror)

        successAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                dialog.dismiss()
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_registrar_to_nav_recordatorio)
            }
        })
        errorAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                dialog.dismiss()
            }
        })

        rvFechas = view.findViewById(R.id.rvFechas)

        ettitle = view.findViewById(R.id.ettitle)
        ettitle.setText(recordatorio.titulo)
        etdescription = view.findViewById(R.id.etdescription)
        etdescription.setText(recordatorio.descripcion)

        btnaddfecha = view.findViewById(R.id.btnaddfecha)
        btnaddfecha.setOnClickListener(this)
        btnregistrar = view.findViewById(R.id.btnregistrar)
        btnregistrar.setOnClickListener(this)

        adapterFecha = context?.let { AdapterFecha(recordatorio.fechasProgramadas, it) }!!
        rvFechas.adapter = adapterFecha

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btnregistrar) -> {
                if (ettitle.text.isNotEmpty() &&
                    etdescription.text.isNotEmpty() &&
                    adapterFecha.getList().size != 0){
                    dialog.setCancelable(false)
                    dialog.show()
                    if (recordatorio.id.isEmpty())
                        recordatorio.id = FirebaseDatabase.getInstance().getReference("Recordatorio").push().getKey().toString()
                    recordatorio.titulo = ettitle.text.toString()
                    recordatorio.descripcion = etdescription.text.toString()
                    recordatorio.fechaModificacion = Date()
                    recordatorio.fechasProgramadas = adapterFecha.getList()

                    FirebaseDatabase.getInstance()
                        .getReference("Recordatorio")
                        .child(SharedPreferencesManager.getStringValue(Valor.DNI).toString())
                        .child(recordatorio.id)
                        .setValue(recordatorio)
                        .addOnSuccessListener {
                            loadAnimationView.pauseAnimation()
                            loadAnimationView.visibility = View.GONE
                            successAnimationView.visibility = View.VISIBLE
                            successAnimationView.playAnimation()
                        }
                        .addOnFailureListener {
                            loadAnimationView.pauseAnimation()
                            loadAnimationView.visibility = View.GONE
                            errorAnimationView.visibility = View.VISIBLE
                            errorAnimationView.playAnimation()
                        }
                } else {
                    if (ettitle.text.isEmpty()) {
                        ettitle.setError("Campo obligatorio")
                    }
                    if (etdescription.text.isEmpty()) {
                        etdescription.setError("Campo obligatorio")
                    }
                    if (adapterFecha.getList().size == 0) {
                        val toast = Toast.makeText(context, "Seleccione al menos una fecha", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            }
            (R.id.btnaddfecha) -> {
                openDateTimeSelectionDialog()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openDateTimeSelectionDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        var selectedDate: Date? = null

        val datePickerDialog = context?.let {
            DatePickerDialog(it, { _, year, month, day ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, day)

                val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedCalendar.set(Calendar.MINUTE, minute)

                    selectedDate = selectedCalendar.time

                    val fecha_select:Date
                    fecha_select = selectedDate as Date
                    recordatorio.fechasProgramadas.add(fecha_select)
                    adapterFecha.notifyDataSetChanged()

                }, currentHour, currentMinute, true)

                timePickerDialog.setTitle("Seleccione una hora")
                timePickerDialog.show()
                val btntime1: Button? = timePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                btntime1?.setTextColor(Color.WHITE)
                val btntime2: Button? = timePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
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