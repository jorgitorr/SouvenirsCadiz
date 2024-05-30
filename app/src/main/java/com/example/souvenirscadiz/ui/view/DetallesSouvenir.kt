package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver

/**
 * Souvenir detail
 *
 * @param navController
 * @param souvenirsViewModel
 * @param loginViewModel
 * @param referencia referencia del souvenir para entrar en sus detalles
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
                        .padding(5.dp)
                        .border(1.dp, RaisanBlack)
                        .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }
                )

                if(!loginViewModel.checkAdmin()){
                    FavoriteButton(souvenir, souvenirsViewModel)
                    ShopingCartButton(souvenir, souvenirsViewModel)
                }else{
                    ModifyButton(souvenir, navController)
                }
            }

            LazyColumn(modifier = Modifier.fillMaxWidth().padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally){

                item {
                    //nombre
                    Box (modifier = Modifier
                        .fillMaxWidth()){
                        Text(
                            text = souvenir.nombre,
                            fontFamily = KiwiMaru,
                            fontSize = 20.sp,
                            color = RaisanBlack,
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    //referencia
                    Box(modifier = Modifier
                        .fillMaxWidth()){
                        Text(text = souvenir.referencia,
                            fontFamily = KiwiMaru,
                            fontSize = 20.sp,
                            color = RaisanBlack
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item{
                    //precio
                    Box(modifier = Modifier
                        .fillMaxWidth()){
                        Text(text = "${souvenir.precio}€",
                            fontFamily = KiwiMaru,
                            fontSize = 20.sp,
                            color = RaisanBlack
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                    Share(text = "Compartido con la app Souvenirs Cadiz\n" +
                            "${souvenir.nombre} \n" +
                            souvenir.referencia + "\n ",
                        context = LocalContext.current)
                }

            }
        }
    }
}
