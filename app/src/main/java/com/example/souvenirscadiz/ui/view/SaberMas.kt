package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Silver
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * página de detalles al seleccionar el lOGO
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param navController navegacion entre páginas
 * @param loginViewModel viewmodel del login
 */
@Composable
fun DetallesLogo(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    //var telefonoSeleccionado by remember { mutableStateOf(false)}
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

            Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
                Text(text = "VENTA DE SOUVENIRS",
                    fontFamily = KiwiMaru,
                    textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(2.dp))
            MyGoogleMaps()//google maps
            Spacer(modifier = Modifier.height(2.dp))
            Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
                Text(text = "Jorge Arce Nogueroles Enterprise" ,
                    fontFamily = KiwiMaru,
                    textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(2.dp))
            /*
            QUITE LA FUNCIÓN DE LLAMAR PORQUE NO ES UNA FUNCIÓN ADECUADA PARA ESTA APP
            YA QUE PUEDEN LLAMAR MUCHA GENTE A LA VEZ O CUALQUIER COSA
            Row{
                Text(text = "Para pedidos e información: ", fontFamily = KiwiMaru)
                Text(text = NUMERO_TLF, fontFamily = KiwiMaru, color = Cerulean, modifier = Modifier.clickable {
                    telefonoSeleccionado = true //para realizar la llamada al numero de telefono
                })
            }*/

            /*if(telefonoSeleccionado){
                MakePhoneCall(NUMERO_TLF, LocalContext.current)
                telefonoSeleccionado = false //para que no vuelva a llamar al salir
            }*/

        }
    }
}



/**
 * implementacion de google maps para la página de detalle del logo y de la empresa
 * marker: marcador
 * cameraPositionState: posicion de la camara
 * UiSettings: para activar el zoom
 * Poperties: Propiedades, para activar el satelite
 */
@Composable
fun MyGoogleMaps(){
    val marker = LatLng(36.50180834956613, -6.268638314742377)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(marker, 17f)
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    GoogleMap(
        modifier = Modifier.fillMaxWidth().height(500.dp),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
    ){
        Marker(position = marker, title = "Ubicación", snippet = "Empresa")
    }
}


/*
/**
 * Permite hacer una llamada por tlf
 * @param customerPhone telefono
 * @param context contexto
 */
@Composable
fun MakePhoneCall(customerPhone:String, context: Context) {
    try {
        val intent = Intent(Intent.ACTION_CALL)
        val phoneUri = Uri.parse("tel:$customerPhone")
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
}*/
