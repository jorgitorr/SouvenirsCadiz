package com.example.souvenirscadiz.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.view.PaginaPrincipal

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavManager(loginViewModel: LoginViewModel, souvenirsViewModel: SouvenirsViewModel){
    val navController = rememberNavController();
    
    NavHost(navController = navController, startDestination = "Principal"){
        composable("PantallaPrincipal"){
            //PaginaPrincipal(souvenirsViewModel, navController)
        }
    }
}