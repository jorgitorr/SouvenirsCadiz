package com.example.souvenirscadiz.notificacion

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.souvenirscadiz.MainActivity

/**
 * Carrito notification
 *
 * @constructor Create empty Carrito notification
 */
class CarritoNotification: BroadcastReceiver() {
    companion object{
        const val NOTIFICATION_ID = 1
    }

    /**
     * On receive
     *
     * @param context
     * @param p1
     */
    override fun onReceive(context: Context, p1: Intent?) {
        createSimpleNotification(context)
    }

    /**
     * Create simple notification
     *
     * @param context
     */
    private fun createSimpleNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = NotificationCompat.Builder(context, MainActivity.MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle("SOUVENIRS CADIZ")
            .setContentText("Tienes souvenirs pendientes en el carrito")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Tiene souvenirs pendientes en el carrito")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID,notification)
    }


}