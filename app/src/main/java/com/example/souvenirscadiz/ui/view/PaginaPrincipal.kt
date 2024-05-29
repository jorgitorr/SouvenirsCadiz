package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
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
    LaunchedEffect(Unit){
        souvenirsViewModel.fetchSouvenirsFav() //devuelve los souvenirs guardados en fav
        souvenirsViewModel.fetchSouvenirsCarrito() //devuelve los souvenirs guardados en carritos
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()){
                Search(souvenirsViewModel, navController) /* buscador */
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "Filtrar",
                    modifier = Modifier
                        .clickable { navController.navigate("Filtro") }
                        .padding(top = 18.dp, start = 5.dp)
                        .size(30.dp)
                )
            }
            //ActiveEvent(souvenirsViewModel)
            SouvenirsList(navController, souvenirsViewModel, loginViewModel)//lista de souvenirs
        }
    }
}

/**
 * Filtro
 *
 * @param souvenirsViewModel
 */
@Composable
fun Filtro(
    souvenirsViewModel: SouvenirsViewModel,
    loginViewModel: LoginViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
            if (loginViewModel.checkAdmin()) {
                HeaderAdmin(navController, souvenirsViewModel)
            } else {
                Header(navController, souvenirsViewModel)
            }
        },
        bottomBar = {
            if (loginViewModel.checkAdmin()) {
                FooterAdmin(navController, souvenirsViewModel, loginViewModel)
            } else {
                Footer(navController, souvenirsViewModel, loginViewModel)
            }
        },
        containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val tipoElegido: Tipo = Tipo.LLAVERO
            var sliderPosition by remember { mutableFloatStateOf(0f) }

            LazyColumn(
                modifier = Modifier
                    .background(Silver)
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                item {
                    Text(
                        text = "TIPO DE SOUVENIR",
                        fontFamily = KiwiMaru,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                item {
                    MenuTiposSouvenir(souvenirsViewModel)
                }
                item {
                    Text(
                        text = "PRECIO",
                        fontFamily = KiwiMaru,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                item {
                    Column {
                        Slider(
                            value = sliderPosition,
                            onValueChange = { sliderPosition = it },
                            valueRange = 0f..5.99f
                        )
                        Text(text = "$sliderPosition €")
                    }
                }
                item {
                    Button(
                        onClick = {
                            souvenirsViewModel.getByTipo(tipoElegido)
                            souvenirsViewModel.getByPrecio(sliderPosition)
                            navController.navigate("Principal")
                        },
                        modifier = Modifier.padding(end = 20.dp, bottom = 20.dp)
                            .wrapContentWidth(Alignment.End)
                    ) {
                        Text(text = "Filtrar")
                    }
                }
            }
        }
    }
}











