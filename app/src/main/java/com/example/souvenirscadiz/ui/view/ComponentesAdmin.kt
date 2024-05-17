package com.example.souvenirscadiz.ui.view


import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Cerulean
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver
import com.example.souvenirscadiz.ui.theme.White

/**
 * Footer del administrador
 * @param navController navegacion entre páginas
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param loginViewModel viewmodel de login
 */
@Composable
fun FooterAdmin(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel){
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
                    imageVector = Icons.Default.PersonSearch,
                    contentDescription = "Usuarios",
                    tint = if(selectedItem=="Usuarios") Cerulean else RaisanBlack,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable {
                            souvenirsViewModel.setSelectedItem("Usuarios")
                            navController.navigate("Usuarios")
                        }
                )
                Text("Users")
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
 * Header del administrador
 * @param navController navegacion entre páginas
 */
@Composable
fun HeaderAdmin(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val selectedItem by souvenirsViewModel.selectedItem.collectAsState()

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(Silver)){
        Row (modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            /*Image(painter = painterResource(id = R.drawable.logo), contentDescription = "LOGO",
                modifier = Modifier.clickable { navController.navigate("Detalles") })
            */

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "AnadirSouvenir",
                tint = if(selectedItem=="AnadirSouvenir") Cerulean else RaisanBlack,
                modifier = Modifier.clickable {
                    souvenirsViewModel.setSelectedItem("AnadirSouvenir")
                    navController.navigate("AnadirSouvenir")
                })

            Text(text = "ADMIN SOUVENIRS CADIZ",
                fontFamily = KneWave
            )

            Icon(
                imageVector = Icons.Default.Sell,
                contentDescription = "Pedidos",
                tint = if(selectedItem=="Pedidos") Cerulean else RaisanBlack,
                modifier = Modifier.clickable {
                    souvenirsViewModel.setSelectedItem("Pedidos")
                    navController.navigate("Pedidos")
                })

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
 * Menu Drop Down
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTiposSouvenir(souvenirsViewModel:SouvenirsViewModel) {
    val context = LocalContext.current
    val coffeeDrinks = Tipo.entries.toTypedArray()
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(coffeeDrinks[0].valor) }
    var tipo by souvenirsViewModel._tipo

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = { tipo = selectedText},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.valor, color = White) },
                        onClick = {
                            selectedText = item.valor
                            tipo = selectedText
                            expanded = false
                            Toast.makeText(context, item.valor, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

