package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Silver

@Composable
fun Tienda(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
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
            souvenirsViewModel.fetchSouvenirsCarrito()
            SouvenirsCarrito(navController, souvenirsViewModel)
            Button(onClick = { /*TODO*/ }) {
                Text(text = "PEDIR", fontFamily = KiwiMaru)
            }
        }
    }
}

/**
 * Muestra los souvenirs guardados en fav
 */
@Composable
fun SouvenirsCarrito(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val souvenirSaved by souvenirsViewModel.souvenirCarrito.collectAsState()//parametro que contiene los metodos guardados
    LazyRow{
        items(souvenirSaved){ souvenir ->
            Cuadrado(navController = navController,
                souvenir = souvenir,
                url = souvenir.url,
                souvenirsViewModel = souvenirsViewModel)
        }
    }
}