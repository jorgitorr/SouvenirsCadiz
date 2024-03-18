package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver

@Composable
fun Principal(souvenirsViewModel: SouvenirsViewModel, navController: NavController){
    Scaffold(
        topBar = {
            Header(navController)
        },
        bottomBar = {
            Footer(navController,souvenirsViewModel)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Search(souvenirsViewModel, navController)//buscador
            NombresSouvenirs(souvenirsViewModel)//todos los enumerados
            SouvenirsList(navController, souvenirsViewModel)//lista de souvenirs
        }
    }
}

@Composable
fun SouvenirDetail(navController: NavController, souvenirsViewModel: SouvenirsViewModel, nombre:String) {
    var isFavorite by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { Header(navController) },
        bottomBar = { Footer(navController, souvenirsViewModel) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.Bottom
        ) {
            val souvenir = souvenirsViewModel.getByReference(nombre)
            val url = "img${souvenir.url}"
            val resourceId = souvenirsViewModel.getResourceIdByName(url)
            Image(painter = painterResource(id = resourceId),
                contentDescription = "")
            Text(text = souvenir.nombre, fontFamily = KiwiMaru)
            Text(text = souvenir.referencia, fontFamily = KiwiMaru)
            Text(text = souvenir.precio.toString()+"€", fontFamily = KiwiMaru)
            
            Text(text = "COMPRAR", fontFamily = KiwiMaru,
                modifier = Modifier.clickable { })

            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite Icon",
                tint = if (!isFavorite) RaisanBlack else Redwood,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .clickable { isFavorite = !isFavorite }
            )
            
        }
    }
}
