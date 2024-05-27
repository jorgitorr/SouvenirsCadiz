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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.souvenirscadiz.data.util.CloudStorageManager

/**
 * Admin principal
 *
 * @param souvenirsViewModel
 * @param navController
 * @param loginViewModel
 */
@Composable
fun AdminPrincipal(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
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
            Search(souvenirsViewModel, navController)//buscador
            //EnumaradoSouvenirs(souvenirsViewModel)//todos los enumerados
            SouvenirsList(navController, souvenirsViewModel, loginViewModel)//lista de souvenirs
        }
    }
}

/**
 * Usuarios
 *
 * @param souvenirsViewModel
 * @param navController
 * @param loginViewModel
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
            UsuariosList(loginViewModel)
        }
    }
}


/**
 * Anadir souvenir
 *
 * @param loginViewModel
 * @param souvenirsViewModel
 * @param navController
 * @param cloudStorageManager
 */
@Composable
fun AnadirSouvenir(loginViewModel: LoginViewModel, souvenirsViewModel: SouvenirsViewModel, navController: NavController, cloudStorageManager:CloudStorageManager){
    val context = LocalContext.current
    var nombre by souvenirsViewModel.nombre
    var referencia by souvenirsViewModel.referencia
    var precio by souvenirsViewModel.precio
    var stock by souvenirsViewModel.stock
    var url by souvenirsViewModel.url
    var selectedImageUri by souvenirsViewModel.selectedImageUri

    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirs() //recorre la lista de souvenirs y añade el nuevo
    }

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





