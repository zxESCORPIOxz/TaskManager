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
import com.errorcorp.taskmanager.Util.CustomDialog
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() , View.OnClickListener {

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

        CustomDialog.inicialization(this)
        CustomDialog.setAnimationEndListenerSuccess(object : CustomDialog.AnimationEndListener {
            override fun onAnimationEnd() {
                CustomDialog.dismiss()
                goToActivity(MainActivity())
            }
        })
        CustomDialog.setAnimationEndListenerFailed(object : CustomDialog.AnimationEndListener {
            override fun onAnimationEnd() {
                CustomDialog.dismiss()
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
            CustomDialog.showLoad()

            if(SharedPreferencesManager.getBooleanValue(Valor.CB_RECORDARME))
                SharedPreferencesManager.setStringValue(Valor.LOG_USER, etmail.text.toString())
                SharedPreferencesManager.setStringValue(Valor.LOG_PASS, etpass.text.toString())

            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                etmail.text.toString(),
                etpass.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        
                        CustomDialog.onSuccess()
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidUserException) {
                            etmail.setError("El correo no está registrado")

                            CustomDialog.onFailed()
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            etpass.setError("La contraseña es incorrecta")

                            CustomDialog.onFailed()
                        } catch (e: Exception) {
                            CustomDialog.onFailed()
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