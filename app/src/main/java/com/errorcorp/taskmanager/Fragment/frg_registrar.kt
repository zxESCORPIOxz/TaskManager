package com.errorcorp.taskmanager.Fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Adapter.AdapterFecha
import com.errorcorp.taskmanager.Model.CustomDate
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.CustomDialog
import com.errorcorp.taskmanager.Util.NotificationReceiver
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Date

class frg_registrar : Fragment() , View.OnClickListener {

    //RecyclerView
    private lateinit var rvFechas: RecyclerView

    //Adapters
    private lateinit var adapterFecha: AdapterFecha

    //Button
    private lateinit var btnregistrar: Button
    private lateinit var btnaddfecha: MaterialButton

    //ImageView
    private lateinit var ivother: ImageView
    private lateinit var ivoffice: ImageView
    private lateinit var ivgmail: ImageView
    private lateinit var ivgithub: ImageView
    private lateinit var ivdrive: ImageView
    private lateinit var ivwhatsapp: ImageView
    private lateinit var ivfacebook: ImageView

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


        CustomDialog.inicialization(requireContext())
        CustomDialog.setAnimationEndListenerSuccess(object : CustomDialog.AnimationEndListener {
            override fun onAnimationEnd() {
                CustomDialog.dismiss()
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_registrar_to_nav_recordatorio)
            }
        })
        CustomDialog.setAnimationEndListenerFailed(object : CustomDialog.AnimationEndListener {
            override fun onAnimationEnd() {
                CustomDialog.dismiss()
            }
        })

        ivother = view.findViewById(R.id.ivother)
        ivother.setOnClickListener(this)
        ivoffice = view.findViewById(R.id.ivoffice)
        ivoffice.setOnClickListener(this)
        ivgmail = view.findViewById(R.id.ivgmail)
        ivgmail.setOnClickListener(this)
        ivgithub = view.findViewById(R.id.ivgithub)
        ivgithub.setOnClickListener(this)
        ivdrive = view.findViewById(R.id.ivdrive)
        ivdrive.setOnClickListener(this)
        ivwhatsapp = view.findViewById(R.id.ivwhatsapp)
        ivwhatsapp.setOnClickListener(this)
        ivfacebook = view.findViewById(R.id.ivfacebook)
        ivfacebook.setOnClickListener(this)

        rvFechas = view.findViewById(R.id.rvFechas)

        ettitle = view.findViewById(R.id.ettitle)
        etdescription = view.findViewById(R.id.etdescription)

        btnaddfecha = view.findViewById(R.id.btnaddfecha)
        btnaddfecha.setOnClickListener(this)
        btnregistrar = view.findViewById(R.id.btnregistrar)
        btnregistrar.setOnClickListener(this)

        if ( arguments?.getString("ACTION").equals("ORIGIN") ){
            recordatorio = Valor.RECORDATORIO
            when(recordatorio.categoria){
                ("other") -> {
                    ivother.setBackgroundResource(R.drawable.ripple_butons_category_on)
                }
                ("office") -> {
                    ivoffice.setBackgroundResource(R.drawable.ripple_butons_category_on)
                }
                ("gmail") -> {
                    ivgmail.setBackgroundResource(R.drawable.ripple_butons_category_on)
                }
                ("github") -> {
                    ivgithub.setBackgroundResource(R.drawable.ripple_butons_category_on)
                }
                ("drive") -> {
                    ivdrive.setBackgroundResource(R.drawable.ripple_butons_category_on)
                }
                ("whatsapp") -> {
                    ivwhatsapp.setBackgroundResource(R.drawable.ripple_butons_category_on)
                }
                ("facebook") -> {
                    ivfacebook.setBackgroundResource(R.drawable.ripple_butons_category_on)
                }
            }

        } else {
            recordatorio = Recordatorio()
            recordatorio.categoria = "other"
            ivother.setBackgroundResource(R.drawable.ripple_butons_category_on)
        }

        ettitle.setText(recordatorio.titulo)
        etdescription.setText(recordatorio.descripcion)

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
                    CustomDialog.showLoad()
                    if (recordatorio.id.isEmpty())
                        recordatorio.id = FirebaseDatabase.getInstance().getReference("Recordatorio").push().getKey().toString()
                    recordatorio.titulo = ettitle.text.toString()
                    recordatorio.descripcion = etdescription.text.toString()
                    recordatorio.fechaModificacion = Date()
                    recordatorio.fechasProgramadas = adapterFecha.getList()

                    recordatorio.fechasProgramadas.forEachIndexed { index, date: CustomDate ->
                        if (!date.recibido){
                            val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                            val notificationIntent = Intent(requireContext(), NotificationReceiver::class.java)

                            notificationIntent.putExtra("dni", SharedPreferencesManager.getStringValue(Valor.DNI).toString())
                            notificationIntent.putExtra("id", recordatorio.id)
                            notificationIntent.putExtra("positio", index.toString())
                            notificationIntent.putExtra("titulo", recordatorio.titulo)
                            notificationIntent.putExtra("descripcion", recordatorio.descripcion)
                            notificationIntent.putExtra("fecha", recordatorio.fechaModificacion.toString())
                            notificationIntent.putExtra("categoria", recordatorio.categoria)

                            val pendingIntent = PendingIntent.getBroadcast(
                                requireContext(),
                                SharedPreferencesManager.getIdNotfication(Valor.ID_NOTIFICATION),
                                notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                            )

                            val futureTimeInMillis = date.date.time
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                futureTimeInMillis,
                                pendingIntent
                            )
                        }
                    }

                    FirebaseDatabase.getInstance()
                        .getReference("Recordatorio")
                        .child(SharedPreferencesManager.getStringValue(Valor.DNI).toString())
                        .child(recordatorio.id)
                        .setValue(recordatorio)
                        .addOnSuccessListener {
                            CustomDialog.onSuccess()
                        }
                        .addOnFailureListener {
                            CustomDialog.onFailed()
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
            (R.id.ivother) -> {
                cleanSelectedCategory()
                recordatorio.categoria = "other"
                ivother.setBackgroundResource(R.drawable.ripple_butons_category_on)
            }
            (R.id.ivoffice) -> {
                cleanSelectedCategory()
                recordatorio.categoria = "office"
                ivoffice.setBackgroundResource(R.drawable.ripple_butons_category_on)
            }
            (R.id.ivgmail) -> {
                cleanSelectedCategory()
                recordatorio.categoria = "gmail"
                ivgmail.setBackgroundResource(R.drawable.ripple_butons_category_on)
            }
            (R.id.ivgithub) -> {
                cleanSelectedCategory()
                recordatorio.categoria = "github"
                ivgithub.setBackgroundResource(R.drawable.ripple_butons_category_on)
            }
            (R.id.ivdrive) -> {
                cleanSelectedCategory()
                recordatorio.categoria = "drive"
                ivdrive.setBackgroundResource(R.drawable.ripple_butons_category_on)
            }
            (R.id.ivwhatsapp) -> {
                cleanSelectedCategory()
                recordatorio.categoria = "whatsapp"
                ivwhatsapp.setBackgroundResource(R.drawable.ripple_butons_category_on)
            }
            (R.id.ivfacebook) -> {
                cleanSelectedCategory()
                recordatorio.categoria = "facebook"
                ivfacebook.setBackgroundResource(R.drawable.ripple_butons_category_on)
            }
        }
    }
    fun cleanSelectedCategory(){
        ivother.setBackgroundResource(R.drawable.ripple_butons_category)
        ivoffice.setBackgroundResource(R.drawable.ripple_butons_category)
        ivgmail.setBackgroundResource(R.drawable.ripple_butons_category)
        ivgithub.setBackgroundResource(R.drawable.ripple_butons_category)
        ivdrive.setBackgroundResource(R.drawable.ripple_butons_category)
        ivwhatsapp.setBackgroundResource(R.drawable.ripple_butons_category)
        ivfacebook.setBackgroundResource(R.drawable.ripple_butons_category)
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
                    selectedCalendar.set(Calendar.SECOND, 0)

                    selectedDate = selectedCalendar.time

                    val fecha_select:CustomDate = CustomDate()
                    fecha_select.date = selectedDate as Date
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