package com.example.souvenirscadiz.ui.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.Silver
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.souvenirscadiz.data.util.CloudStorageManager

/**
 * pantalla principal del admin, que tiene todos los souvenirs
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param navController navegacion entre paginas
 * @param loginViewModel viewmodel del login
 */
@Composable
fun AdminPrincipal(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel,
                   cloudStorageManager:CloudStorageManager){
    Scaffold(
        floatingActionButton = { FloatingActionButton(
            onClick = {

        }) {
            Icon(imageVector = Icons.Default.Add,
                contentDescription = "Add souvenir")
        }},
        topBar = {
            HeaderAdmin(navController, souvenirsViewModel)
        },
        bottomBar = {
            FooterAdmin(navController,souvenirsViewModel, loginViewModel)
        }, containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Search(souvenirsViewModel, navController)//buscador
            EnumaradoSouvenirs(souvenirsViewModel)//todos los enumerados
            SouvenirsList(navController, souvenirsViewModel, loginViewModel)//lista de souvenirs
        }
    }
}

/**
 * le pasa todos los usuarios de la base de datos para que pueda borrar uno si quiere
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param navController navegacino
 * @param loginViewModel viewmodel del login
 */
@Composable
fun Usuarios(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    LaunchedEffect(true){
        loginViewModel.fetchUsers()
    }
    Scaffold(
        topBar = {
            HeaderAdmin(navController, souvenirsViewModel)
        },
        bottomBar = {
            FooterAdmin(navController,souvenirsViewModel, loginViewModel)
        }, containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SearchUsuarios(loginViewModel, navController)
            UsuariosList(navController, loginViewModel, souvenirsViewModel)
        }
    }
}

@Composable
fun UsuarioDetail(loginViewModel: LoginViewModel, souvenirsViewModel: SouvenirsViewModel, navController: NavController){
    Scaffold(
        topBar = {
            HeaderAdmin(navController, souvenirsViewModel)
        },
        bottomBar = {
            Footer(navController,souvenirsViewModel, loginViewModel)
        }, containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

        }
    }
}




@Composable
fun CoilImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalContext.current)
            .data(data = imageUrl)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                transformations(RoundedCornersTransformation(topLeft = 20f, topRight = 20f, bottomLeft = 20f, bottomRight = 20f))
            })
            .build()
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.padding(6.dp),
        contentScale = contentScale,
    )
}





