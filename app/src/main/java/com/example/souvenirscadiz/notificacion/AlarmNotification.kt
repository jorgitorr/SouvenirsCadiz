package com.example.souvenirscadiz.notificacion

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.souvenirscadiz.MainActivity

class AlarmNotification: BroadcastReceiver() {
    companion object{
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, p1: Intent?) {
        createSimpleNotification(context)
    }

    private fun createSimpleNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = NotificationCompat.Builder(context, MainActivity.MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle("My title")
            .setContentText("Esto es un ejemplo <3")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Has recibido nuevos souvenirs")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}