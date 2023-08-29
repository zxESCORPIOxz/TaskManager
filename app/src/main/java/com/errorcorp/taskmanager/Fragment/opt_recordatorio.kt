package com.errorcorp.taskmanager.Fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.errorcorp.taskmanager.Adapter.AdapterRecordatorio
import com.errorcorp.taskmanager.Model.CustomDate
import com.errorcorp.taskmanager.Model.Recordatorio
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONObject
import java.util.Calendar
import java.util.Date
import java.util.regex.Pattern

class opt_recordatorio : Fragment() , View.OnClickListener {

    //Dialog
    private lateinit var dialog: Dialog

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

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        btnagregar = view.findViewById(R.id.btnagregar)
        btnagregar.setOnClickListener(this)

        rvList = view.findViewById(R.id.rvList)

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
                        }
                    }

                    val adapterRecordatorio = context?.let { AdapterRecordatorio(list_recordatorio, it, getView()) }
                    rvList.adapter = adapterRecordatorio

                    dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error
                }
            })

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