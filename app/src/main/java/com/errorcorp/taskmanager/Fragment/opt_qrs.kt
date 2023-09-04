package com.errorcorp.taskmanager.Fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.errorcorp.taskmanager.Fragment.qr.qr_opt_create
import com.errorcorp.taskmanager.Fragment.qr.qr_opt_history
import com.errorcorp.taskmanager.Fragment.qr.qr_opt_scanner
import com.errorcorp.taskmanager.R
import com.google.android.material.button.MaterialButton

class opt_qrs : Fragment() {

    //Imageview
    private lateinit var ivlector: MaterialButton
    private lateinit var ivhistory: MaterialButton
    private lateinit var ivcreate: MaterialButton

    //FrameLayout
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_opt_qrs, container, false)

        val fragmentManager = childFragmentManager

        ivlector = view.findViewById(R.id.ivlector)
        ivhistory = view.findViewById(R.id.ivhistory)
        ivcreate = view.findViewById(R.id.ivcreate)
        fragmentContainer = view.findViewById(R.id.fragmentContainer)

        ivlector.setOnClickListener {
            clarSelected()
            ivlector.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            replaceFragment(fragmentManager, qr_opt_scanner(), "FragmentA")
        }

        ivhistory.setOnClickListener {
            clarSelected()
            ivhistory.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            replaceFragment(fragmentManager, qr_opt_history(), "FragmentB")
        }

        ivcreate.setOnClickListener {
            clarSelected()
            ivcreate.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
            replaceFragment(fragmentManager, qr_opt_create(), "FragmentC")
        }

        ivlector.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.orange))
        replaceFragment(fragmentManager, qr_opt_scanner(), "FragmentA")

        return view
    }

    private fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment, tag: String) {
        val transaction = fragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            R.anim.slide_in_up,
            R.anim.slide_out_down
        )

        transaction.replace(R.id.fragmentContainer, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun clarSelected() {
        ivlector.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.gray_dark))
        ivhistory.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.gray_dark))
        ivcreate.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.gray_dark))
    }
}