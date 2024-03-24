package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Silver
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker

/**
 * página de detalles al seleccionar el logo
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param navController navegacion entre páginas
 * @param loginViewModel viewmodel del login
 */
@Composable
fun Detalles(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    Scaffold(
        topBar = {
            Header(navController)
        },
        bottomBar = {
            Footer(navController,souvenirsViewModel, loginViewModel)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Venta de souvenirs", fontFamily = KiwiMaru)
            Spacer(modifier = Modifier.height(2.dp))
            MyGoogleMaps()//google maps
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Jorge Arce Nogueroles" , fontFamily = KiwiMaru)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Para pedidos e información: 617759036", fontFamily = KiwiMaru)
        }
    }
}

/**
 * implementacion de google maps
 */
@Composable
fun MyGoogleMaps(){
    val marker = com.google.android.gms.maps.model.LatLng(36.50180834956613, -6.268638314742377)
    GoogleMap(modifier = Modifier.fillMaxWidth().height(450.dp)){
        Marker(position = marker, title = "Ubicación", snippet = "Empresa")
    }
}