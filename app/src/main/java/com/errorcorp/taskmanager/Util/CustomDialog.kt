package com.errorcorp.taskmanager.Util

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.errorcorp.taskmanager.R

object CustomDialog {

    //Dialog
    private lateinit var dialog: Dialog

    //Lotties
    private lateinit var loadAnimationView: LottieAnimationView
    private lateinit var successAnimationView: LottieAnimationView
    private lateinit var errorAnimationView: LottieAnimationView

    private var animationEndListenerSuccess: AnimationEndListener? = null
    private var animationEndListenerFailed: AnimationEndListener? = null

    fun inicialization(ctx:Context) {
        dialog = Dialog(ctx)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setCancelable(false)

        loadAnimationView = dialog.findViewById<LottieAnimationView>(R.id.lfload)
        successAnimationView = dialog.findViewById<LottieAnimationView>(R.id.lfsuccess)
        errorAnimationView = dialog.findViewById<LottieAnimationView>(R.id.lferror)

        successAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                animationEndListenerSuccess?.onAnimationEnd()
            }
        })
        errorAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                animationEndListenerFailed?.onAnimationEnd()
            }
        })
    }

    fun showLoad(){
        loadAnimationView.visibility = View.VISIBLE
        successAnimationView.visibility = View.GONE
        errorAnimationView.visibility = View.GONE
        dialog.show()
    }

    fun dismiss() {
        loadAnimationView.visibility = View.VISIBLE
        successAnimationView.visibility = View.GONE
        errorAnimationView.visibility = View.GONE
        dialog.dismiss()
    }

    fun onSuccess(){
        loadAnimationView.visibility = View.GONE
        successAnimationView.visibility = View.VISIBLE
        successAnimationView.playAnimation()
    }

    fun onFailed(){
        loadAnimationView.visibility = View.GONE
        errorAnimationView.visibility = View.VISIBLE
        errorAnimationView.playAnimation()
    }

    interface AnimationEndListener {
        fun onAnimationEnd()
    }

    fun setAnimationEndListenerSuccess(listener: AnimationEndListener) {
        animationEndListenerSuccess = listener
    }

    fun setAnimationEndListenerFailed(listener: AnimationEndListener) {
        animationEndListenerFailed = listener
    }
}