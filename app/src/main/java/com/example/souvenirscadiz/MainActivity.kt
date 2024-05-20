package com.example.souvenirscadiz

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.souvenirscadiz.data.util.CloudStorageManager
import com.example.souvenirscadiz.data.util.Constant.Companion.EMAIL_ADMIN
import com.example.souvenirscadiz.navigation.NavManager
import com.example.souvenirscadiz.notificacion.CarritoNotification
import com.example.souvenirscadiz.notificacion.CarritoNotification.Companion.NOTIFICATION_ID
import com.example.souvenirscadiz.notificacion.PedidosNotification
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.SouvenirsCadizTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel = LoginViewModel()
    private val souvenirsViewModel:SouvenirsViewModel = SouvenirsViewModel()
    private val cloudStorageManager: CloudStorageManager = CloudStorageManager()
    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        screenSplash.setKeepOnScreenCondition{false}

        setContent {
            SouvenirsCadizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavManager(souvenirsViewModel, loginViewModel, cloudStorageManager)
                }
            }
        }


    }


    /**
     * Notificacion preparada en el momento de que el usuario tenga más de un elemento en el carrito
     */
    @Composable
    @SuppressLint("ScheduleExactAlarm")
    private fun CarritoNotification() {
        val souvenirsCarrito by souvenirsViewModel.souvenirCarrito.collectAsState()

        if(souvenirsCarrito.isNotEmpty()){
            val intent = Intent(applicationContext, CarritoNotification::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + 15000,
                pendingIntent,
            )
        }
    }



    /**
     * Notificacion preparada en el momento que el administrador tenga un pedido o más
     */
    @Composable
    @SuppressLint("ScheduleExactAlarm")
    private fun PedidosNotification() {
        val souvenirsPedidos by souvenirsViewModel.souvenirPedidos.collectAsState()

        if(souvenirsPedidos.isNotEmpty() && loginViewModel.email == EMAIL_ADMIN){
            val intent = Intent(applicationContext, PedidosNotification::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + 15000,
                pendingIntent,
            )
        }
    }

}
