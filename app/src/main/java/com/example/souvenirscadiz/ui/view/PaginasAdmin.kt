package com.example.souvenirscadiz.ui.view


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
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

/**
 * Detalle de los usuarios
 * @param loginViewModel viewmodel del login
 * @param souvenirsViewModel viewmodel del login
 * @param navController navegacion
 */
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


/**
 * Permite añadir nuevos souvenirs
 * @param loginViewModel viewmodel del login
 * @param souvenirsViewModel viewmodel de souvenir
 * @param navController navegacion
 */
@Composable
fun AnadirSouvenir(loginViewModel: LoginViewModel, souvenirsViewModel: SouvenirsViewModel, navController: NavController, cloudStorageManager:CloudStorageManager){
    var context = LocalContext.current
    var nombre by souvenirsViewModel._nombre
    var referencia by souvenirsViewModel._referencia
    var precio by souvenirsViewModel._precio
    var stock by souvenirsViewModel._stock
    var url by souvenirsViewModel._url
    var selectedImageUri by souvenirsViewModel.selectedImageUri

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            uri?.let {
                cloudStorageManager.uploadImgSouvenir(souvenirsViewModel, uri) { success, downloadUrl ->
                    if (success) {
                        url = downloadUrl
                        souvenirsViewModel.updateSouvenirImage(downloadUrl)
                        Toast.makeText(context, "Imagen subida a firebase", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Error al subir la imagen", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }


        }
    )

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

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre= it },
                label = { Text("Nombre del souvenir") },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            OutlinedTextField(
                value = referencia,
                onValueChange = { referencia = it },
                label = { Text("referencia del souvenir") },
                modifier = Modifier.padding(horizontal = 16.dp)
            )


            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("precio del souvenir") },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("stock del souvenir") },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            MenuTiposSouvenir(souvenirsViewModel)

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(10.dp)
                    .size(100.dp)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedImageUri)
                        .build(),
                    contentDescription = null,
                    loading = { CircularProgressIndicator() },
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Button(onClick = {
                souvenirsViewModel.saveSouvenir {
                    Toast.makeText(context, "souvenir guardado", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "GUARDAR")
            }
            
        }
    }
}





