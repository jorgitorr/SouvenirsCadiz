package com.example.souvenirscadiz

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.Tipo
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
                    //NavManager(souvenirsViewModel, loginViewModel)
                    readCSV()
                }
            }
        }
    }

    /**
     * Se encarga de leer el CSV que contiene la referencia de los souvenirs
     * y el nombre
     */
    private fun readCSV() {
        val souvenirList = mutableListOf<Souvenir>()
        var line :String?
        try{
            val input = resources.openRawResource(R.raw.souvenirs) //lee el archivo csv
            var br = BufferedReader(InputStreamReader(input,
                Charset.forName("UTF-8")
            ))

            while (true) {
                line = br.readLine()
                if (line.isNullOrEmpty()) break
                var palabra = line.split(",")
                var souvenir = Souvenir()
                souvenir.referencia = palabra[0]
                souvenir.nombre = palabra[1]
                //setTipo(souvenir,souvenir.nombre)

                souvenirList.add(souvenir)
            }
            Log.d("size",souvenirList.size.toString())
        }catch (_: IOException){
            Log.d("Error de lectura","Error")
        }
    }


    fun setTipo(souvenir: Souvenir, palabra:String){
        when {
            palabra.contains("Llavero") -> souvenir.tipo = Tipo.LLAVERO
            palabra.contains("Iman") -> souvenir.tipo = Tipo.IMAN
            palabra.contains("Abridor") -> souvenir.tipo = Tipo.ABRIDOR
            palabra.contains("Pins") -> souvenir.tipo = Tipo.PINS
            palabra.contains("Cortauñas") -> souvenir.tipo = Tipo.CORTAUNIAS
            palabra.contains("Cucharilla") -> souvenir.tipo = Tipo.CUCHARILLA
            palabra.contains("Campana") -> souvenir.tipo = Tipo.CAMPANA
            palabra.contains("Salvamanteles") -> souvenir.tipo = Tipo.SALVAMANTELES
            palabra.contains("Posa") -> souvenir.tipo = Tipo.POSA
            palabra.contains("Set") -> souvenir.tipo = Tipo.SET
            palabra.contains("Parche") -> souvenir.tipo = Tipo.PARCHE
            palabra.contains("Adhes.") -> souvenir.tipo = Tipo.ADHESIVO
            palabra.contains("Pastillero") -> souvenir.tipo = Tipo.PASTILLERO
            palabra.contains("Espejo") -> souvenir.tipo = Tipo.ESPEJO
            palabra.contains("Cubremascarilla") -> souvenir.tipo = Tipo.CUBRE_MASCARILLA
            palabra.contains("Dedal") -> souvenir.tipo = Tipo.DEDAL
            palabra.contains("Pisapapeles") -> souvenir.tipo = Tipo.PISAPAPELES
            palabra.contains("Abanico") -> souvenir.tipo = Tipo.ABANICO
            palabra.contains("Estuche") -> souvenir.tipo = Tipo.ESTUCHE
            palabra.contains("Bola") -> souvenir.tipo = Tipo.BOLA
            else -> {
                souvenir.tipo = Tipo.LLAVERO
            }
        }
    }

}
