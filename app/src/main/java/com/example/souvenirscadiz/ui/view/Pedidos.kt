package com.example.souvenirscadiz.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun Pedidos(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    Scaffold(
        topBar = {
            HeaderAdmin(navController, souvenirsViewModel)
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
            //SouvenirsPedido(navController, souvenirsViewModel)
        }
    }
}

/**
 * Muestra los souvenirs guardados en fav
 * @param navController navegacion
 * @param souvenirsViewModel viewmodel de souvenirs
 */
@Composable
fun SouvenirsPedido(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsPedido()
    }

    val souvenirsPedidos by souvenirsViewModel.souvenirPedidos.collectAsState()//parametro que contiene los metodos guardados
    LazyRow{
        items(souvenirsPedidos.distinctBy { it.emailUser }){ souvenir ->
            Log.d("emailUser", souvenir.emailUser)
            Log.d("souvenir",souvenir.url.toString() + " " + souvenir.referencia)
            Text(
                text = souvenir.emailUser,
                fontFamily = KiwiMaru)
            Spacer(modifier = Modifier.padding(2.dp))
            CajaPedido(
                navController = navController,
                souvenir = souvenir)
        }
    }
}

