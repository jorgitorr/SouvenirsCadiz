package com.example.souvenirscadiz.ui.view

import android.media.MediaPlayer
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * Souvenirs list
 *
 * @param navController
 * @param souvenirsViewModel
 * @param loginViewModel
 */
@Composable
fun SouvenirsList(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel) {
    val souvenirs by souvenirsViewModel.souvenirs.collectAsState() // todos los souvenirs
    val souvenirsFiltrados by souvenirsViewModel.souvenirsFiltrados.collectAsState()

    if(souvenirsFiltrados.isEmpty()){
        LazyColumn {
            items(souvenirs) { souvenir ->
                souvenirsViewModel.checkSouvenirIsSaved(souvenir)
                Caja(navController, souvenir, souvenirsViewModel, loginViewModel)
            }
        }
    }else{
        LazyColumn{
            items(souvenirsFiltrados) { souvenir ->
                souvenirsViewModel.checkSouvenirIsSaved(souvenir)
                Caja(navController, souvenir, souvenirsViewModel, loginViewModel)
            }
        }
    }
}


/**
 * Souvenirs list fav
 *
 * @param navController
 * @param souvenirsViewModel
 * @param loginViewModel
 */
@Composable
fun SouvenirsListFav(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel){
    val context = LocalContext.current
    val souvenirSaved by souvenirsViewModel.souvenirFav.collectAsState()
    val soundEffect = MediaPlayer.create(context, R.raw.angry_start_sound)
    val compositionAngryStart by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.angry_start))

    if(souvenirSaved.isEmpty()){ //si no hay souvenirs guardados
        LottieAnimation(composition = compositionAngryStart)

        if(souvenirsViewModel.soundPlayedFav){
            soundEffect.start()
            souvenirsViewModel.soundPlayedFav = false
        }

        //muestra el AlertDialog
        if (souvenirsViewModel.showDialogFav) {
            AlertDialog(
                onDismissRequest = {
                    souvenirsViewModel.showDialogFav = false
                },
                title = {
                    Text(text = "Alerta",
                        fontFamily = KiwiMaru,
                        color = if(isSystemInDarkTheme()) Silver else RaisanBlack
                    )
                },
                text = {
                    Text(
                        text = "NO TIENES ELEMENTOS GUARDADOS EN FAVORITOS",
                        fontFamily = KiwiMaru,
                        color = if(isSystemInDarkTheme()) Silver else RaisanBlack
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            souvenirsViewModel.showDialogFav = false
                        }
                    ) {
                        Text("Aceptar",
                            fontFamily = KiwiMaru,
                            color = if(isSystemInDarkTheme()) Silver else RaisanBlack
                        )
                    }
                }
            )
        }
    }else{
        LazyColumn{
            items(souvenirSaved){ souvenir ->
                Caja(navController,
                    souvenir,
                    souvenirsViewModel,
                    loginViewModel)
            }
        }
    }


}


/**
 * Souvenirs list carrito
 *
 * @param navController
 * @param souvenirsViewModel
 * @param loginViewModel
 */
@Composable
fun SouvenirsListCarrito(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel){
    val souvenirSaved by souvenirsViewModel.souvenirCarrito.collectAsState()//parametro que contiene los metodos guardados
    val compositionEmptyBasket by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_basket))
    val context = LocalContext.current
    val soundEffect = MediaPlayer.create(context, R.raw.empty_basket_sound)


    LaunchedEffect(Unit){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

    if(souvenirSaved.isEmpty()){
        if(souvenirsViewModel.soundPlayedCarrito){
            soundEffect.start()
            souvenirsViewModel.soundPlayedCarrito = false
        }

        LottieAnimation(composition = compositionEmptyBasket)

        //muestra el AlertDialog
        if (souvenirsViewModel.showDialogCarrito) {
            AlertDialog(
                onDismissRequest = {
                    souvenirsViewModel.showDialogCarrito = false
                },
                title = {
                    Text(text = "Alerta",
                        fontFamily = KiwiMaru,
                        color = if(isSystemInDarkTheme()) Silver else RaisanBlack)
                },
                text = {
                    Text(
                        text = "NO TIENES ELEMENTOS GUARDADOS EN EL CARRITO",
                        fontFamily = KiwiMaru,
                        color = if(isSystemInDarkTheme()) Silver else RaisanBlack)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            souvenirsViewModel.showDialogCarrito = false
                        }
                    ) {
                        Text("Aceptar",
                            fontFamily = KiwiMaru,
                            color = if(isSystemInDarkTheme()) Silver else RaisanBlack)
                    }
                }
            )
        }
    }else{
        LazyColumn{
            items(souvenirSaved){ souvenir ->
                CajaCarrito(
                    navController,
                    souvenir,
                    souvenirsViewModel)
            }
            item {
                Spacer(modifier = Modifier.height(50.dp))
                ButtonPedirOrMsg(souvenirsViewModel, loginViewModel, navController)
            }
        }
    }
}


/**
 * Souvenirs list pedidos
 *
 * @param navController
 * @param souvenirsViewModel
 */
@Composable
fun SouvenirsListPedidos(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val souvenirsPedidos by souvenirsViewModel.souvenirPedidos.collectAsState()

    if(souvenirsPedidos.isEmpty()){
        Text(text = "No hay ningún pedido",
            fontFamily = KiwiMaru)
    }else{
        LazyColumn{
            items(souvenirsPedidos){ pedido ->
                CajaPedido(
                    souvenirsViewModel,
                    navController,
                    pedido)
            }
        }
    }
}



@Composable
fun SouvenirsListHistorial(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val souvenirsHistorial by souvenirsViewModel.pedidoHistorial.collectAsState()

    if(souvenirsHistorial.isEmpty()){
        Text(text = "No hay ningún pedido",
            fontFamily = KiwiMaru)
    }else{
        LazyColumn{
            items(souvenirsHistorial){ historial ->
                CajaHistorial(
                    navController,
                    historial)
            }
        }
    }
}


/**
 * Usuarios list
 *
 * @param loginViewModel
 */
@Composable
fun UsuariosList(loginViewModel: LoginViewModel){
    val users by loginViewModel.users.collectAsState()

    if(users.isEmpty()){
        Text(text = "No hay usuarios registrados",
            fontFamily = KiwiMaru)
    }else{
        LazyColumn{
            items(users){ user ->
                CajaUsuarios(user, loginViewModel)
            }
        }
    }
}