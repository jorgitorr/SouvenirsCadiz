package com.example.souvenirscadiz.ui.view

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.souvenirscadiz.data.model.Pedido
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.User
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver


/**
 * Caja que contiene cada imagen del souvenir y su nombre, referencia y precio
 * Caja para la pagina principal y para elementos en favoritos
 * @param navController navegacion
 * @param souvenir clase souvenir
 */
@Composable
fun Caja(navController: NavController, souvenir: Souvenir, souvenirsViewModel: SouvenirsViewModel,
         loginViewModel: LoginViewModel){

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
                SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(souvenir.url)
                    .build(),
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentDescription = souvenir.nombre,
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
    souvenir: Souvenir,
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
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(souvenir.url)
                        .build(),
                    loading = { CircularProgressIndicator()},
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
 * @param pedido pedido
 * @param souvenirsViewModel viewmodel de souvenir
 */
@Composable
fun CajaPedido(
    navController: NavController,
    pedido: Pedido
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Silver, shape = RoundedCornerShape(5.dp))
            .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))
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

                for(souvenir in pedido.souvenirs){
                    Box(contentAlignment = Alignment.TopEnd){
                        // Imagen
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(souvenir.url)
                                .build(),
                            loading = { CircularProgressIndicator()},
                            contentDescription = souvenir.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("SouvenirDetail/${souvenir.referencia}") }

                        )
                    }

                    Text(
                        text = souvenir.referencia,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RaisanBlack,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = souvenir.nombre,
                        fontSize = 20.sp,
                        color = RaisanBlack,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = souvenir.cantidad,
                        fontSize = 20.sp,
                        color = RaisanBlack,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = souvenir.precio,
                        fontSize = 20.sp,
                        color = RaisanBlack,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                }
            }
        }
    }
}

@Composable
fun CajaUsuarios(user:User,
                 loginViewModel: LoginViewModel,
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
                    loginViewModel)
            }
        }
    }
}


