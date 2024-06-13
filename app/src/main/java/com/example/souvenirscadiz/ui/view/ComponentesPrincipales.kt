package com.example.souvenirscadiz.ui.view


import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Cerulean
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
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
                            if (selectedItem != "Principal") {
                                souvenirsViewModel.setSelectedItem("Principal")
                                navController.navigate("Principal")
                            }
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
                            if (selectedItem != "Favoritos") {
                                souvenirsViewModel.setSelectedItem("Favoritos")
                                navController.navigate("Favoritos")
                            }
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
                            if (selectedItem != "Perfil") {
                                if (loginViewModel.getCurrentUser() != null) {
                                    navController.navigate("Perfil")
                                } else {
                                    navController.navigate("InicioSesion")
                                }
                                souvenirsViewModel.setSelectedItem("Perfil")
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
                modifier = Modifier.clickable {
                    if(selectedItem!="Detalles"){
                        navController.navigate("Detalles")
                        souvenirsViewModel.setSelectedItem("Detalles")
                    }
                })
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
                        if(selectedItem!="Carrito"){
                            navController.navigate("Tienda")
                            souvenirsViewModel.setSelectedItem("Carrito")
                        }
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
fun Buscador(souvenirsViewModel: SouvenirsViewModel, navController: NavController) {
    val active by souvenirsViewModel.active.collectAsState()
    val query by souvenirsViewModel.query.collectAsState()
    val souvenirs by souvenirsViewModel.souvenirs.collectAsState()

    val filteredSouvenirs = if (query.isNotEmpty()) {
        souvenirs.filter { it.nombre.contains(query, ignoreCase = true) }
    } else {
        souvenirs
    }

    // Actualiza los souvenirs filtrados en el ViewModel si es necesario
    LaunchedEffect(filteredSouvenirs) {
        souvenirsViewModel.souvenirsFiltrados(filteredSouvenirs)
    }

    SearchBar(
        modifier = Modifier
            .width(345.dp)
            .padding(start = 25.dp),
        query = query,
        onQueryChange = { souvenirsViewModel.setQuery(it) },
        onSearch = { souvenirsViewModel.setActive(false) },
        active = active,
        onActiveChange = { souvenirsViewModel.setActive(it) },
        placeholder = {
            Text(
                text = "Search",
                color = if (isSystemInDarkTheme()) White else RaisanBlack,
                fontFamily = KiwiMaru
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "BUSCADOR")
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "CERRAR",
                modifier = Modifier.clickable {
                    souvenirsViewModel.setActive(false)
                    souvenirsViewModel.setQuery("")
                }
            )
        }
    ) {
        filteredSouvenirs.forEach { souvenir ->
            Text(
                text = souvenir.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = KiwiMaru,
                color = if (isSystemInDarkTheme()) White else RaisanBlack,
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 10.dp)
                    .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }
            )
        }
    }
}





@Composable
fun Share(text: String, context: android.content.Context) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    Button(
        onClick = {
            val packageManager = context.packageManager
            val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
                `package` = "com.whatsapp"
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }

            val resolveInfoList = packageManager.queryIntentActivities(sendIntent, 0)
            val resolvedApps = resolveInfoList.map { it.activityInfo.packageName }
            if (resolvedApps.contains("com.whatsapp")) {
                val chooserIntent = Intent.createChooser(whatsappIntent, null)
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(sendIntent))
                startActivity(context, chooserIntent, null)
            } else {
                startActivity(context, shareIntent, null)
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Redwood)
    ) {
        Icon(imageVector = Icons.Default.Share, contentDescription = null)
        Text("Share", modifier = Modifier.padding(start = 8.dp))
    }
}



/**
 * Filtro
 *
 * @param souvenirsViewModel
 * @param loginViewModel
 * @param navController
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
            val souvenirs by souvenirsViewModel.souvenirs.collectAsState()
            var tipoElegido by souvenirsViewModel.tipoElegido
            var sliderPosition by souvenirsViewModel.sliderPosition

            LazyColumn(
                modifier = Modifier
                    .background(Silver)
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                item {
                    Text(
                        text = "TIPO DE SOUVENIR",
                        fontFamily = KiwiMaru,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                item {
                    MenuTiposSouvenir(souvenirsViewModel, onTipoSelected = { tipo ->
                        tipoElegido = tipo
                    })

                    Spacer(modifier = Modifier.padding(top = 50.dp))
                }
                item {
                    Text(
                        text = "PRECIO MÍNIMO",
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
                        Text(text = String.format("%.2f €", sliderPosition))
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(top = 50.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ){
                        Button(
                            onClick = {
                                Log.d("tipoElegido",tipoElegido.toString())
                                val filteredSouvenirs = souvenirs.filter { souvenir ->
                                    souvenir.tipo.equals(tipoElegido.toString(), ignoreCase = true) && souvenir.precio.toFloat() >= sliderPosition
                                }

                                souvenirsViewModel.updateSouvenirs(filteredSouvenirs)
                                souvenirsViewModel.setSelectedItem("Principal")
                                navController.navigate("Principal")
                            }
                        ) {
                            Text(text = "Filtrar")
                        }
                    }

                }
            }
        }
    }
}














