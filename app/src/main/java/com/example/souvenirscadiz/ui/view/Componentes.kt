package com.example.souvenirscadiz.ui.view

import android.content.res.Resources.Theme
import android.graphics.Color
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Cerulean
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KleeOne
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * Cuadrado que contiene cada imagen
 */
@Composable
fun Cuadrado(souvenir: Souvenir, url:Int){
    var isFavorite by remember { mutableStateOf(false) }

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
                        .clickable { isFavorite = !isFavorite }
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


@Composable
fun SouvenirsList(souvenirsViewModel: SouvenirsViewModel){
    val souvenirs by souvenirsViewModel.souvenirs.collectAsState()

    LazyColumn{
        items(souvenirs){ souvenir->
            val url = "img${souvenir.url}"
            val resourceId = souvenirsViewModel.getResourceIdByName(url)
            Cuadrado(souvenir, resourceId)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
fun Footer(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
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
                            navController.navigate("Perfil")
                        }
                )
                Text("Profile")
            }
        }
    }
}


@Composable
fun Header(navController: NavController){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(Silver)){
        Row (modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
            Text(text = "SOUVENIRS CADIZ",
                fontFamily = KneWave)

            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shop",
                modifier = Modifier.clickable { navController.navigate("Tienda") })
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(souvenirsViewModel: SouvenirsViewModel){
    val active by souvenirsViewModel.active.collectAsState()
    val query by souvenirsViewModel.query.collectAsState()

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
            Icon(imageVector = Icons.Default.Search, contentDescription = "")
        },
        trailingIcon = {
            // DCS - Icono para cerrar la vista de búsqueda o limpiar el texto de búsqueda.
            Icon(imageVector = Icons.Default.Close, contentDescription = "",
                modifier = Modifier.clickable { /*navController.popBackStack()*/ }
            )
        }
    ) {
        /*if(query.isNotEmpty()){ //si la busqueda no está vacía
            val filterName = superHero.filter { it.name.contains(query,ignoreCase = true) }

            filterName.forEach{
                Text(text = it.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 10.dp, start = 10.dp)
                        .clickable { navController.navigate("HeroDetail/${it.id}") }
                )
            }
        }*/
    }

}

@Composable
fun NombresSouvenirs(){
    LazyRow {
        items(Tipo.entries.toTypedArray()) { tipo ->
            Text(
                text = tipo.valor,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = RaisanBlack,
                fontSize = 16.sp,
                fontFamily = KleeOne
            )
        }
    }
}

