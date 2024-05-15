package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.Silver

/**
 * Página que detalla información del souvenir
 * @param navController navegacion
 * @param souvenirsViewModel viewmodel de los souvenirs
 * @param referencia referencia del souvenir
 */
@Composable
fun SouvenirDetail(navController: NavController, souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, referencia:String) {
    Scaffold(
        topBar = {
            if(loginViewModel.checkAdmin())
                HeaderAdmin(
                navController,
                souvenirsViewModel)
            else
                Header(
                    navController,
                    souvenirsViewModel) },
        bottomBar = {
            if(loginViewModel.checkAdmin())
                FooterAdmin(
                    navController,
                    souvenirsViewModel,
                    loginViewModel)
            else
                Footer(navController,
                    souvenirsViewModel,
                    loginViewModel) }
        , containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.Center
        ) {
            //devuelve el souvenir por su referencia
            val souvenir = souvenirsViewModel.getByReference(referencia)

            Box(contentAlignment = Alignment.TopEnd){

                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(souvenir.url)
                    .build()
                    , contentDescription = souvenir.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(345.dp)
                        .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }
                )

                FavoriteButton(souvenir, souvenirsViewModel)
                ShopingCartButton(souvenir, souvenirsViewModel)
            }

            LazyColumn(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){

                item {
                    //nombre
                    Text(
                        text = souvenir.nombre,
                        fontFamily = KiwiMaru
                    )
                }

                item {
                    //referencia
                    Text(text = souvenir.referencia,
                        fontFamily = KiwiMaru
                    )
                }

                item{
                    //precio
                    Text(text = "${souvenir.precio}€",
                        fontFamily = KiwiMaru
                    )
                }

            }
        }
    }
}