package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * pagina del carrito
 */
@Composable
fun Carrito(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    val context = LocalContext.current
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
                Button(onClick = { souvenirsViewModel.saveSouvenirInCarrito {
                    Toast.makeText(context,"Souvenirs Pedidos", Toast.LENGTH_SHORT)
                        .show()
                } },
                    colors = ButtonDefaults.buttonColors(Redwood)) {
                    Text(text = "PEDIR", fontFamily = KiwiMaru)
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
        }
    }
}