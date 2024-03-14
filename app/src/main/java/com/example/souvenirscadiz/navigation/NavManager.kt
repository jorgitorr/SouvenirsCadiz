package com.example.souvenirscadiz.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.view.Favoritos
import com.example.souvenirscadiz.ui.view.InicioSesion
import com.example.souvenirscadiz.ui.view.Perfil
import com.example.souvenirscadiz.ui.view.Principal
import com.example.souvenirscadiz.ui.view.Registro
import com.example.souvenirscadiz.ui.view.SouvenirDetail
import com.example.souvenirscadiz.ui.view.Tienda

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavManager(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel){
    val navController = rememberNavController();
    
    NavHost(navController = navController, startDestination = "Principal"){
        composable("Principal"){
            Principal(souvenirsViewModel, navController)
        }
        composable("Favoritos"){
            Favoritos(souvenirsViewModel, navController)
        }
        composable("Perfil"){
            Perfil(loginViewModel, navController)
        }
        composable("Tienda"){
            Tienda(souvenirsViewModel, navController)
        }
        composable("Registro"){
            Registro(loginViewModel, navController)
        }
        composable("InicioSesion"){
            InicioSesion(loginViewModel, navController)
        }
        composable("SouvenirDetail/{referencia}", arguments = listOf(
            navArgument("referencia") { type = NavType.StringType }
        )  ){
            val referencia = it.arguments?.getString("referencia") ?: 0
            SouvenirDetail(navController, souvenirsViewModel, referencia.toString())
        }

    }
}