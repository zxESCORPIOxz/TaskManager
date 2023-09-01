package com.errorcorp.taskmanager.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.errorcorp.taskmanager.Model.Archivo
import com.errorcorp.taskmanager.R
import com.errorcorp.taskmanager.Util.MyApplication
import com.errorcorp.taskmanager.Util.NotificationReceiver
import com.errorcorp.taskmanager.Util.SharedPreferencesManager
import com.errorcorp.taskmanager.Util.Valor
import com.errorcorp.taskmanager.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.System.exit
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //TextView
    private lateinit var tvhora: TextView
    private lateinit var tvfecha: TextView
    private lateinit var tvname: TextView
    private lateinit var tvmail: TextView

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable


    @SuppressLint("RemoteViewLayout", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val header = navView.getHeaderView(0) as RelativeLayout
        tvhora = header.findViewById(R.id.tvhora)
        tvfecha = header.findViewById(R.id.tvfecha)
        tvname = header.findViewById(R.id.tvname)
        tvmail = header.findViewById(R.id.tvmail)
        tvmail.setText(intent.getStringExtra("mail"))
        FirebaseFirestore.getInstance()
            .collection(Valor.USUARIOS)
            .document(intent.getStringExtra("mail").toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    val fullname = userData?.get("fullname")
                    SharedPreferencesManager.setStringValue(Valor.DNI, userData?.get("dni").toString())
                    tvname.setText(fullname.toString())
                } else {
                    tvname.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                tvname.visibility = View.GONE
            }

        handler = Handler()
        updateClock()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val formattedDate = dateFormat.format(currentDate)

        tvfecha.text = formattedDate

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_inicio,
                R.id.nav_recordatorio,
                R.id.nav_archivos,
                R.id.nav_calendario,
                R.id.nav_alarma,
                R.id.nav_musica,
                R.id.nav_configuracion
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun updateClock() {
        runnable = Runnable {
            val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentTime = Calendar.getInstance().time
            val formattedTime = dateFormat.format(currentTime)

            tvhora.text = formattedTime

            handler.postDelayed(runnable, 1000)
        }
        handler.post(runnable)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.opt_closesesion -> {
                val auth = FirebaseAuth.getInstance()
                auth.signOut()

                SharedPreferencesManager.setBooleanValue(Valor.CB_RECORDARME, false)
                SharedPreferencesManager.setStringValue(Valor.LOG_USER, "")
                SharedPreferencesManager.setStringValue(Valor.LOG_PASS, "")

                goToActivity(LoginActivity())
            }
            R.id.opt_exit -> {
                finish()
                exit(0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun goToActivity(activity: AppCompatActivity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
}