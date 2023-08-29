package com.errorcorp.taskmanager.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Adapter.AdapterRecordatorio
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date
import java.util.regex.Pattern

class opt_recordatorio : Fragment() , View.OnClickListener {

    //RecyclerView
    private lateinit var rvList: RecyclerView

    //Button
    private lateinit var btnagregar: Button

    //ArrayList
    private var list_recordatorio: ArrayList<Recordatorio> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_opt_recordatorio, container, false)

        btnagregar = view.findViewById(R.id.btnagregar)
        btnagregar.setOnClickListener(this)

        rvList = view.findViewById(R.id.rvList)

        FirebaseDatabase.getInstance()
            .getReference("Recordatorio")
            .child(SharedPreferencesManager.getStringValue(Valor.DNI).toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list_recordatorio.clear()

                    for (registroSnapshot in snapshot.children){

                        val registro = registroSnapshot.value as? HashMap<*, *> ?: continue

                        val id = registro["id"] as? String ?: ""
                        val titulo = registro["titulo"] as? String ?: ""
                        val descripcion = registro["descripcion"] as? String ?: ""

                        val fechaModificacionSnapshot = registro["fechaModificacion"] as? HashMap<*, *>
                        val fechaModificacionTimestamp = fechaModificacionSnapshot?.get("time") as? Long ?: 0
                        val fechaModificacion = Date(fechaModificacionTimestamp)

                        val fechasProgramadas = ArrayList<Date>()

                        val jsonString = (registro["fechasProgramadas"]).toString()

                        val dateStrings = jsonString.split("}, ").map { it.removePrefix("{").removeSuffix("}") }
                        val dates = dateStrings.mapNotNull { parseDateString(it) }

                        for (date in dates) {
                            fechasProgramadas.add(date)
                        }

                        val recordatorio = Recordatorio(titulo, descripcion, fechaModificacion, fechasProgramadas)
                        recordatorio.fechasProgramadas = fechasProgramadas
                        recordatorio.id = id

                        list_recordatorio.add(recordatorio)

                        val adapterRecordatorio = context?.let { AdapterRecordatorio(list_recordatorio, it, getView()) }
                        rvList.adapter = adapterRecordatorio
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error
                }
            })

        return view
    }


    fun parseDateString(dateString: String): Date? {
        val pattern = Pattern.compile("time=(\\d+)")
        val matcher = pattern.matcher(dateString)

        if (matcher.find()) {
            val timeMillis = matcher.group(1)?.toLongOrNull() ?: return null
            return Date(timeMillis)
        }

        return null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btnagregar) -> {
                Navigation.findNavController(v).navigate(R.id.action_nav_recordatorio_to_nav_registrar)
            }
        }
    }
}