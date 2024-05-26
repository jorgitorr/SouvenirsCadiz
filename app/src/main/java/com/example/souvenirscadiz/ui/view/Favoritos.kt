package com.example.souvenirscadiz.ui.view


import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Silver

/**
 * Favoritos
 *
 * @param souvenirsViewModel
 * @param navController
 * @param loginViewModel
 */
@Composable
fun Favoritos(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    Scaffold(
        topBar = {
            if(loginViewModel.checkAdmin()){
                HeaderAdmin(navController, souvenirsViewModel)
            }else{
                Header(navController,souvenirsViewModel)
            }
        },
        bottomBar = {
            if(loginViewModel.checkAdmin()){
                FooterAdmin(navController, souvenirsViewModel, loginViewModel)
            }else{
                Footer(navController, souvenirsViewModel, loginViewModel)
            }
        }, containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SouvenirsListFav(navController, souvenirsViewModel, loginViewModel)
        }
    }
}

