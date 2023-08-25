package com.errorcorp.taskmanager.Activity

import android.animation.Animator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.errorcorp.taskmanager.R
import com.google.firebase.auth.FirebaseAuth

class RecuperarActivity : AppCompatActivity() , View.OnClickListener {

    //Dialog
    private lateinit var dialog: Dialog

    //Lotties
    private lateinit var loadAnimationView: LottieAnimationView
    private lateinit var successAnimationView: LottieAnimationView
    private lateinit var errorAnimationView: LottieAnimationView

    //Button
    private lateinit var btnback: Button
    private lateinit var btnrecuperar: Button

    //Edittext
    private lateinit var etmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar)

        btnback = findViewById<Button>(R.id.btnback)
        btnback.setOnClickListener(this)
        btnrecuperar = findViewById<Button>(R.id.btnrecuperar)
        btnrecuperar.setOnClickListener(this)

        etmail = findViewById<EditText>(R.id.etmail)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btnback) -> {
                goToActivity(LoginActivity())
            }
            (R.id.btnrecuperar) -> {
                dialog = Dialog(this)
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
                        goToActivity(LoginActivity())
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

                recuperarPass()
            }
        }
    }

    fun recuperarPass(){
        if(
            etmail.text.toString().isNotEmpty()
        ) {
            dialog.setCancelable(false)
            dialog.show()

            FirebaseAuth.getInstance().sendPasswordResetEmail(etmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val toast = Toast.makeText(this, "El correo de recuperación se envió correctamente", Toast.LENGTH_SHORT)
                        toast.show()

                        loadAnimationView.pauseAnimation()
                        loadAnimationView.visibility = View.GONE
                        successAnimationView.visibility = View.VISIBLE
                        successAnimationView.playAnimation()
                    } else {
                        val toast = Toast.makeText(this, "Hubo un error al enviar el correo de recuperación", Toast.LENGTH_SHORT)
                        toast.show()

                        loadAnimationView.pauseAnimation()
                        loadAnimationView.visibility = View.GONE
                        errorAnimationView.visibility = View.VISIBLE
                        errorAnimationView.playAnimation()
                    }
                }
        } else {
            if(etmail.text.toString().isEmpty()){
                etmail.setError("Campo obligatorio")
            }
        }

    }

    fun goToActivity(activity: AppCompatActivity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
}