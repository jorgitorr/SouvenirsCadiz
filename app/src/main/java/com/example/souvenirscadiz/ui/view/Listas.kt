package com.example.souvenirscadiz.ui.view

import android.media.MediaPlayer
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
 * lista de souvenirs para la PÁGINA PRINCIPAL
 * Si la lista de souvenirs está vacía ya que no se a pulsado ninguno de los tipos de souvenirs
 * me coge la lista completa de souvenirs y me la muesra
 * si la lista (que contiene los souvenirs de un tipo) está vacía me la hace con todos los souvenirs
 * @param navController navegacion
 * @param souvenirsViewModel viewmodel de souvenirs
 */
@Composable
fun SouvenirsList(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel) {
    val souvenirs by souvenirsViewModel.souvenirsTipo.collectAsState() // souvenirs de un tipo
    val souvenirsPre by souvenirsViewModel.souvenirs.collectAsState() // todos los souvenirs
    //val visibleItemCount by souvenirsViewModel.visibleItemCount.collectAsState()

    // si no ha seleccionado ningún tipo de souvenirs, muestra todos los souvenirs
    if (souvenirs.isEmpty()) {
        LazyColumn {
            itemsIndexed(souvenirsPre) { index, souvenirP ->
                souvenirsViewModel.checkSouvenirIsSaved(souvenirP)
                Caja(navController, souvenirP, souvenirsViewModel, loginViewModel)
            }
        }
    } else { // si hay un tipo seleccionado, muestra los souvenirs de ese tipo
        LazyColumn {
            itemsIndexed(souvenirs) { index, souvenir->
                souvenirsViewModel.checkSouvenirIsSaved(souvenir)
                Caja(navController, souvenir, souvenirsViewModel, loginViewModel)
            }
        }
    }
}


/**
 * Contiene todos los souvenirs guardados por el usuario
 * @param navController navegacion
 * @param souvenirsViewModel viewModel de los souvenirs
 */
@Composable
fun SouvenirsListFav(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel){
    val context = LocalContext.current
    val souvenirSaved by souvenirsViewModel.souvenirFav.collectAsState()
    val soundEffect = MediaPlayer.create(context, R.raw.angry_start_sound)
    val compositionAngryStart by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.angry_start))

    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsFav()
    }

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
 * Muestra los souvenirs guardados en fav
 * @param navController navegacion
 * @param souvenirsViewModel viewmodel de souvenirs
 */
@Composable
fun SouvenirsListCarrito(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel){
    val souvenirSaved by souvenirsViewModel.souvenirCarrito.collectAsState()//parametro que contiene los metodos guardados
    val compositionEmptyBasket by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_basket))
    val context = LocalContext.current
    val soundEffect = MediaPlayer.create(context, R.raw.empty_basket_sound)

    LaunchedEffect(souvenirSaved.size){
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
                //boton de pedir todos los souvenirs
                Spacer(modifier = Modifier.height(50.dp))
                ButtonPedirOrMsg(souvenirsViewModel, loginViewModel, navController)
            }
        }
    }
}


/**
 * Muestra los souvenirs guardados en pedido
 * @param navController navegacion
 * @param souvenirsViewModel viewmodel de souvenirs
 */
@Composable
fun SouvenirsListPedidos(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val souvenirsPedidos by souvenirsViewModel.souvenirPedidos.collectAsState()

    if(souvenirsPedidos.isEmpty()){
        Text(text = "No hay ningún pedido",
            fontFamily = KiwiMaru)
    }else{
        LazyColumn{
            items(souvenirsPedidos){ souvenir ->
                CajaPedido(
                    navController,
                    souvenir,
                    souvenirsViewModel)
            }
        }
    }
}


@Composable
fun UsuariosList(navController: NavController, loginViewModel: LoginViewModel, souvenirsViewModel: SouvenirsViewModel){
    val users by loginViewModel.users.collectAsState()

    if(users.isEmpty()){
        Text(text = "No hay usuarios registrados",
            fontFamily = KiwiMaru)
    }else{
        LazyColumn{
            items(users){ user ->
                CajaUsuarios(
                    user,
                    loginViewModel,
                    souvenirsViewModel,
                    navController
                )
            }
        }
    }
}