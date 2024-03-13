package com.example.souvenirscadiz

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.navigation.NavManager
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.SouvenirsCadizTheme
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset


class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel = LoginViewModel()
    private val souvenirsViewModel:SouvenirsViewModel = SouvenirsViewModel()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SouvenirsCadizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    readCSV(souvenirsViewModel)
                    SouvenirsAddImages(souvenirsViewModel)
                    NavManager(souvenirsViewModel, loginViewModel)
                }
            }
        }
    }

    /**
     * Se encarga de leer el CSV que contiene la referencia de los souvenirs
     * y el nombre
     */
    @Composable
    private fun readCSV(souvenirsViewModel: SouvenirsViewModel) :MutableList<Souvenir>{
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
                souvenirsViewModel.SetTipo(souvenir,souvenir.nombre)
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
        val souvenirList = readCSV(souvenirsViewModel)
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
