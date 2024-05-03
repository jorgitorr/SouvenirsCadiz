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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * Página prinicipal de los souvenirs
 * @param souvenirsViewModel viewModel de los souvenirs
 * @param navController navegacion
 */
@Composable
fun Principal(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    //devuelve los souvenirs guardados en la BDD
    LaunchedEffect(true){
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
            EnumaradoSouvenirs(souvenirsViewModel)//todos los enumerados
            SouvenirsList(navController, souvenirsViewModel)//lista de souvenirs
        }
    }
}



/**
 * Página que detalla información del souvenir
 * @param navController navegacion
 * @param souvenirsViewModel viewmodel de los souvenirs
 * @param referencia referencia del souvenir
 */
@Composable
fun SouvenirDetail(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, referencia:String) {
    Scaffold(
        topBar = { Header(navController, souvenirsViewModel) },
        bottomBar = { Footer(navController, souvenirsViewModel, loginViewModel) }
        , containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.Center
        ) {
            //imagen del souvenir
            val souvenir = souvenirsViewModel.getByReference(referencia)
            val url = "img${souvenir.url}"
            val resourceId = souvenirsViewModel.getResourceIdByName(url)
            Box(contentAlignment = Alignment.TopEnd){
                Image(painter = painterResource(id = resourceId),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigateUp() }
                )
                FavoriteButton(souvenir, souvenirsViewModel)
                ShopingCartButton(souvenir, souvenirsViewModel)
            }

            LazyColumn(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){

                item {
                    //nombre
                    Text(
                        text = souvenir.nombre,
                        fontFamily = KiwiMaru
                    )
                }

                item {
                    //referencia
                    Text(text = souvenir.referencia,
                        fontFamily = KiwiMaru)
                }

                item{
                    //precio
                    Text(text = "${souvenir.precio}€",
                        fontFamily = KiwiMaru)
                }

            }
        }
    }
}


/**
 * lista de souvenirs para la PÁGINA PRINCIPAL
 * Si la lista de souvenirs está vacía ya que no se a pulsado ninguno de los tipos de souvenirs
 * me coge la lista completa de souvenirs y me la muesra
 * si la lista (que contiene los souvenirs de un tipo) está vacía me la hace con todos los souvenirs
 * @param navController navegacion
 * @param souvenirsViewModel viewmodel de souvenirs
 */
@Composable
fun SouvenirsList(navController: NavController,souvenirsViewModel: SouvenirsViewModel){
    val souvenirs by souvenirsViewModel.souvenirsTipo.collectAsState()//souvenirs de un tipo
    val souvenirsPre by souvenirsViewModel.souvenirs.collectAsState()//todos los souvenirs

    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsFav()
        souvenirsViewModel.fetchSouvenirsCarrito()
    }
    //si no ha seleccionado ningun tipo de souvenirs me rellena los souvenirs con todos los tipos
    //si no lo rellena con los seleccionados
    if(souvenirs.isEmpty()){
        LazyColumn{
            items(souvenirsPre){ souvenirP->
                val url = "img${souvenirP.url}"
                val resourceId = souvenirsViewModel.getResourceIdByName(url)
                souvenirsViewModel.CheckSouvenirIsSaved(souvenirP)
                Caja(navController,souvenirP, resourceId, souvenirsViewModel)
            }
        }
    }else{
        LazyColumn{
            items(souvenirs){ souvenir->
                val url = "img${souvenir.url}"
                val resourceId = souvenirsViewModel.getResourceIdByName(url)
                souvenirsViewModel.CheckSouvenirIsSaved(souvenir)
                Caja(navController,souvenir, resourceId, souvenirsViewModel)
            }
        }
    }
}






