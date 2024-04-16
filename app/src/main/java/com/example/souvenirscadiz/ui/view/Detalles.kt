package com.example.souvenirscadiz.ui.view

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.data.util.Constant.Companion.NUMERO_TLF
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Cerulean
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker

/**
 * página de detalles al seleccionar el lOGO
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param navController navegacion entre páginas
 * @param loginViewModel viewmodel del login
 */
@Composable
fun DetallesLogo(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    Scaffold(
        topBar = {
            Header(navController, souvenirsViewModel)
        },
        bottomBar = {
            Footer(navController,souvenirsViewModel, loginViewModel)
        }, containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = Silver)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            var pulsar:Boolean = false
            Text(text = "Venta de souvenirs", fontFamily = KiwiMaru)
            Spacer(modifier = Modifier.height(2.dp))
            MyGoogleMaps()//google maps
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Jorge Arce Nogueroles" , fontFamily = KiwiMaru)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Para pedidos e información: ", fontFamily = KiwiMaru)
            Text(text = NUMERO_TLF, fontFamily = KiwiMaru, color = Cerulean, modifier = Modifier.clickable {})
            //MakePhoneCall(customerPhone = NUMERO_TLF, context = LocalContext.current)

        }
    }
}

/**
 * implementacion de google maps para la página de detalle del logo y de la empresa
 */
@Composable
fun MyGoogleMaps(){
    val marker = com.google.android.gms.maps.model.LatLng(36.50180834956613, -6.268638314742377)
    GoogleMap(modifier = Modifier
        .fillMaxWidth()
        .height(450.dp)){
        Marker(position = marker, title = "Ubicación", snippet = "Empresa")
    }
}

/**
 * Permite hacer una llamada por tlf
 * @param customerPhone telefono
 * @param context contexto
 */
@Composable
fun MakePhoneCall(customerPhone:String, context: Context) {
    try {
        val formattedPhone = "0$customerPhone"
        val intent = Intent(Intent.ACTION_CALL)
        val phoneUri = Uri.parse("tel:$formattedPhone")
        intent.data = phoneUri

        val permission = Manifest.permission.CALL_PHONE

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            context.startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), 0)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error making phone call", Toast.LENGTH_LONG).show()
    }
}
