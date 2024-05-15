package com.example.souvenirscadiz.ui.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import coil.Coil
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.souvenirscadiz.data.model.PedidoState
import com.example.souvenirscadiz.data.model.SouvenirState
import com.example.souvenirscadiz.data.model.UserState
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver

/**
 * Caja con State
 * para que me permita introducir un State
 * @param navController navegacion
 * @param souvenir contiene souvenirState que es el objeto de la base de datos
 * @param url contiene el numero de la url
 */
@Composable
fun Caja(navController: NavController, souvenir: SouvenirState, url:Int, souvenirsViewModel: SouvenirsViewModel){
    LaunchedEffect(souvenir.guardadoFav){
        souvenirsViewModel.fetchSouvenirsFav()
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Silver, shape = RoundedCornerShape(5.dp))
        .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.TopEnd){
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(url)
                        .build(),
                    contentDescription = souvenir.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }

                )
                FavoriteButton(souvenir, souvenirsViewModel)
                ShopingCartButton(souvenir, souvenirsViewModel)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = souvenir.nombre,
                    style = TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                    color = RaisanBlack
                )
                Text(
                    text = souvenir.referencia,
                    style = TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                    color = RaisanBlack
                )
                Text(
                    text = "${souvenir.precio}€",
                    style = TextStyle(fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 3.dp, end = 4.dp),
                    color = RaisanBlack
                )
            }
        }
    }

}


/**
 * Caja que contiene cada imagen del souvenir y su nombre, referencia y precio
 * @param navController navegacion
 * @param souvenir clase souvenir
 */
@Composable
fun Caja(navController: NavController, souvenir: SouvenirState, souvenirsViewModel: SouvenirsViewModel,
         loginViewModel: LoginViewModel){

    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirs()
        souvenirsViewModel.fetchSouvenirsFav()
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

    val onChangeFav = souvenirsViewModel.onChangeFav.collectAsState()
    val onChangeCarrito = souvenirsViewModel.onChangeCarrito.collectAsState()

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = Silver, shape = RoundedCornerShape(5.dp))
        .border(width = 1.dp, color = RaisanBlack, shape = RoundedCornerShape(5.dp))){

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()) {

            Box(contentAlignment = Alignment.TopEnd){
                /*Image(
                    painter = painterResource(id = url),
                    contentDescription = souvenir.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(345.dp)
                        .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }
                )*/

                Log.d("souvenir_url",souvenir.url)
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(souvenir.url)
                    .build()
                    , contentDescription = souvenir.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(345.dp)
                        .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }
                )


                if(!loginViewModel.checkAdmin()){
                    if(onChangeFav.value){
                        FavoriteButton(souvenir, souvenirsViewModel)
                    }else{
                        FavoriteButton(souvenir, souvenirsViewModel)
                    }

                    if(onChangeCarrito.value){
                        ShopingCartButton(souvenir, souvenirsViewModel)
                    }else{
                        ShopingCartButton(souvenir, souvenirsViewModel)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Ajustamos el modificador del Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Quitamos el modificador de peso del Column
                Row {
                    Text(
                        text = souvenir.nombre,
                        style = TextStyle(fontSize = 15.sp),
                        fontFamily = KiwiMaru,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = RaisanBlack,
                        modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                    )

                    Text(
                        text = souvenir.referencia,
                        style = TextStyle(fontSize = 15.sp),
                        fontFamily = KiwiMaru,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = RaisanBlack,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .padding(start = 4.dp, end = 8.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }
}


/**
 * Caja del carrito
 * @param navController navegacion
 * @param souvenir souvenirState
 * @param url url del souvenir
 * @param souvenirsViewModel viewmodel del souvenir
 */
@Composable
fun CajaCarrito(
    navController: NavController,
    souvenir: SouvenirState,
    url: Int,
    souvenirsViewModel: SouvenirsViewModel
) {

    LaunchedEffect(souvenir.guardadoCarrito){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }
    val context = LocalContext.current
    var cantidadSouvenir by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Silver, shape = RoundedCornerShape(5.dp))
            .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))
            .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(contentAlignment = Alignment.TopEnd){
                // Imagen
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(url)
                        .build(),
                    contentDescription = souvenir.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }

                )
                //boton de eliminar souvenir del carrito
                EliminarButton(souvenirsViewModel, souvenir)

            }
            Spacer(modifier = Modifier.height(8.dp))
            // Detalles del souvenir
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = souvenir.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Ref: ${souvenir.referencia}",
                    fontSize = 14.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${souvenir.precio}€",
                    fontSize = 14.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                OutlinedTextField(value = cantidadSouvenir,
                    label = { Text(text = "Cantidad a pedir ${souvenir.cantidad}")},
                    onValueChange = {
                        if (it.isDigitsOnly()) {
                            cantidadSouvenir = it
                            souvenir.cantidad = cantidadSouvenir
                        }else{
                            Toast.makeText(context,"Has introducido un campo erroneo", Toast.LENGTH_SHORT).show()
                        }},
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

        }
    }
}



/**
 * Caja del carrito
 * @param navController navegacion
 * @param pedido souvenirState
 */
@Composable
fun CajaPedido(
    navController: NavController,
    pedido: PedidoState,
    souvenirsViewModel: SouvenirsViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Silver, shape = RoundedCornerShape(5.dp))
            .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))
            .clickable { navController.navigate("SouvenirDetail/${pedido.referencia}") }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pedido.emailUser,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = pedido.nombre,
                    fontSize = 16.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Ref: ${pedido.referencia}",
                    fontSize = 14.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Cantidad: ${pedido.cantidad}",
                    fontSize = 14.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row {
                    AcceptButton(pedido, souvenirsViewModel)
                    CancelButton(pedido, souvenirsViewModel)
                }
            }
        }
    }
}

@Composable
fun CajaUsuarios(user:UserState,
                 loginViewModel: LoginViewModel,
                 souvenirsViewModel: SouvenirsViewModel,
                 navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Silver, shape = RoundedCornerShape(5.dp))
            .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))
            .clickable {
                navController.navigate(
                    "UsuarioDetail/"
                )
            }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = user.username,
                    fontSize = 14.sp,
                    color = RaisanBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                EliminarButton(
                    user,
                    souvenirsViewModel)
            }
        }
    }
}


