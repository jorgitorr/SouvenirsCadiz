package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Silver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker

/**
 * página de detalles al seleccionar el lOGO
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
        }, containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = Silver)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
 * implementacion de google maps para la página de detalle del logo y de la empresa
 */
@Composable
fun MyGoogleMaps(){
    val marker = com.google.android.gms.maps.model.LatLng(36.50180834956613, -6.268638314742377)
    GoogleMap(modifier = Modifier.fillMaxWidth().height(450.dp)){
        Marker(position = marker, title = "Ubicación", snippet = "Empresa")
    }
}
