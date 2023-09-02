package com.errorcorp.taskmanager.Fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Activity.MainActivity
import com.errorcorp.taskmanager.Adapter.AdapterRecordatorio
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

class opt_recordatorio : Fragment() , View.OnClickListener {

    //SearchView
    private lateinit var svbuscar: SearchView

    //RecyclerView
    private lateinit var rvList: RecyclerView

    //AdapterRecordatorio
    private lateinit var adapterRecordatorio: AdapterRecordatorio

    //Button
    private lateinit var btnagregar: Button

    //ArrayList
    private var list_recordatorio: ArrayList<Recordatorio> = ArrayList()

    //ImageView
    private lateinit var ivall: ImageView
    private lateinit var ivother: ImageView
    private lateinit var ivoffice: ImageView
    private lateinit var ivgmail: ImageView
    private lateinit var ivgithub: ImageView
    private lateinit var ivdrive: ImageView
    private lateinit var ivwhatsapp: ImageView
    private lateinit var ivfacebook: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_opt_recordatorio, container, false)



        CustomDialog.inicialization(requireContext())
        CustomDialog.setAnimationEndListenerSuccess(object : CustomDialog.AnimationEndListener {
            override fun onAnimationEnd() {
                CustomDialog.dismiss()
            }
        })
        CustomDialog.setAnimationEndListenerFailed(object : CustomDialog.AnimationEndListener {
            override fun onAnimationEnd() {
                listRecordatorios(view)
            }
        })

        ivall = view.findViewById(R.id.ivall)
        ivall.setOnClickListener(this)
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

        btnagregar = view.findViewById(R.id.btnagregar)
        btnagregar.setOnClickListener(this)

        rvList = view.findViewById(R.id.rvList)

        listRecordatorios(view)

        return view
    }

    fun listRecordatorios(view:View) {
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
                        }
                    }
                    list_recordatorio.reverse()
                    adapterRecordatorio = AdapterRecordatorio(list_recordatorio, requireContext(), getView())
                    rvList.adapter = adapterRecordatorio

                    svbuscar = view.findViewById(R.id.svbuscar)
                    svbuscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            cleanSelectedCategory()
                            ivall.setBackgroundResource(R.drawable.ripple_butons_category)
                            adapterRecordatorio.filtradoByText(newText)
                            return false
                        }
                    })

                    CustomDialog.onSuccess(R.raw.anim_on_download)
                }

                override fun onCancelled(error: DatabaseError) {
                    CustomDialog.onFailed()
                }
            })
    }

    override fun onClick(v: View?) {
        svbuscar.setQuery("", false)
        svbuscar.isIconified = true
        Util.hideKeyboard(requireView())
        when (v?.id) {
            (R.id.btnagregar) -> {
                Navigation.findNavController(v).navigate(R.id.action_nav_recordatorio_to_nav_registrar)
            }
            (R.id.ivall) -> {
                cleanSelectedCategory()
                adapterRecordatorio.filtradoByCategoria("all")
                ivall.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivother) -> {
                cleanSelectedCategory()
                adapterRecordatorio.filtradoByCategoria("other")
                ivother.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivoffice) -> {
                cleanSelectedCategory()
                adapterRecordatorio.filtradoByCategoria("office")
                ivoffice.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivgmail) -> {
                cleanSelectedCategory()
                adapterRecordatorio.filtradoByCategoria("gmail")
                ivgmail.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivgithub) -> {
                cleanSelectedCategory()
                adapterRecordatorio.filtradoByCategoria("github")
                ivgithub.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivdrive) -> {
                cleanSelectedCategory()
                adapterRecordatorio.filtradoByCategoria("drive")
                ivdrive.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivwhatsapp) -> {
                cleanSelectedCategory()
                adapterRecordatorio.filtradoByCategoria("whatsapp")
                ivwhatsapp.setBackgroundResource(R.drawable.ripple_butons_category)
            }
            (R.id.ivfacebook) -> {
                cleanSelectedCategory()
                adapterRecordatorio.filtradoByCategoria("facebook")
                ivfacebook.setBackgroundResource(R.drawable.ripple_butons_category)
            }
        }
    }
    fun cleanSelectedCategory(){
        ivall.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivother.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivoffice.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivgmail.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivgithub.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivdrive.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivwhatsapp.setBackgroundResource(R.drawable.ripple_butons_category_on)
        ivfacebook.setBackgroundResource(R.drawable.ripple_butons_category_on)
    }
}