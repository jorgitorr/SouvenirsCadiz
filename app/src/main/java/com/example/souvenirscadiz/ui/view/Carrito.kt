package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * pagina del carrito
 */
@Composable
fun Carrito(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

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
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SouvenirsCarrito(navController, souvenirsViewModel, loginViewModel) //souvenirs en el carrito
        }
    }
}

/**
 * Muestra el botón de pedir en el caso de que se pueda mostrar o el mensaje de alerta
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param loginViewModel viewmodel del login
 * @param navController navegacion
 */
@Composable
fun ButtonPedirOrMsg(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, navController: NavController){
    val souvenirCarrito by souvenirsViewModel.souvenirCarrito.collectAsState()
    val showDialog = remember { mutableStateOf(true) } //muestra el dialogo
    val context = LocalContext.current
    val compositionEmptyBasket by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_basket))

    //si no hay souvenirs en el carrito
    if(souvenirCarrito.isNotEmpty()){
        Button(onClick = {
            //solo queda introducir la cantidad de cada uno
            souvenirsViewModel.saveSouvenirInPedido {
                Toast.makeText(context,
                    "Souvenirs Pedidos",
                    Toast.LENGTH_SHORT).show()
            }
            //vaciar souvenirs del carrito
            souvenirsViewModel.vaciarSouvenirsCarrito() },

            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Redwood)) {
            Text(text = "PEDIR",
                fontFamily = KiwiMaru
            )
        }
    }else{
        //si el tamaño de souvenirs fav es diferente diferente
        if (loginViewModel.showAlert) {
            // Mostrar un diálogo si el usuario no ha iniciado sesión
            AlertDialog(
                onDismissRequest = { /* No hacer nada en el cierre del diálogo */ },
                title = {
                    Text(
                        text = "No has iniciado sesión para ver tus elementos en el carrito",
                        fontFamily = KiwiMaru,
                        color = RaisanBlack,
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            navController.navigate("InicioSesion")
                        }
                    ) {
                        Text(
                            text = "Ir a Inicio de Sesión",
                            fontFamily = KiwiMaru,
                            color = Redwood
                        )
                    }
                }
            )
        } else{
            LottieAnimation(composition = compositionEmptyBasket)
            //muestra el AlertDialog
            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog.value = false
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
                                showDialog.value = false
                            }
                        ) {
                            Text("Aceptar",
                                fontFamily = KiwiMaru,
                                color = if(isSystemInDarkTheme()) RaisanBlack else Silver)
                        }
                    }
                )
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
fun SouvenirsCarrito(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel){
    val souvenirSaved by souvenirsViewModel.souvenirCarrito.collectAsState()//parametro que contiene los metodos guardados

    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

    LazyColumn{
        items(souvenirSaved){
            souvenir ->
            Spacer(modifier = Modifier.height(20.dp))

            val url = "img${souvenir.url}"
            val resourceId = souvenirsViewModel.getResourceIdByName(url)
            CajaCarrito(navController = navController,
                souvenir = souvenir,
                url = resourceId,
                souvenirsViewModel = souvenirsViewModel)
        }
        item {
            //boton de pedir los souvenirs
            Spacer(modifier = Modifier.height(50.dp))
            ButtonPedirOrMsg(souvenirsViewModel, loginViewModel, navController)
        }
    }
}

