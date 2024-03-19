package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver
import com.example.souvenirscadiz.ui.theme.White





/**
 * Página de perfil
 * @param loginViewModel viewmodel del login
 * @param navController navegacion
 */
@Composable
fun Perfil(loginViewModel: LoginViewModel, navController: NavController){

}


/**
 * Inicio de sesion
 * @param loginViewModel viewmodel del login
 * @param navController navegacion
 */
@Composable
fun InicioSesion(loginViewModel: LoginViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Silver)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Silver),
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
        IntroducirContrasenia(loginViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        BotonAceptarRegistro(loginViewModel, navController)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¿No tienes cuenta?")
        Text(text = "Registrate.", modifier = Modifier
            .clickable { navController.navigate("Registro") })
    }
}

/**
 * Registro
 * @param loginViewModel viewModel del login
 * @param navController navegacion
 */
@Composable
fun Registro(loginViewModel: LoginViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Silver)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
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
        BotonAceptarRegistro(loginViewModel, navController)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¿Tienes cuenta?")
        Text(text = "Inicia Sesión.", modifier = Modifier
            .clickable { navController.navigate("InicioSesion") })
    }
}

/**
 * Introducir contraseña para crear un nuevo usuario
 * @param loginViewModel le pasamos el viewmodel para que ingrese y guarde la contraseña
 */
@Composable
fun IntroducirContrasenia(loginViewModel: LoginViewModel){
    var hidden by remember { mutableStateOf(true) } //1
    Text(text = "Contraseña", color = RaisanBlack,
        fontFamily = KiwiMaru
    )
    OutlinedTextField(
        value = loginViewModel.password,
        onValueChange = { loginViewModel.changePassword(it) },
        visualTransformation =
        if (hidden) PasswordVisualTransformation() else VisualTransformation.None,//3
        trailingIcon = {
            IconButton(onClick = { hidden = !hidden }) {
                val vector = if (hidden) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (hidden) "Ocultar contraseña" else "Revelar contraseña" //6
                Icon(imageVector = vector, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier
            .background(White)
            .clip(RoundedCornerShape(8.dp))
    )
}


/**
 * Introducir email para crear un nuevo usuario
 * @param loginViewModel le pasamos el viewmodel para que guarde el email introducido
 */
@Composable
fun IntroducirEmail(loginViewModel: LoginViewModel){
    Text(text = "Email", color = RaisanBlack,
        fontFamily = KiwiMaru
    )
    OutlinedTextField(
        value = loginViewModel.email,
        onValueChange = { loginViewModel.changeEmail(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
            .background(White)
            .clip(RoundedCornerShape(8.dp))
    )
}



/**
 * Componente que nos permite ingresar un nuevo usuario
 * @param loginViewModel le pasamos el viewmodel del login que guarda la informacion de este usuario nuevo
 * y la añade a la base de datos
 */
@Composable
fun IntroducirUsuario(loginViewModel: LoginViewModel){
    Text(text = "Usuario", color = RaisanBlack, fontFamily = KiwiMaru)
    OutlinedTextField(
        value = loginViewModel.userName,
        onValueChange = { loginViewModel.changeUserName(it) },
        modifier = Modifier
            .background(White)
            .clip(RoundedCornerShape(8.dp))
    )
}


/**
 * Boton de aceptar del registro
 * @param loginViewModel le pasamos el viewmodel del login
 * @param navController le pasamos el nav que nos lleva a donde queramos al darle a aceptar y
 * registrar un nuevo usuario en la base de datos
 */
@Composable
fun BotonAceptarRegistro(loginViewModel: LoginViewModel, navController: NavController){
    Button(
        onClick = { loginViewModel.createUser { navController.navigate("Principal") } },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 50.dp),
        colors = ButtonDefaults.buttonColors(Redwood),
        shape = CutCornerShape(1.dp)
    ) {
        Text(
            text = "Log in",
            fontFamily = KiwiMaru,
            color = White
        )
    }
}




