package com.example.souvenirscadiz.ui.view


import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import coil.compose.AsyncImage
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.util.Storage
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.KneWave
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood
import com.example.souvenirscadiz.ui.theme.Silver
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

    Scaffold(
        topBar = { Header(navController) },
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

            //imagen de perfil
            SinglePhotoPicker()

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
            Button(onClick = { navController.navigate("ModificarPerfil") },
                colors = ButtonDefaults.buttonColors(RaisanBlack)) {
                Text(text = "Modificar",
                    style = TextStyle(color = Silver))
            }

            Spacer(modifier = Modifier.height(20.dp))
            //boton para suprimir el perfil
            Button(onClick = {  },
                colors = ButtonDefaults.buttonColors(Redwood)) {
                Text(text = "Suprimir",
                    style = TextStyle(color = Silver))
            }

            Button(onClick = { loginViewModel.signOut()
                             navController.navigate("Perfil")},
                colors = ButtonDefaults.buttonColors(White)) {
                Text(text = "Cerrar Sesion",
                    style = TextStyle(RaisanBlack)
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
        BotonAceptarInicio(loginViewModel, navController)
        Spacer(modifier = Modifier.height(16.dp))
        //InicioSesionGoogle(loginViewModel, navController) -> no funciona ahora mismo
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¿No tienes cuenta?", fontFamily = KiwiMaru)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Registrate.", modifier = Modifier
            .clickable { navController.navigate("Registro") },
            fontFamily = KiwiMaru,
            color = Redwood)

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
        BotonAceptarRegistro(loginViewModel, navController)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¿Tienes cuenta?", fontFamily = KiwiMaru)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Inicia Sesión.", modifier = Modifier
            .clickable { navController.navigate("InicioSesion") },
            fontFamily = KiwiMaru,
            color = Redwood)
    }
}

/**
 * Introducir contraseña para crear un nuevo usuario
 * @param loginViewModel le pasamos el viewmodel para que ingrese y guarde la contraseña
 */
@Composable
fun IntroducirContrasenia(loginViewModel: LoginViewModel){
    var hidden by remember { mutableStateOf(true) }
    Text(text = "Contraseña", color = RaisanBlack, fontFamily = KiwiMaru)
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


/**
 * Boton de inicio de sesion
 * @param loginViewModel le pasamos el viewmodel del login
 * @param navController le pasamos el nav que nos lleva a donde queramos al darle a aceptar y
 * registrar un nuevo usuario en la base de datos
 */
@Composable
fun BotonAceptarInicio(loginViewModel: LoginViewModel, navController: NavController){
    Button(
        onClick = { loginViewModel.login { navController.navigate("Principal") } },
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



@Composable
fun ModificarPerfil(loginViewModel: LoginViewModel, navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    Scaffold(
        topBar = { Header(navController) },
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
            Text(text = loginViewModel.userName,
                color = RaisanBlack,
                style = TextStyle(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = loginViewModel.email)

            Spacer(modifier = Modifier.height(8.dp))


            Text(text = "Introduce la nueva contraseña: ", fontFamily = KiwiMaru)
            val password = loginViewModel.password //tengo que cambiarlo para que me la introduzca
            Text(text = "Introduce la nueva contraseña: ", fontFamily = KiwiMaru)
            if(loginViewModel.password == password){
                Log.d("Admin","Admin")
            }



        }
    }
}


/**
 * Inicio de sesion con google
 * @param loginViewModel viewmodel del login
 * @param navController navegacion
 */
@Composable
fun InicioSesionGoogle(loginViewModel: LoginViewModel, navController: NavController){
    val token = "802100001314-0168qhbg5joqhv56bgjpf7ib4qbdotv0.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()){//nos abre un activity para hacer login de google
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try{
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            loginViewModel.singInWithGoogleCredential(credential){navController.navigate("PaginaPrincipal")}
        }catch (e:Exception){
            Log.d("Excepcion","Excepcion + ${e.message}")
        }
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clip(RoundedCornerShape(10.dp))
        .clickable {
            val opciones = GoogleSignInOptions
                .Builder(
                    GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
                )
                .requestIdToken(token)
                .requestEmail()
                .build()
            val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
            launcher.launch(googleSingInCliente.signInIntent)

        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){

        Image(painter = painterResource(id = com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark),
            contentDescription = "Google",
            modifier = Modifier
                .padding(10.dp)
                .size(40.dp))
        Text(text = "Login Google", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}


/**
 * Escoge una imagen como foto de perfil
 */
@Composable
fun SinglePhotoPicker(){
    var uri by remember{
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )

    val context = LocalContext.current


    Column{
        Button(onClick = {
            singlePhotoPicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )

        }){
            Text("Pick Single Image")
        }

        AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(248.dp))

        Button(onClick = {
            uri?.let{
                Storage.uploadToStorage(uri=it, context=context, type="image")
            }

        }){
            Text("Upload")
        }

    }
}



