package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver
import com.example.souvenirscadiz.ui.theme.White
import org.apache.commons.lang3.ObjectUtils


/**
 * pagina del carrito
 */
@Composable
fun Carrito(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    val context = LocalContext.current
    val showDialog by remember { mutableStateOf(false) } //esto es para que salte un dialogo antes de confirmar el pedido
    var onClickBasket by remember { mutableStateOf(true) }//al pulsar el boton del carrito
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
            souvenirsViewModel.fetchSouvenirsCarrito()
            SouvenirsCarrito(navController, souvenirsViewModel)
            //si no hay ningun souvenir en la lista de souvenirs del carrito
            //el boton no aparece en la pantalla
            if(souvenirsViewModel.getNumberSouvenirsInCarrito()!=0){
                Button(onClick = {
                    //solo queda introducir la cantidad de cada uno
                    souvenirsViewModel.saveSouvenirInPedido {
                        Toast.makeText(context,"Souvenirs Pedidos", Toast.LENGTH_SHORT)
                            .show()
                } },
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
                                color = White,
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
                } else {
                    Text(
                        text = "Todavía no tienes souvenirs en el carrito",
                        fontFamily = KiwiMaru
                    )
                }
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
fun SouvenirsCarrito(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val souvenirSaved by souvenirsViewModel.souvenirCarrito.collectAsState()//parametro que contiene los metodos guardados
    LazyColumn{
        items(souvenirSaved){ souvenir ->
            Cuadrado(navController = navController,
                souvenir = souvenir,
                url = souvenir.url,
                souvenirsViewModel = souvenirsViewModel)
            cantidadSouvenir()
        }
    }
}


/**
 * Cantidad de souvenirs que pide el cliente
 * Comprueba si el valor introducido es un número antes de enviarlo
 */
@Composable
fun cantidadSouvenir():Int{
    val context = LocalContext.current
    var cantidadSouvenir by remember { mutableStateOf("0") }
    OutlinedTextField(value = cantidadSouvenir,
        onValueChange = {
            if (it.isDigitsOnly()) {
                cantidadSouvenir = it
            }else{
                Toast.makeText(context,"Has introducido un campo erroneo",Toast.LENGTH_SHORT).show()
            }},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

    return cantidadSouvenir.toInt()
}