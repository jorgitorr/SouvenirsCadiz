package com.example.souvenirscadiz.ui.view

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.data.util.CloudStorageManager
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * Principal
 *
 * @param souvenirsViewModel
 * @param navController
 * @param loginViewModel
 */
@Composable
fun Principal(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel, cloudStorageManager:CloudStorageManager){
    LaunchedEffect(Unit){
        souvenirsViewModel.fetchSouvenirsFav() //devuelve los souvenirs guardados en fav
        souvenirsViewModel.fetchSouvenirsCarrito() //devuelve los souvenirs guardados en carritos
    }

    val context = LocalContext.current

    // Manejo del botón de retroceso
    BackHandler {
        if (context is Activity) {
            context.finish()
        }
    }

    Scaffold(
        topBar = {
            if(loginViewModel.checkAdmin()){
                HeaderAdmin(navController, souvenirsViewModel) //tipo de header del administrador de la BDD
            }else{
               Header(navController, souvenirsViewModel)
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

            Row(modifier = Modifier.fillMaxWidth()){
                Buscador(souvenirsViewModel, navController)
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtrar",
                    modifier = Modifier
                        .clickable {
                            navController.navigate("Filtro")
                            souvenirsViewModel.setSelectedItem("")
                        }
                        .padding(top = 18.dp, start = 5.dp)
                        .size(30.dp),
                    tint = RaisanBlack
                )
            }
            SouvenirsList(navController, souvenirsViewModel, loginViewModel, cloudStorageManager)//lista de souvenirs
        }
    }
}












