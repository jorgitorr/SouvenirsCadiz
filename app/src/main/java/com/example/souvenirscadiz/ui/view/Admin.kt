package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Cerulean
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver

@Composable
fun AdminPrincipal(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    Scaffold(
        topBar = {
            HeaderAdmin(navController)
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
            Search(souvenirsViewModel, navController)//buscador
            NombresSouvenirs(souvenirsViewModel)//todos los enumerados
            SouvenirsList(navController, souvenirsViewModel)//lista de souvenirs
        }
    }
}


/**
 * Header
 * @param navController navegacion entre páginas
 */
@Composable
fun HeaderAdmin(navController: NavController){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(Silver)){
        Row (modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "LOGO",
                modifier = Modifier.clickable { navController.navigate("Detalles") })
            Text(text = "SOUVENIRS CADIZ",
                fontFamily = KneWave
            )

            Icon(
                imageVector = Icons.Default.Sell,
                contentDescription = "Shop",
                modifier = Modifier.clickable { navController.navigate("Pedidos") })
        }
    }
}
