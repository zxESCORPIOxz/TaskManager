package com.errorcorp.taskmanager.Util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.errorcorp.taskmanager.R
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission", "RemoteViewLayout")
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Recordatorio", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        var res:Int = 0
        when(intent.getStringExtra("categoria")){
            ("other") -> {
                res = (R.drawable.ic_other)
            }
            ("office") -> {
                res = (R.drawable.ic_office)
            }
            ("gmail") -> {
                res = (R.drawable.ic_gmail)
            }
            ("github") -> {
                res = (R.drawable.ic_github)
            }
            ("drive") -> {
                res = (R.drawable.ic_drive)
            }
            ("whatsapp") -> {
                res = (R.drawable.ic_whatsapp)
            }
            ("facebook") -> {
                res = (R.drawable.ic_facebook)
            }
        }

        val notificationLayout = RemoteViews(context.packageName, R.layout.notification_small)

        notificationLayout.setTextViewText(R.id.tvtitle, intent.getStringExtra("titulo"))

        val dateFormatfull = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date: Date = dateFormatfull.parse(intent.getStringExtra("fecha")!!)!!
        val formattedDate = dateFormat.format(date)
        notificationLayout.setTextViewText(R.id.tvfecha, formattedDate)
        notificationLayout.setImageViewResource(R.id.ivcategoria, res)

        val notificationLayoutExpanded = RemoteViews(context.packageName, R.layout.notification_expanded)

        notificationLayoutExpanded.setTextViewText(R.id.tvtitle, intent.getStringExtra("titulo"))
        notificationLayoutExpanded.setTextViewText(R.id.tvfecha, intent.getStringExtra("fecha"))
        notificationLayoutExpanded.setTextViewText(R.id.tvdescription, intent.getStringExtra("descripcion"))
        notificationLayoutExpanded.setImageViewResource(R.id.ivcategoria, res)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID).also {
            it.setSmallIcon(R.drawable.ic_notification)
            it.setCustomContentView(notificationLayout)
            it.setCustomBigContentView(notificationLayoutExpanded)
        }.build()

        val ID_NOTIF = SharedPreferencesManager.getIdNotfication(Valor.ID_NOTIFICATION)

        notificationManager.notify(ID_NOTIF, notification)

        FirebaseDatabase.getInstance()
            .getReference("Recordatorio")
            .child(intent.getStringExtra("dni").toString())
            .child(intent.getStringExtra("id").toString())
            .child("fechasProgramadas")
            .child(intent.getStringExtra("positio").toString())
            .child("recibido")
            .setValue(true)
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    companion object {
        private const val CHANNEL_ID = "10"
    }
}
