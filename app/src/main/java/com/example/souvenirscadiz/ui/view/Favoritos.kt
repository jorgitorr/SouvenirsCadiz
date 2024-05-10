package com.example.souvenirscadiz.ui.view

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.souvenirscadiz.ui.theme.Silver

@Composable
fun Favoritos(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsFav()
    }

    Scaffold(
        topBar = {
            if(loginViewModel.checkAdmin()){
                HeaderAdmin(navController, souvenirsViewModel)
            }else{
                Header(navController,souvenirsViewModel)
            }
        },
        bottomBar = {
            Footer(navController, souvenirsViewModel, loginViewModel)
        }, containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SouvenirSavedFav(navController, souvenirsViewModel)
        }
    }
}

/**
 * Contiene todos los souvenirs guardados por el usuario
 * @param navController navegacion
 * @param souvenirsViewModel viewModel de los souvenirs
 */
@Composable
fun SouvenirSavedFav(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val context = LocalContext.current
    val soundEffect = MediaPlayer.create(context, R.raw.angry_start_sound)
    val soundPlayed = remember { mutableStateOf(false) } // Variable para rastrear si el sonido ya se ha reproducido

    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsFav()
    }

    val showDialog = remember { mutableStateOf(true) } //muestra el dialogo
    val souvenirSaved by souvenirsViewModel.souvenirFav.collectAsState()//parametro que contiene los metodos guardados
    val compositionAngryStart by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.angry_start)) //animacion de la estrella

    if(souvenirSaved.isEmpty()){ //si no hay souvenirs guardados
        LottieAnimation(composition = compositionAngryStart)

        if(!soundPlayed.value){
            soundEffect.start()
            soundPlayed.value = true
        }

        //muestra el AlertDialog
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialog.value = false
                },
                title = {
                    Text(text = "Alerta",
                        fontFamily = KiwiMaru,
                        color = MaterialTheme.colorScheme.tertiaryContainer)
                },
                text = {
                    Text(
                        text = "NO TIENES ELEMENTOS GUARDADOS EN FAVORITOS",
                        fontFamily = KiwiMaru,
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog.value = false
                        }
                    ) {
                        Text("Aceptar",
                            fontFamily = KiwiMaru)
                    }
                }
            )
        }
    }else{
        LazyColumn{
            items(souvenirSaved){ souvenir ->
                val url = "img${souvenir.url}"
                val resourceId = souvenirsViewModel.getResourceIdByName(url)

                Caja(navController = navController,
                    url = resourceId,
                    souvenir = souvenir,
                    souvenirsViewModel = souvenirsViewModel)
            }
        }
    }


}