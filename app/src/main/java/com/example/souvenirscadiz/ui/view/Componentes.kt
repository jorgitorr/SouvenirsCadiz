package com.example.souvenirscadiz.ui.view


import android.media.MediaPlayer
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver
import com.example.souvenirscadiz.ui.theme.White


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
 * @param navController navegacion entre páginas
 */
@Composable
fun Header(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val selectedItem by souvenirsViewModel.selectedItem.collectAsState()
    val souvenirsCarrito by souvenirsViewModel.souvenirCarrito.collectAsState()

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
 * Busca por usuarios
 * @param loginViewModel viewmodel del login
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUsuarios(loginViewModel: LoginViewModel, navController: NavController){
    val active by loginViewModel.active.collectAsState()
    val query by loginViewModel.query.collectAsState()
    val souvenirs by loginViewModel.users.collectAsState()

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        query = query,
        onQueryChange = { loginViewModel.setQuery(it) }, // DCS - Actualiza el texto de búsqueda en el ViewModel.
        onSearch = { loginViewModel.setActive(false) }, // DCS - Desactiva la búsqueda al presionar el botón de búsqueda.
        active = active,
        onActiveChange = { loginViewModel.setActive(it) }, // DCS - Actualiza el estado de activación de la búsqueda.
        placeholder = {
            Text(text = "Search",
                color = if(isSystemInDarkTheme()) Silver else RaisanBlack)}, // DCS - Muestra un texto placeholder en la barra de búsqueda.
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "BUSCADOR")
        },
        trailingIcon = {
            Icon(imageVector = Icons.Default.Close, contentDescription = "CERRAR",
                modifier = Modifier.clickable {
                    loginViewModel.setActive(false)
                    loginViewModel.setQuery("") }
            )
        }
    ) {
        if(query.isNotEmpty()){
            val filterName = souvenirs.filter { it.email.contains(query,ignoreCase = true) }
            val filterName2 = souvenirs.filter { it.username.contains(query,ignoreCase = true) }

            filterName.forEach{
                Text(text = it.email,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = KiwiMaru,
                    color = if(isSystemInDarkTheme()) White else RaisanBlack,
                    modifier = Modifier
                        .padding(bottom = 10.dp, start = 10.dp)
                        .clickable { navController.navigate("UsuarioDetial/${it.email}") }
                )
            }

            filterName2.forEach{
                Text(text = it.email,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = KiwiMaru,
                    color = if(isSystemInDarkTheme()) White else RaisanBlack,
                    modifier = Modifier
                        .padding(bottom = 10.dp, start = 10.dp)
                        .clickable { navController.navigate("UsuarioDetial/${it.email}") }
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
fun EnumaradoSouvenirs(souvenirsViewModel: SouvenirsViewModel){
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


/**
 * Muestra el botón de pedir en el caso de que se pueda mostrar o el mensaje de alerta
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param loginViewModel viewmodel del login
 * @param navController navegacion
 */
@Composable
fun ButtonPedirOrMsg(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, navController: NavController){
    val souvenirCarrito by souvenirsViewModel.souvenirCarrito.collectAsState()
    val context = LocalContext.current
    val soundEffect = MediaPlayer.create(context, R.raw.pedido_sound)

    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

    //si no hay souvenirs en el carrito
    if(souvenirCarrito.isNotEmpty()){
        Button(onClick = {
            souvenirsViewModel.saveSouvenirInPedido {
                Toast.makeText(context,
                    "Souvenirs Pedidos",
                    Toast.LENGTH_SHORT).show()
            }
            souvenirsViewModel.deleteSouvenirInCarritoFromUser()
            souvenirsViewModel.vaciarSouvenirsCarrito()

            soundEffect.start()
            //tengo que eliminar los souvenirs de ese usuario de la BDD
        },

            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Redwood)) {
            Text(text = "PEDIR",
                fontFamily = KiwiMaru
            )
        }
    }else{
        if (loginViewModel.showAlert) {
            // Mostrar un diálogo si el usuario no ha iniciado sesión
            AlertDialog(
                onDismissRequest = { /* No hacer nada en el cierre del diálogo */ },
                title = {
                    Text(
                        text = "No has iniciado sesión para ver tus elementos en el carrito",
                        fontFamily = KiwiMaru,
                        color = RaisanBlack,
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            navController.navigate("InicioSesion")
                        }
                    ) {
                        Text(
                            text = "Ir a Inicio de Sesión",
                            fontFamily = KiwiMaru,
                            color = Redwood
                        )
                    }
                }
            )
        }
    }
}









