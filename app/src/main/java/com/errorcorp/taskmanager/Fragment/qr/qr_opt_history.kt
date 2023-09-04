package com.errorcorp.taskmanager.Fragment.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.errorcorp.taskmanager.Adapter.AdapterBarCode
import com.errorcorp.taskmanager.Model.BarCode
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.MyApplication

class qr_opt_history : Fragment() {

    //RecyclerView
    private lateinit var rvList: RecyclerView

    private lateinit var adapterBarCode: AdapterBarCode

    private var listBarcode = ArrayList<BarCode>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_qr_opt_history, container, false)

        rvList = view.findViewById(R.id.rvList)

        listBarcode.addAll(
            MyApplication
                .getDatabaseInstance()
                .barcodeDao()
                .getAllBarCode()
        )
        listBarcode.reverse()
        adapterBarCode = AdapterBarCode(
            listBarcode,
            requireContext()
        )
        rvList.adapter = adapterBarCode

        return view
    }
}