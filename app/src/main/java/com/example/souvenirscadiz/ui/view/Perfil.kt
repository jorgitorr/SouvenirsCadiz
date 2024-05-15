package com.example.souvenirscadiz.ui.view

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.util.Constant.Companion.TOKEN
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver
import com.example.souvenirscadiz.ui.theme.Teal
import com.example.souvenirscadiz.ui.theme.White
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


/**
 * Página de perfil
 * muestra el nombre y el correo
 * @param loginViewModel viewmodel del login
 * @param navController navegacion
 */
@Composable
fun Perfil(loginViewModel: LoginViewModel, navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val context = LocalContext.current

    LaunchedEffect(true){
        loginViewModel.fetchUser {
            Toast.makeText(context,"Has iniciado sesión ${loginViewModel.userName}",Toast.LENGTH_SHORT).show()
        } //para coger el usuario actual
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


            //user
            Text(text = loginViewModel.userName,
                color = RaisanBlack,
                style = TextStyle(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))

            //email
            Text(text = loginViewModel.email,
                color = RaisanBlack,
                style = TextStyle(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))


            //boton para modificar el perfil
            /*Button(onClick = { navController.navigate("ModificarPerfil") },
                colors = ButtonDefaults.buttonColors(RaisanBlack)) {
                Text(text = "Modificar",
                    style = TextStyle(color = Silver))
            }*/

            //boton para cerra sesion
            Button(onClick = {
                loginViewModel.signOut()
                navController.navigate("Principal")
                souvenirsViewModel.setSelectedItem("Principal")
                //hacer que al cerrar sesion me cierre sesion en google
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
 * Inicio de sesion
 * @param loginViewModel viewmodel del login
 * @param navController navegacion
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
                modifier = Modifier.padding(start = 8.dp)
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
 * @param loginViewModel viewModel del login
 * @param navController navegacion
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








