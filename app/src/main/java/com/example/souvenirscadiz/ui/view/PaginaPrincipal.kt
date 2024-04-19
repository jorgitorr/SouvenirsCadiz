package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * Página prinicipal de los souvenirs
 * @param souvenirsViewModel viewModel de los souvenirs
 * @param navController navegacion
 */
@Composable
fun Principal(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    souvenirsViewModel.fetchSouvenirsFav() //devuelve los souvenirs guardados en fav

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
            NombresSouvenirs(souvenirsViewModel)//todos los enumerados
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
    val context = LocalContext.current

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
            Image(painter = painterResource(id = resourceId),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigateUp() }
            )

            LazyRow(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly){

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

                item {
                    //icono de fav
                    Icon(
                        //si el souvenir esta guarado el icono se pone en rojo
                        imageVector = if (souvenir.guardado) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite Icon",
                        //si el souvenir esta guardado el color del icono cambia
                        tint = if (!souvenir.guardado) RaisanBlack else Redwood,
                        modifier = Modifier
                            .clickable {
                                souvenirsViewModel.saveSouvenirInFav {
                                    Toast
                                        .makeText(context, "Souvenir guardado", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                //permite saber si el souvenir esta guardado
                                souvenir.guardado = !souvenir.guardado
                            }
                    )
                }
            }

            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .padding(),
                horizontalArrangement = Arrangement.SpaceEvenly){
                item{
                    Row (modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround){
                        //precio
                        Text(text = souvenir.precio.toString()+"€", fontFamily = KiwiMaru)
                        //icono de carrito
                        Icon(imageVector = Icons.Default.ShoppingBasket, contentDescription = "Cesta de la compra",
                            modifier = Modifier
                                .clickable {
                                    souvenirsViewModel.saveSouvenirInCarrito {
                                        Toast
                                            .makeText(
                                                context,
                                                "Souvenir guardado en carrito",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                                .size(50.dp))
                    }
                }
            }
        }
    }
}
