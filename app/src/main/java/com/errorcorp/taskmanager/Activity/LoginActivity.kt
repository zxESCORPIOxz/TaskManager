package com.errorcorp.taskmanager.Activity

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() , View.OnClickListener {

    //Dialog
    private lateinit var dialog: Dialog

    //Lotties
    private lateinit var loadAnimationView: LottieAnimationView
    private lateinit var successAnimationView: LottieAnimationView
    private lateinit var errorAnimationView: LottieAnimationView

    //Button
    private lateinit var btnlogin: Button
    private lateinit var btngoregistrar: Button
    private lateinit var btnrecuperar: Button

    //Edittext
    private lateinit var etmail: EditText
    private lateinit var etpass: EditText

    //CheckBox
    private lateinit var cbrecordarme: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                goToActivity(MainActivity())
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

        btnlogin = findViewById(R.id.btnlogin)
        btnlogin.setOnClickListener(this)
        btngoregistrar = findViewById(R.id.btngoregistrar)
        btngoregistrar.setOnClickListener(this)
        btnrecuperar = findViewById(R.id.btnrecuperar)
        btnrecuperar.setOnClickListener(this)

        etmail = findViewById(R.id.etmail)
        etpass = findViewById(R.id.etpass)

        cbrecordarme = findViewById(R.id.cbrecordarme)
        cbrecordarme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                SharedPreferencesManager.setBooleanValue(Valor.CB_RECORDARME, isChecked)
            } else {
                SharedPreferencesManager.setBooleanValue(Valor.CB_RECORDARME, isChecked)
            }
        }
        if (SharedPreferencesManager.getBooleanValue(Valor.CB_RECORDARME)){
            cbrecordarme.isChecked = true
            etmail.setText(SharedPreferencesManager.getStringValue(Valor.LOG_USER))
            etpass.setText(SharedPreferencesManager.getStringValue(Valor.LOG_PASS))
            validateUser()
        }
    }

    fun goToActivity(activity: AppCompatActivity) {
        val intent = Intent(this, activity::class.java)
        intent.putExtra("mail", etmail.text.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btnlogin) -> {
                validateUser()

            }
            (R.id.btngoregistrar) -> {
                goToActivity(RegistrarActivity())
            }
            (R.id.btnrecuperar) -> {
                goToActivity(RecuperarActivity())
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun validateUser(){
        if(
            etmail.text.toString().isNotEmpty() &&
            etpass.text.toString().isNotEmpty()
        ){
            dialog.setCancelable(false)
            dialog.show()

            if(SharedPreferencesManager.getBooleanValue(Valor.CB_RECORDARME))
                SharedPreferencesManager.setStringValue(Valor.LOG_USER, etmail.text.toString())
                SharedPreferencesManager.setStringValue(Valor.LOG_PASS, etpass.text.toString())

            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                etmail.text.toString(),
                etpass.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val toast = Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT)
                        toast.show()
                        
                        loadAnimationView.pauseAnimation()
                        loadAnimationView.visibility = View.GONE
                        successAnimationView.visibility = View.VISIBLE
                        successAnimationView.playAnimation()
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidUserException) {
                            etmail.setError("El correo no está registrado")

                            loadAnimationView.pauseAnimation()
                            loadAnimationView.visibility = View.GONE
                            errorAnimationView.visibility = View.VISIBLE
                            errorAnimationView.playAnimation()
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            etpass.setError("La contraseña es incorrecta")

                            loadAnimationView.pauseAnimation()
                            loadAnimationView.visibility = View.GONE
                            errorAnimationView.visibility = View.VISIBLE
                            errorAnimationView.playAnimation()
                        } catch (e: Exception) {
                            // Otro tipo de error
                            dialog.dismiss()
                        }
                    }
                }
        } else {
            if(etmail.text.toString().isEmpty()){
                etmail.setError("Campo obligatorio")
            }
            if(etpass.text.toString().isEmpty()){
                etpass.setError("Campo obligatorio")
            }else if(etpass.text.length >= 8){
                etpass.setError("Logitud minima de 8")
            }
        }
    }
}