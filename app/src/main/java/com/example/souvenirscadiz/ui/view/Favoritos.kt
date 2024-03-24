package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Silver

@Composable
fun Favoritos(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    souvenirsViewModel.fetchSouvenirs()
    Scaffold(
        topBar = {
            Header(navController)
        },
        bottomBar = {
            Footer(navController, souvenirsViewModel, loginViewModel)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SouvenirsSaved(navController = navController, souvenirsViewModel = souvenirsViewModel)
        }
    }
}

/**
 * Contiene todos los souvenirs guardados por el usuario
 * @param navController navegacion
 * @param souvenirsViewModel viewModel de los souvenirs
 */
@Composable
fun SouvenirsSaved(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val souvenirSaved by souvenirsViewModel.souvenirSaved.collectAsState()
    LazyRow{
        items(souvenirSaved){ souvenir ->
            Cuadrado(navController = navController,
                souvenir = souvenir,
                url = souvenir.url,
                souvenirsViewModel = souvenirsViewModel)
        }
    }
}