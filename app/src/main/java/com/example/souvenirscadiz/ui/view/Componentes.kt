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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
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
fun Caja(navController: NavController, souvenir: Souvenir, url:Int, souvenirsViewModel: SouvenirsViewModel){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Silver, shape = RoundedCornerShape(5.dp))
        .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.TopEnd){
                Image(
                    painter = painterResource(id = url),
                    contentDescription = souvenir.nombre,
                    contentScale = ContentScale.Crop, //para ajustar las imagenes al tamaño del cuadrado
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }

                )
                FavoriteButton(souvenir, souvenirsViewModel) //icono de fav
                ShopingCartButton(souvenir, souvenirsViewModel)
            }

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
                    color = RaisanBlack,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Text(
                    text = souvenir.referencia,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    color = RaisanBlack,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )

                Text(
                    text = "${souvenir.precio}€",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    color = RaisanBlack,
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
 */
@Composable
fun Caja(navController: NavController, souvenir: SouvenirState, url:Int){
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
                contentScale = ContentScale.Crop,
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
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                    color = RaisanBlack
                )
                Text(
                    text = souvenir.referencia,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                    color = RaisanBlack
                )
                Text(
                    text = "${souvenir.precio}€",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 3.dp, end = 4.dp),
                    color = RaisanBlack
                )
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
    val souvenirsGuardado by souvenirsViewModel.souvenirFav.collectAsState()

    if(souvenirs.isEmpty()){
        LazyColumn{
            items(souvenirsPre){ souvenirP->
                val url = "img${souvenirP.url}"
                val resourceId = souvenirsViewModel.getResourceIdByName(url)
                //recorre los souvenirs guardados para comprobar si es de los que muestra
                for(souvenirG in souvenirsGuardado){
                    if(souvenirP.referencia==souvenirG.referencia){
                        souvenirP.guardadoFav = true
                    }
                }
                Caja(navController,souvenirP, resourceId, souvenirsViewModel)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }else{
        LazyColumn{
            items(souvenirs){ souvenir->
                val url = "img${souvenir.url}"
                val resourceId = souvenirsViewModel.getResourceIdByName(url)
                //recorre los souvenirs guardados para comprobar si es de los que muestra
                for(souvenirG in souvenirsGuardado){
                    if(souvenir.referencia == souvenirG.referencia){
                        souvenir.guardadoFav = true
                    }
                }
                Caja(navController,souvenir, resourceId, souvenirsViewModel)
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
    val numberSouvenir by souvenirsViewModel.numberSouvenir.collectAsState()

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
                if(numberSouvenir>0){
                    Badge {
                        Text(text = "$numberSouvenir")
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
            color = Silver)}, // DCS - Muestra un texto placeholder en la barra de búsqueda.
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
                    color = Silver,
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
 * Componente para el botón de favoritos
 * @param souvenir
 * @param souvenirsViewModel viewmodel de los souvenirs para acceder al metodo que guarda souvenirs en la BDD
 */
@Composable
fun FavoriteButton(
    souvenir: Souvenir,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current

    IconToggleButton(
        checked = souvenir.guardadoFav,
        onCheckedChange = {
            souvenir.guardadoFav = !souvenir.guardadoFav
        }
    ) {
        Icon(
            tint = if (!souvenir.guardadoFav) RaisanBlack else Redwood,
            imageVector = if (souvenir.guardadoFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    if(!souvenir.guardadoFav){
                        //si el souvenir no esta guardado
                        souvenirsViewModel.saveSouvenirInFav {
                            Toast
                                .makeText(context, "Souvenir guardado", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }else{
                        souvenirsViewModel.deleteSouvenirInFav {
                            Toast
                                .makeText(context, "Souvenir eliminado", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    souvenir.guardadoFav = !souvenir.guardadoFav
                }
        )
    }

}



@Composable
fun ShopingCartButton(
    souvenir: Souvenir,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current

    IconToggleButton(
        checked = souvenir.guardadoCarrito,
        onCheckedChange = {
            souvenir.guardadoCarrito = !souvenir.guardadoCarrito
        },
        modifier = Modifier.padding(top = 280.dp, end = 340.dp) //posicion del icono
    ) {
        Icon(
            //si el elemento esta guardado en el carrito, el icono cambia a eliminar del guardado
            imageVector = if(!souvenir.guardadoCarrito) Icons.Default.AddShoppingCart else Icons.Default.RemoveShoppingCart,
            tint = if (!souvenir.guardadoCarrito) RaisanBlack else Redwood,
            contentDescription = "Cesta de la compra",
            modifier = Modifier
                .size(30.dp)
                .clickable {

                    if(!souvenir.guardadoCarrito){
                        souvenirsViewModel.saveSouvenirInCarrito {
                            Toast
                                .makeText(
                                    context,
                                    "Souvenir guardado en carrito",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                        souvenir.guardadoCarrito = !souvenir.guardadoCarrito
                    }else{
                        souvenirsViewModel.deleteSouvenirInCarrito({
                            Toast
                                .makeText(
                                    context,
                                    "Souvenir guardado en carrito",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },souvenir)
                    }

                }

        )
    }

}


@Composable
fun CajaCarrito(
    navController: NavController,
    souvenir: SouvenirState,
    url: Int,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current
    var cantidadSouvenir by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Silver, shape = RoundedCornerShape(5.dp))
            .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))
            .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Imagen
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(url)
                    .build(),
                contentDescription = souvenir.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }

            )
            Spacer(modifier = Modifier.height(8.dp))
            // Detalles del souvenir
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                //boton de eliminar souvenir del carrito
                EliminarButton(souvenirsViewModel, souvenir)

                Text(
                    text = souvenir.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Ref: ${souvenir.referencia}",
                    fontSize = 14.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${souvenir.precio}€",
                    fontSize = 14.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                OutlinedTextField(value = cantidadSouvenir,
                    onValueChange = {
                        if (it.isDigitsOnly()) {
                            cantidadSouvenir = it
                        }else{
                            Toast.makeText(context,"Has introducido un campo erroneo",Toast.LENGTH_SHORT).show()
                        }},
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                souvenir.cantidad = cantidadSouvenir

            }


        }
    }
}


@Composable
fun EliminarButton(souvenirsViewModel: SouvenirsViewModel, souvenir: SouvenirState) {
    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

    var eliminar = false
    val context = LocalContext.current

    IconToggleButton(
        checked = eliminar,
        onCheckedChange = {
            eliminar = !eliminar
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = Redwood,
            modifier = Modifier.clickable {
                souvenirsViewModel.deleteSouvenirInCarrito(
                    {
                        Toast.makeText(context,
                            "Has eliminado un souvenir del carrito",
                            Toast.LENGTH_SHORT).show()
                    }, souvenir
                )

                souvenir.guardadoCarrito = false
            }
        )
    }

}



