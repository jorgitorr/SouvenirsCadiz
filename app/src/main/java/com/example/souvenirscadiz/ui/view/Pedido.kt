package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.souvenirscadiz.data.model.PedidoState
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver

@Composable
fun Pedidos(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel){
    Scaffold(
        topBar = {
            HeaderAdmin(navController, souvenirsViewModel)
        },
        bottomBar = {
            Footer(navController,souvenirsViewModel, loginViewModel)
        }, containerColor = Silver
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            souvenirsViewModel.fetchSouvenirsPedido()
            SouvenirsPedido(navController, souvenirsViewModel)
        }
    }
}

/**
 * Muestra los souvenirs guardados en fav
 */
@Composable
fun SouvenirsPedido(navController: NavController, souvenirsViewModel: SouvenirsViewModel){
    val souvenirSaved by souvenirsViewModel.souvenirPedidos.collectAsState()//parametro que contiene los metodos guardados
    LazyRow{
        items(souvenirSaved){ souvenir ->
            CuadradoPedido(navController = navController,
                pedido = souvenir,
                url = souvenir.url,
                souvenirsViewModel = souvenirsViewModel)
        }
    }
}


@Composable
fun CuadradoPedido(navController: NavController, pedido: PedidoState, url:Int, souvenirsViewModel: SouvenirsViewModel){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Silver, shape = RoundedCornerShape(5.dp))
        .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(url)
                    .build(),
                contentDescription = pedido.nombre,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .clickable { navController.navigate("SouvenirDetail/${pedido.referencia}") }

            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = pedido.userMail,
                    style = TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Text(
                    text = pedido.nombre,
                    style = TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Text(
                    text = pedido.referencia,
                    style = TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Text(
                    text = "${pedido.cantidad}€",
                    style = TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 3.dp, end = 4.dp)
                )
            }
        }
    }

}
