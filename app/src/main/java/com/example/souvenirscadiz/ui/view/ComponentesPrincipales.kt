package com.example.souvenirscadiz.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Cerulean
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KleeOne
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver
import com.example.souvenirscadiz.ui.theme.White


/**
 * Footer
 *
 * @param navController
 * @param souvenirsViewModel
 * @param loginViewModel
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
                //favoritos
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
                //perfil
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = if(selectedItem=="Perfil") Cerulean else RaisanBlack,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable {
                            souvenirsViewModel.setSelectedItem("Perfil")
                            if (loginViewModel.getCurrentUser() != null) {
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
 *
 * @param navController
 * @param souvenirsViewModel
 */
@Composable
fun Header(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val selectedItem by souvenirsViewModel.selectedItem.collectAsState()
    val souvenirsCarrito by souvenirsViewModel.souvenirCarrito.collectAsState()

    LaunchedEffect(Unit){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(Silver)){
        Row (modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            //logo personalizado
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "LOGO",
                modifier = Modifier.clickable { navController.navigate("Detalles") })
            //texto de souvenirs cadiz
            Text(text = "SOUVENIRS CADIZ",
                fontFamily = KneWave)



            //nube con el numero de objetos en el carrito

            BadgedBox(badge = {
                if(souvenirsCarrito.isNotEmpty()){
                    Badge {
                        Text(text = "${souvenirsCarrito.size}")
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shop",
                    tint = if(selectedItem=="Carrito") Cerulean else RaisanBlack,
                    modifier = Modifier.clickable {
                        navController.navigate("Tienda")
                        souvenirsViewModel.setSelectedItem("Carrito")
                    })
            }
        }
    }
}

/**
 * Search
 *
 * @param souvenirsViewModel
 * @param navController
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
        placeholder = {
            Text(text = "Search",
            color = if(isSystemInDarkTheme()) Silver else RaisanBlack)}, // DCS - Muestra un texto placeholder en la barra de búsqueda.
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
                    color = if(isSystemInDarkTheme()) White else RaisanBlack,
                    modifier = Modifier
                        .padding(bottom = 10.dp, start = 10.dp)
                        .clickable { navController.navigate("SouvenirDetail/${it.referencia}") }
                )
            }
        }
    }

}


/**
 * Enumarado souvenirs
 *
 * @param souvenirsViewModel
 */
@Composable
fun EnumaradoSouvenirs(souvenirsViewModel: SouvenirsViewModel){
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













