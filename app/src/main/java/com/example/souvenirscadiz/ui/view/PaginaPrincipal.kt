package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
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
                .padding(innerPadding).background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Search(souvenirsViewModel)
            NombresSouvenirs(souvenirsViewModel)
            SouvenirsList(souvenirsViewModel)
        }
    }
}
