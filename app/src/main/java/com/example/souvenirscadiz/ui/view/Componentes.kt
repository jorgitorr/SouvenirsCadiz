package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.SouvenirState
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Cerulean
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KleeOne
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * Cuadrado que contiene cada imagen del souvenir y su nombre, referencia y precio
 * @param navController navegacion
 * @param souvenir
 * @param url de la imagen que queremos mostrar
 */
@Composable
fun Cuadrado(navController: NavController, souvenir: Souvenir, url:Int, souvenirsViewModel: SouvenirsViewModel){
    var isFavorite by remember { mutableStateOf(false) }//variable fav
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Silver, shape = RoundedCornerShape(5.dp))
        .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = url),
                contentDescription = souvenir.nombre,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }

            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = souvenir.nombre,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Text(
                    text = souvenir.referencia,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    tint = if (!isFavorite)RaisanBlack else Redwood,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable {
                            isFavorite = !isFavorite
                            souvenirsViewModel.saveSouvenir (
                                { Toast.makeText(context, "Souvenir guardado", Toast.LENGTH_SHORT)
                                    .show()}, souvenir)
                        }
                )
                Text(
                    text = "${souvenir.precio}€",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 3.dp, end = 4.dp)
                )
            }
        }
    }

}

/**
 * Sobreescritura del componente cuadrado
 * para que me permita introducir un State
 * @param navController navegacion
 * @param souvenir contiene souvenirState que es el objeto de la base de datos
 * @param url contiene el numero de la url
 * @param souvenirsViewModel contiene el viewmodel de souvenir
 */
@Composable
fun Cuadrado(navController: NavController, souvenir: SouvenirState, url:Int, souvenirsViewModel: SouvenirsViewModel){
    var isFavorite by remember { mutableStateOf(false) }//variable fav
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Silver, shape = RoundedCornerShape(5.dp))
        .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(url)
                    .build(),
                contentDescription = souvenir.nombre,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }

            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = souvenir.nombre,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Text(
                    text = souvenir.referencia,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Text(
                    text = "${souvenir.precio}€",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 3.dp, end = 4.dp)
                )
            }
        }
    }

}





/**
 * lista de souvenirs
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

    if(souvenirs.isEmpty()){
        LazyColumn{
            items(souvenirsPre){ souvenirP->
                val url = "img${souvenirP.url}"
                val resourceId = souvenirsViewModel.getResourceIdByName(url)
                Cuadrado(navController,souvenirP, resourceId, souvenirsViewModel)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }else{
        LazyColumn{
            items(souvenirs){ souvenir->
                val url = "img${souvenir.url}"
                val resourceId = souvenirsViewModel.getResourceIdByName(url)
                Cuadrado(navController,souvenir, resourceId, souvenirsViewModel)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/**
 * Footer
 * @param navController navegacion entre páginas
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param loginViewModel viewmodel de login
 */
@Composable
fun Footer(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel){
    val selectedItem by souvenirsViewModel.selectedItem.collectAsState()
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
        .background(Silver)){
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home Icon",
                    tint = if(selectedItem=="Principal") Cerulean else RaisanBlack,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable {
                            souvenirsViewModel.setSelectedItem("Principal")
                            navController.navigate("Principal")
                        }
                )
                Text("Home")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite Icon",
                    tint = if(selectedItem=="Favoritos") Cerulean else RaisanBlack,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable {
                            souvenirsViewModel.setSelectedItem("Favoritos")
                            navController.navigate("Favoritos")
                        }
                )
                Text("Favorites")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = if(selectedItem=="Perfil") Cerulean else RaisanBlack,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable {
                            souvenirsViewModel.setSelectedItem("Perfil")
                            if (loginViewModel.getCurrentUser()!=null) {
                                navController.navigate("Perfil")
                            } else {
                                navController.navigate("InicioSesion")
                            }
                        }
                )
                Text("Profile")
            }
        }
    }
}

/**
 * Header
 * @param navController navegacion entre páginas
 */
@Composable
fun Header(navController: NavController){
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
                fontFamily = KneWave)

            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shop",
                modifier = Modifier.clickable { navController.navigate("Tienda") })
        }
    }
}

/**
 * Buscador
 * @param souvenirsViewModel viewModel de souvenirs
 * @param navController navegacion entre páginas
 * active -> si se ha pulsado en el buscador quedará activa
 * query -> la información que se recoge del buscador
 * souvenirs -> todos los souvenirs para que con ello me pueda generar la lista de los souvenirs
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(souvenirsViewModel: SouvenirsViewModel, navController: NavController){
    val active by souvenirsViewModel.active.collectAsState()
    val query by souvenirsViewModel.query.collectAsState()
    val souvenirs by souvenirsViewModel.souvenirs.collectAsState()

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        query = query,
        onQueryChange = { souvenirsViewModel.setQuery(it) }, // DCS - Actualiza el texto de búsqueda en el ViewModel.
        onSearch = { souvenirsViewModel.setActive(false) }, // DCS - Desactiva la búsqueda al presionar el botón de búsqueda.
        active = active,
        onActiveChange = { souvenirsViewModel.setActive(it) }, // DCS - Actualiza el estado de activación de la búsqueda.
        placeholder = { Text(text = "Search") }, // DCS - Muestra un texto placeholder en la barra de búsqueda.
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "BUSCADOR")
        },
        trailingIcon = {
            Icon(imageVector = Icons.Default.Close, contentDescription = "CERRAR",
                modifier = Modifier.clickable {
                    souvenirsViewModel.setActive(false)
                    souvenirsViewModel.setQuery("") }
            )
        }
    ) {
        if(query.isNotEmpty()){
            val filterName = souvenirs.filter { it.nombre.contains(query,ignoreCase = true) }

            filterName.forEach{
                Text(text = it.nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = KiwiMaru,
                    modifier = Modifier
                        .padding(bottom = 10.dp, start = 10.dp)
                        .clickable { navController.navigate("SouvenirDetail/${it.referencia}") }
                )
            }
        }
    }

}

/**
 * nombre de todos los tipos de souvenirs
 * @param souvenirsViewModel viewmodel de souvenirs
 */
@Composable
fun NombresSouvenirs(souvenirsViewModel: SouvenirsViewModel){
    souvenirsViewModel.setTipo()//poner el tipo de souvenir para cada souvenir
    LazyRow {
        items(Tipo.entries.toTypedArray()) { tipo ->
            Text(
                text = tipo.valor,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable { souvenirsViewModel.getByTipo(tipo) },
                color = RaisanBlack,
                fontSize = 16.sp,
                fontFamily = KleeOne
            )
        }
    }
}

