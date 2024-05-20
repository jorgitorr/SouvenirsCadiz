package com.example.souvenirscadiz.ui.view

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.KiwiMaru
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood





/**
 * Boton de favoritos con los souvenirs de la base de datos (Cajas de favoritos)
 * @param souvenir souvenirState que es de la base de datos (Favorito)
 * @param souvenirsViewModel viewmodel del souvenir
 */
@Composable
fun FavoriteButton(
    souvenir: Souvenir,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current
    val soundEffect = MediaPlayer.create(context, R.raw.like_sound)

    LaunchedEffect(souvenir.guardadoFav){
        souvenirsViewModel.fetchSouvenirsFav()
    }

    IconToggleButton(
        checked = souvenir.guardadoFav,
        onCheckedChange = {
            souvenir.guardadoFav = !souvenir.guardadoFav
        }
    ) {
        Icon(
            tint = if (!souvenir.guardadoFav) RaisanBlack else Redwood,
            imageVector = if (souvenir.guardadoFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    if (!souvenir.guardadoFav) {
                        souvenir.guardadoFav = true
                        souvenirsViewModel.saveSouvenirInFav({
                            Toast
                                .makeText(context, "Souvenir guardado en favoritos", Toast.LENGTH_SHORT)
                                .show()
                        }, souvenir)
                    } else {
                        souvenir.guardadoFav = false
                        souvenirsViewModel.deleteSouvenirInFav({
                            Toast
                                .makeText(context, "Souvenir eliminado de favoritos", Toast.LENGTH_SHORT)
                                .show()
                        }, souvenir)
                    }
                    //efecto de sonido
                    soundEffect.start()
                }
        )
    }

}



/**
 * icono del carrito en cada caja recogieda de la base de datos (Favorito)
 * @param souvenir souvenir actual de la base de datos (Favorito)
 * @param souvenirsViewModel viewmodel
 */
@Composable
fun ShopingCartButton(
    souvenir: Souvenir,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current

    IconToggleButton(
        checked = souvenir.guardadoCarrito,
        onCheckedChange = {
            souvenir.guardadoCarrito = !souvenir.guardadoCarrito
        },
        modifier = Modifier.padding(top = 280.dp, end = 340.dp) //posicion del icono
    ) {
        Icon(
            //si el elemento esta guardado en el carrito, el icono cambia a eliminar del guardado
            imageVector = if(!souvenir.guardadoCarrito) Icons.Default.AddShoppingCart else Icons.Default.RemoveShoppingCart,
            tint = if (!souvenir.guardadoCarrito) RaisanBlack else Redwood,
            contentDescription = "Cesta de la compra",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    if (!souvenir.guardadoCarrito) {
                        souvenir.guardadoCarrito = true
                        souvenirsViewModel.saveSouvenirInCarrito({
                            Toast
                                .makeText(
                                    context,
                                    "Souvenir guardado en carrito",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }, souvenir)
                    } else {
                        souvenir.guardadoCarrito = false
                        souvenirsViewModel.deleteSouvenirInCarrito({
                            Toast
                                .makeText(
                                    context,
                                    "Souvenir guardado en carrito",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }, souvenir)
                    }
                }

        )
    }

}


/**
 * Boton de eliminar (Carrito)
 * @param souvenirsViewModel viewmodel de souvenir
 * @param souvenir souvenirState recogido de la base de datos (Carrito)
 */
@Composable
fun EliminarButton(souvenirsViewModel: SouvenirsViewModel, souvenir: Souvenir) {
    val context = LocalContext.current

    IconToggleButton(
        checked = souvenir.guardadoCarrito,
        onCheckedChange = {
            souvenir.guardadoCarrito = !souvenir.guardadoCarrito
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = Redwood,
            modifier = Modifier.clickable {
                souvenir.guardadoCarrito = false
                souvenirsViewModel.deleteSouvenirInCarrito(
                    {
                        Toast.makeText(context,
                            "Has eliminado un souvenir del carrito",
                            Toast.LENGTH_SHORT).show()
                    }, souvenir)
            }
        )
    }

}


/**
 * Muestra el botón de pedir en el caso de que se pueda mostrar o el mensaje de alerta
 * @param souvenirsViewModel viewmodel de souvenirs
 * @param loginViewModel viewmodel del login
 * @param navController navegacion
 */
@Composable
fun ButtonPedirOrMsg(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, navController: NavController){
    val souvenirCarrito by souvenirsViewModel.souvenirCarrito.collectAsState()
    val context = LocalContext.current
    val soundEffect = MediaPlayer.create(context, R.raw.pedido_sound)

    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

    //si no hay souvenirs en el carrito
    if(souvenirCarrito.isNotEmpty()){
        Button(onClick = {
            souvenirsViewModel.saveSouvenirInPedido {
                Toast.makeText(context,
                    "Souvenirs Pedidos",
                    Toast.LENGTH_SHORT).show()
            }
            souvenirsViewModel.deleteSouvenirInCarritoFromUser()
            souvenirsViewModel.vaciarSouvenirsCarrito()

            soundEffect.start()
        },

            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Redwood)) {
            Text(text = "PEDIR",
                fontFamily = KiwiMaru
            )
        }
    }else{
        if (loginViewModel.showAlert) {
            // Mostrar un diálogo si el usuario no ha iniciado sesión
            AlertDialog(
                onDismissRequest = { /* No hacer nada en el cierre del diálogo */ },
                title = {
                    Text(
                        text = "No has iniciado sesión para ver tus elementos en el carrito",
                        fontFamily = KiwiMaru,
                        color = RaisanBlack,
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            navController.navigate("InicioSesion")
                        }
                    ) {
                        Text(
                            text = "Ir a Inicio de Sesión",
                            fontFamily = KiwiMaru,
                            color = Redwood
                        )
                    }
                }
            )
        }
    }
}


