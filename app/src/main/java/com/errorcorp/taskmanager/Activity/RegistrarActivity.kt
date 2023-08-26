package com.errorcorp.taskmanager.Activity

import android.animation.Animator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.Valor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class RegistrarActivity : AppCompatActivity() , View.OnClickListener {

    //Dialog
    private lateinit var dialog: Dialog

    //Lotties
    private lateinit var loadAnimationView: LottieAnimationView
    private lateinit var successAnimationView: LottieAnimationView
    private lateinit var errorAnimationView: LottieAnimationView

    //Button
    private lateinit var btnback: Button
    private lateinit var btnregistrarme: Button

    //Edittext
    private lateinit var etdni: EditText
    private lateinit var etfullname: EditText
    private lateinit var etphone: EditText
    private lateinit var etmail: EditText
    private lateinit var etpass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        btnback = findViewById<Button>(R.id.btnback)
        btnback.setOnClickListener(this)
        btnregistrarme = findViewById<Button>(R.id.btnregistrarme)
        btnregistrarme.setOnClickListener(this)

        etdni = findViewById<EditText>(R.id.etdni)
        etfullname = findViewById<EditText>(R.id.etfullname)
        etphone = findViewById<EditText>(R.id.etphone)
        etmail = findViewById<EditText>(R.id.etmail)
        etpass = findViewById<EditText>(R.id.etpass)
    }

    fun goToActivity(activity: AppCompatActivity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            (R.id.btnback) -> {
                goToActivity(LoginActivity())
            }
            R.id.btnregistrarme -> {
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

                saveCredentials()
            }
        }
    }

    private fun saveCredentials(){
        if(
            etdni.text.toString().isNotEmpty() &&
            etfullname.text.toString().isNotEmpty() &&
            etphone.text.toString().isNotEmpty() &&
            etmail.text.toString().isNotEmpty() &&
            etpass.text.toString().isNotEmpty()
        ){
            dialog.setCancelable(false)
            dialog.show()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                etmail.text.toString(),
                etpass.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveUserFireStore()
                } else {
                    val toast = Toast.makeText(this, "No se pudo guardar el usuario", Toast.LENGTH_SHORT)
                    toast.show()

                    loadAnimationView.pauseAnimation()
                    loadAnimationView.visibility = View.GONE
                    errorAnimationView.visibility = View.VISIBLE
                    errorAnimationView.playAnimation()
                }
            }
        } else {
            if(etdni.text.toString().isEmpty()){
                etdni.setError("Campo obligatorio")
            }else if(etdni.text.length == 8){
                etdni.setError("Logitud minima y maxima de 8")
            }
            if(etfullname.text.toString().isEmpty()){
                etfullname.setError("Campo obligatorio")
            }
            if(etphone.text.toString().isEmpty()){
                etphone.setError("Campo obligatorio")
            }
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

    private fun saveUserFireStore(){

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                val toast = Toast.makeText(this, "No se pudo guardar el usuario", Toast.LENGTH_SHORT)
                toast.show()

                loadAnimationView.pauseAnimation()
                loadAnimationView.visibility = View.GONE
                errorAnimationView.visibility = View.VISIBLE
                errorAnimationView.playAnimation()
            }

            val userData = hashMapOf(
                "provider" to "Android",
                "dni" to etdni.text.toString(),
                "fullname" to etfullname.text.toString(),
                "phone" to etphone.text.toString(),
                "mail" to etmail.text.toString(),
                "token" to task.result
            )

            FirebaseFirestore.getInstance()
                .collection(Valor.USUARIOS)
                .document(etmail.text.toString())
                .set(userData)
                .addOnSuccessListener {
                    val toast = Toast.makeText(this, "Registro exitoso!!!", Toast.LENGTH_SHORT)
                    toast.show()

                    loadAnimationView.pauseAnimation()
                    loadAnimationView.visibility = View.GONE
                    successAnimationView.visibility = View.VISIBLE
                    successAnimationView.playAnimation()
                }
                .addOnFailureListener { e ->
                    val toast = Toast.makeText(this, "No se pudo guardar el usuario", Toast.LENGTH_SHORT)
                    toast.show()

                    loadAnimationView.pauseAnimation()
                    loadAnimationView.visibility = View.GONE
                    errorAnimationView.visibility = View.VISIBLE
                    errorAnimationView.playAnimation()
                }
        }
    }
}