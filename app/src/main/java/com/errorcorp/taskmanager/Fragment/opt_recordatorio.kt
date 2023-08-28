package com.errorcorp.taskmanager.Fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Activity.RecuperarActivity
import com.errorcorp.taskmanager.Activity.RegistrarActivity
import com.errorcorp.taskmanager.Adapter.AdapterRecordatorio
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import java.util.Date

class opt_recordatorio : Fragment() , View.OnClickListener {

    //RecyclerView
    private lateinit var rvList: RecyclerView

    //Button
    private lateinit var btnagregar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_opt_recordatorio, container, false)

        btnagregar = view.findViewById(R.id.btnagregar)
        btnagregar.setOnClickListener(this)

        rvList = view.findViewById(R.id.rvList)
        val list_recordatorio: ArrayList<Recordatorio> = ArrayList()
        val list_date: ArrayList<Date> = ArrayList()
        list_date.add(Date())
        list_recordatorio.add(Recordatorio("Title1", "Descripcion1", Date(), list_date))
        list_recordatorio.add(Recordatorio("Title2", "Descripcion2", Date(), list_date))
        list_recordatorio.add(Recordatorio("Title3", "Descripcion3", Date(), list_date))
        list_recordatorio.add(Recordatorio("Title4", "Descripcion4", Date(), list_date))

        val adapterRecordatorio = context?.let { AdapterRecordatorio(list_recordatorio, it) }
        rvList.adapter = adapterRecordatorio

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btnagregar) -> {
                Navigation.findNavController(v).navigate(R.id.action_nav_recordatorio_to_nav_registrar)
            }
        }
    }
}