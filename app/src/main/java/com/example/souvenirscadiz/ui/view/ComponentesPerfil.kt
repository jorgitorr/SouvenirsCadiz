@file:Suppress("DEPRECATION")

package com.example.souvenirscadiz.ui.view

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.data.util.Constant
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver
import com.example.souvenirscadiz.ui.theme.White
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Introducir contrasenia
 *
 * @param loginViewModel
 */
@Composable
fun IntroducirContrasenia(loginViewModel: LoginViewModel){
    var hidden by remember { mutableStateOf(true) }
    OutlinedTextField(
        label = { Text(text = "Contraseña", color = RaisanBlack, fontFamily = KiwiMaru) },
        value = loginViewModel.password,
        onValueChange = { loginViewModel.changePassword(it) },
        visualTransformation =
        if (hidden) PasswordVisualTransformation() else VisualTransformation.None,//3
        trailingIcon = {
            IconButton(onClick = { hidden = !hidden }) {
                val vector = if (hidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (hidden) "Ocultar contraseña" else "Revelar contraseña" //6
                Icon(imageVector = vector, contentDescription = description, tint = RaisanBlack)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
    )
}


/**
 * Introducir email
 *
 * @param loginViewModel
 */
@Composable
fun IntroducirEmail(loginViewModel: LoginViewModel){
    OutlinedTextField(
        label = { Text(text = "Email", color = RaisanBlack, fontFamily = KiwiMaru) },
        value = loginViewModel.email,
        onValueChange = { loginViewModel.changeEmail(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
    )
}


/**
 * Introducir usuario
 *
 * @param loginViewModel
 */
@Composable
fun IntroducirUsuario(loginViewModel: LoginViewModel){
    OutlinedTextField(
        label = { Text(text = "Usuario", color = RaisanBlack, fontFamily = KiwiMaru) },
        value = loginViewModel.userName,
        onValueChange = { loginViewModel.changeUserName(it) },
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
    )
}


/**
 * Boton aceptar registro
 *
 * @param loginViewModel
 * @param navController
 * @param souvenirsViewModel
 */
@Composable
fun BotonAceptarRegistro(loginViewModel: LoginViewModel, navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val context = LocalContext.current
    Button(
        onClick = {
            if(loginViewModel.password.length<6){
                Toast.makeText(context,"La contraseña tiene que ser mayor de 6 carácteres", Toast.LENGTH_SHORT).show()
            }
            else if(loginViewModel.email.isEmpty()){
                Toast.makeText(context,"No has introducido el email", Toast.LENGTH_SHORT).show()
            }
            else if(loginViewModel.password.isEmpty()){
                Toast.makeText(context,"No has introducido la contraseña", Toast.LENGTH_SHORT).show()
            }
            else if(loginViewModel.userName.isEmpty()){
                Toast.makeText(context,"No has introducido la contraseña", Toast.LENGTH_SHORT).show()
            }else {
                loginViewModel.checkUserExists(loginViewModel.email){
                    exists ->
                    if(exists){
                        Toast.makeText(context,"Es un usario existente", Toast.LENGTH_SHORT).show()
                    }else{
                        loginViewModel.createUser {
                            navController.navigate("Principal")
                            souvenirsViewModel.setSelectedItem("Principal")}
                    }
                }
            } },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 50.dp),
        colors = ButtonDefaults.buttonColors(Redwood),
        shape = CutCornerShape(1.dp)
    ) {
        Text(
            text = "Registrarte",
            fontFamily = KiwiMaru,
            color = White
        )
    }
}


/**
 * Boton aceptar inicio
 *
 * @param loginViewModel
 * @param navController
 * @param souvenirsViewModel
 */
@Composable
fun BotonAceptarInicio(loginViewModel: LoginViewModel, navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    Button(
        onClick = {
            loginViewModel.login {
            navController.navigate("Principal")
            souvenirsViewModel.setSelectedItem("Principal")
            } },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 50.dp),
        colors = ButtonDefaults.buttonColors(Redwood),
        shape = CutCornerShape(1.dp)
    ) {
        Text(
            text = "Iniciar Sesión",
            fontFamily = KiwiMaru,
            color = White
        )
    }
}


/**
 * Modificar perfil
 *
 * @param loginViewModel
 * @param navController
 * @param souvenirsViewModel
 */
@Composable
fun ModificarPerfil(loginViewModel: LoginViewModel, navController: NavController, souvenirsViewModel: SouvenirsViewModel){

    Scaffold(
        topBar = { Header(navController, souvenirsViewModel) },
        bottomBar = { Footer(navController, souvenirsViewModel, loginViewModel) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var contraseniaActual by remember { mutableStateOf("") }

            //si introduce la contrasenia actual
            if(loginViewModel.password==contraseniaActual){
                //muestra una pantalla para poder cambiar el nombre
                var nuevoNombreUsuario by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = nuevoNombreUsuario,
                    onValueChange = { nuevoNombreUsuario = it },
                    label = { Text("Nuevo nombre de usuario") },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                loginViewModel.changeUserName(nuevoNombreUsuario)
            }else{
                //sigue preguntando por la contrasenia
                Text(text = "Introduce la contrasenia actual")
                OutlinedTextField(
                    value = contraseniaActual,
                    onValueChange = { contraseniaActual = it },
                    label = { Text("Nuevo nombre de usuario") },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }



            Spacer(modifier = Modifier.height(20.dp))


            Spacer(modifier = Modifier.height(20.dp))
            //boton para suprimir el perfil
            Button(onClick = {  },
                colors = ButtonDefaults.buttonColors(Redwood)) {
                Text(text = "Suprimir",
                    style = TextStyle(color = Silver)
                )
            }



        }
    }
}


/**
 * Inicio sesion google
 *
 * @param souvenirsViewModel
 * @param loginViewModel
 * @param navController
 */
@Composable
fun InicioSesionGoogle(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, navController: NavController){
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .StartActivityForResult()){//nos abre un activity para hacer login de google
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try{
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            loginViewModel.singInWithGoogleCredential(
                credential, {
                    navController.navigate("Principal")
                    souvenirsViewModel.setSelectedItem("Principal")
                    Toast.makeText(context,"Has iniciado sesión", Toast.LENGTH_LONG).show()
                },
                context)
        }catch (e:Exception){
            Log.e("InicioSesionGoogle", "Error al iniciar sesión con Google: ${e.message}", e)
        }
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clip(RoundedCornerShape(10.dp))
        .clickable {
            val opciones = GoogleSignInOptions
                .Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                .requestIdToken(Constant.TOKEN)
                .requestEmail()
                .build()
            val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
            launcher.launch(googleSingInCliente.signInIntent)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){

        Text(text = "Login Google",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = KiwiMaru
        )
    }
}


/**
 * Menu tipos souvenir
 *
 * @param souvenirsViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTiposSouvenir(souvenirsViewModel: SouvenirsViewModel, onTipoSelected: (String) -> Unit) {
    val context = LocalContext.current
    val tiposSouvenir = Tipo.entries.toTypedArray()
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("TODOS") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp),
        contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            TextField(
                value = selectedText,
                onValueChange = {selectedText = onTipoSelected.toString()},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tiposSouvenir.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.valor, color = RaisanBlack) },
                        onClick = {
                            selectedText = item.valor
                            expanded = false
                            onTipoSelected(item.valor)
                            Toast.makeText(context, item.valor, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

