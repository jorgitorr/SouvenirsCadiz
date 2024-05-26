package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * Principal
 *
 * @param souvenirsViewModel
 * @param navController
 * @param loginViewModel
 */
@Composable
fun Principal(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    var context = LocalContext.current
    LaunchedEffect(Unit){
        souvenirsViewModel.fetchSouvenirsFav() //devuelve los souvenirs guardados en fav
        souvenirsViewModel.fetchSouvenirsCarrito() //devuelve los souvenirs guardados en carritos
    }

    Scaffold(
        topBar = {
            if(loginViewModel.checkAdmin()){
                Toast.makeText(
                    context,
                    "Has iniciado sesión como Administrador",
                    Toast.LENGTH_SHORT
                ).show()

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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Search(souvenirsViewModel, navController)//buscador
            EnumaradoSouvenirs(souvenirsViewModel)//todos los enumerados
            SouvenirsList(navController, souvenirsViewModel, loginViewModel)//lista de souvenirs
        }
    }
}











