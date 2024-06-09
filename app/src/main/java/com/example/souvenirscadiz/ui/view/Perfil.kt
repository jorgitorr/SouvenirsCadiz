package com.example.souvenirscadiz.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.util.CloudStorageManager
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver
import com.example.souvenirscadiz.ui.theme.Teal
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


/**
 * Perfil
 *
 * @param loginViewModel
 * @param navController
 * @param souvenirsViewModel
 * @param cloudStorageManager contiene el acceso a las imagenes de firebase
 */
@Composable
fun Perfil(loginViewModel: LoginViewModel, navController: NavController, souvenirsViewModel: SouvenirsViewModel,
           cloudStorageManager:CloudStorageManager){

    val context = LocalContext.current
    var selectedImageUri by loginViewModel.selectedImageUri
    val userState by loginViewModel.userState.collectAsState()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            uri?.let {
                cloudStorageManager.uploadImgProfile(loginViewModel.getCurrentUser()!!.uid, uri) { success, downloadUrl ->
                    if (success) {
                        loginViewModel.updateUserProfileImage(downloadUrl)
                    } else {
                        Toast.makeText(context, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    LaunchedEffect(true){
        loginViewModel.fetchUser()
    }

    Scaffold(
        topBar = {
            if(loginViewModel.checkAdmin()){
                HeaderAdmin(navController, souvenirsViewModel)
            }else{
                Header(navController, souvenirsViewModel)
            } },
        bottomBar = {
            if(loginViewModel.checkAdmin()){
                FooterAdmin(navController, souvenirsViewModel, loginViewModel)
            }else{
                Footer(navController, souvenirsViewModel, loginViewModel)
            } }
        ,containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(10.dp)
                    .size(180.dp)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userState?.imagen)
                        .build(),
                    contentDescription = null,
                    loading = { CircularProgressIndicator() },
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            //email
            Text(text = loginViewModel.email,
                color = RaisanBlack,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp))
            Spacer(modifier = Modifier.height(20.dp))

            //boton para cerra sesion
            Button(onClick = {
                loginViewModel.signOut()
                navController.navigate("Principal")
                souvenirsViewModel.setSelectedItem("Principal")
                //vacia los souvenirs en las listas del usuario
                souvenirsViewModel.vaciarSouvenirsFav()
                souvenirsViewModel.vaciarSouvenirsCarrito()
            },
                colors = ButtonDefaults.buttonColors(Teal)) {
                Text(text = "Cerrar Sesion",
                    style = TextStyle(Silver)
                )
            }

        }
    }
}


/**
 * Inicio sesion
 *
 * @param souvenirsViewModel
 * @param loginViewModel
 * @param navController
 */
@Composable
fun InicioSesion(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Silver)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Silver),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "SOUVENIRS CADIZ",
                fontFamily = KneWave,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IntroducirEmail(loginViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        IntroducirContrasenia(loginViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        BotonAceptarInicio(loginViewModel, navController, souvenirsViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        InicioSesionGoogle(souvenirsViewModel, loginViewModel, navController)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¿No tienes cuenta?", fontFamily = KiwiMaru)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Registrate.", modifier = Modifier
            .clickable { navController.navigate("Registro") },
            fontFamily = KiwiMaru,
            color = Redwood)


        if(loginViewModel.checkAdmin()){ //si es Admin
            navController.navigate("PrincipalAdmin")//se dirige a una pantalla esclusiva para el admin
        }

    }
}

/**
 * Registro
 *
 * @param souvenirsViewModel
 * @param loginViewModel
 * @param navController
 */
@Composable
fun Registro(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Silver)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Silver),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "SOUVENIRS CADIZ",
                fontFamily = KneWave,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IntroducirUsuario(loginViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        IntroducirEmail(loginViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        IntroducirContrasenia(loginViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        BotonAceptarRegistro(loginViewModel, navController, souvenirsViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¿Tienes cuenta?", fontFamily = KiwiMaru)
        Spacer(modifier = Modifier.height(16.dp))
        InicioSesionGoogle(souvenirsViewModel, loginViewModel, navController)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Inicia Sesión.", modifier = Modifier
            .clickable { navController.navigate("InicioSesion") },
            fontFamily = KiwiMaru,
            color = Redwood)
    }
}

/**
 * Create image file
 *
 * @return Archivo de imagen
 */
@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}








