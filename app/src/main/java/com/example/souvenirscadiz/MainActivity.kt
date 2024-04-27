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
import android.util.Log
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
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.navigation.NavManager
import com.example.souvenirscadiz.notificacion.AlarmNotification
import com.example.souvenirscadiz.notificacion.AlarmNotification.Companion.NOTIFICATION_ID
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.SouvenirsCadizTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.Calendar


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel = LoginViewModel()
    private val souvenirsViewModel:SouvenirsViewModel = SouvenirsViewModel()

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
                    readCSV()
                    SouvenirsAddImages(souvenirsViewModel)
                    NavManager(souvenirsViewModel, loginViewModel)
                }
            }
        }

        createChannel()

        if(getNumberSouvenirsPedidos()>0){
            sheduleNotification()
        }
    }

    //NOTIFICACIONES

    fun getNumberSouvenirsPedidos(): Int{
        souvenirsViewModel.fetchSouvenirsPedido()
        return souvenirsViewModel.souvenirPedidos.value.size
    }

    /**
     * notificacion que se activa con el tiempo
     */
    @SuppressLint("ScheduleExactAlarm")
    fun sheduleNotification(){
        val intent = Intent(applicationContext, AlarmNotification::class.java)
        val pendingIndent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //en que momento decirle que se lance
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis + 150,
            pendingIndent,
        )
    }




    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MY_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SUSCRIBETE"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    //LECTURA DE ARCHIVOS
    /**
     * Se encarga de leer el CSV que contiene la referencia de los souvenirs
     * y el nombre
     */
    @Composable
    private fun readCSV() :MutableList<Souvenir>{
        val souvenirList = mutableListOf<Souvenir>()
        var line :String?
        try{
            val input = resources.openRawResource(R.raw.souvenirs) //lee el archivo csv
            val br = BufferedReader(InputStreamReader(input,
                Charset.forName("UTF-8")
            ))

            while (true) {
                line = br.readLine()
                if (line.isNullOrEmpty()) break
                val palabra = line.split(",")
                val souvenir = Souvenir()
                souvenir.referencia = palabra[0]
                souvenir.nombre = palabra[1]
                souvenirList.add(souvenir)
            }
        }catch (_: IOException){
            Log.d("Error de lectura","Error")
        }
        return souvenirList
    }


    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun SouvenirsAddImages(souvenirsViewModel: SouvenirsViewModel) {
        val souvenirList = readCSV()
        val souvenirImg = souvenirsViewModel.souvenirs.collectAsState().value

        souvenirImg.mapIndexed { index, souvenir ->
            souvenir.apply {
                val additionalInfo = souvenirList.getOrNull(index)
                additionalInfo?.let {
                    souvenir.nombre = it.nombre
                    souvenir.referencia = it.referencia
                }
            }
        }
    }

}
