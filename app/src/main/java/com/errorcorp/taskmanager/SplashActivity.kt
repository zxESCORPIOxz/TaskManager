package com.errorcorp.taskmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout

class SplashActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val animatedView = findViewById<RelativeLayout>(R.id.contanim)
//        val animationDrawable = animatedView.background as AnimationDrawable
//        animationDrawable.start()

        val element = findViewById<RelativeLayout>(R.id.element)

        val animation = AnimationUtils.loadAnimation(this, R.anim.animation)

        element.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                goToActivity(MainActivity())
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }

    fun goToActivity(activity: AppCompatActivity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

}