package com.example.souvenirscadiz.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.souvenirscadiz.data.util.CloudStorageManager
import com.example.souvenirscadiz.ui.model.ChatViewModel
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.view.AdminPrincipal
import com.example.souvenirscadiz.ui.view.AnadirSouvenir
import com.example.souvenirscadiz.ui.view.DetallesLogo
import com.example.souvenirscadiz.ui.view.Favoritos
import com.example.souvenirscadiz.ui.view.InicioSesion
import com.example.souvenirscadiz.ui.view.ModificarPerfil
import com.example.souvenirscadiz.ui.view.Pedidos
import com.example.souvenirscadiz.ui.view.Perfil
import com.example.souvenirscadiz.ui.view.Principal
import com.example.souvenirscadiz.ui.view.Registro
import com.example.souvenirscadiz.ui.view.SouvenirDetail
import com.example.souvenirscadiz.ui.view.Carrito
import com.example.souvenirscadiz.ui.view.Chat
import com.example.souvenirscadiz.ui.view.ChatAdmin
import com.example.souvenirscadiz.ui.view.UsuarioDetail
import com.example.souvenirscadiz.ui.view.Usuarios

/**
 * navManager que permite la navegacion entre páginas
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param loginViewModel viewmodel del login
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavManager(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, cloudStorageManager:CloudStorageManager, chatViewModel:ChatViewModel){
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "Principal"){
        composable("Principal"){
            Principal(souvenirsViewModel, navController, loginViewModel)
        }
        composable("Favoritos"){
            Favoritos(souvenirsViewModel, navController, loginViewModel)
        }
        composable("Perfil"){
            Perfil(loginViewModel, navController, souvenirsViewModel, cloudStorageManager)
        }
        composable("Tienda"){
            Carrito(souvenirsViewModel, navController, loginViewModel)
        }
        composable("Registro"){
            Registro(souvenirsViewModel, loginViewModel, navController)
        }
        composable("InicioSesion"){
            InicioSesion(souvenirsViewModel, loginViewModel, navController)
        }
        composable("SouvenirDetail/{referencia}", arguments = listOf(
            navArgument("referencia") { type = NavType.StringType }
        )  ){
            val referencia = it.arguments?.getString("referencia") ?: 0
            SouvenirDetail(navController, souvenirsViewModel, loginViewModel, referencia.toString())
        }
        composable("ModificarPerfil"){
            ModificarPerfil(loginViewModel, navController, souvenirsViewModel)
        }
        composable("Detalles"){
            DetallesLogo(souvenirsViewModel, navController, loginViewModel)
        }
        composable("PrincipalAdmin"){
            AdminPrincipal(souvenirsViewModel, navController, loginViewModel)
        }
        composable("Pedidos"){
            Pedidos(souvenirsViewModel, navController, loginViewModel)
        }
        composable("Usuarios"){
            Usuarios(souvenirsViewModel, navController, loginViewModel)
        }
        composable("UsuarioDetail"){
            //no esta terminado
            UsuarioDetail(loginViewModel, souvenirsViewModel, navController)
        }
        composable("AnadirSouvenir"){
            AnadirSouvenir(loginViewModel, souvenirsViewModel, navController, cloudStorageManager)
        }
        composable("Chat"){
            Chat(souvenirsViewModel, navController, loginViewModel, chatViewModel)
        }
        composable("ChatAdmin"){
            ChatAdmin(chatViewModel, loginViewModel)
        }

    }
}