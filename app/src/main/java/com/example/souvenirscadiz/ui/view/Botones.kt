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
import androidx.compose.material.icons.filled.ModeEdit
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
 * Favorite button
 *
 * @param souvenir
 * @param souvenirsViewModel
 */
@Composable
fun FavoriteButton(
    souvenir: Souvenir,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current
    val soundEffect = MediaPlayer.create(context, R.raw.like_sound)
    var isFavorite by remember { mutableStateOf(souvenir.favorito) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            souvenir.favorito = isFavorite
            if (isFavorite) {
                souvenirsViewModel.saveSouvenirInFav({
                    Toast
                        .makeText(context, "Souvenir guardado en favoritos", Toast.LENGTH_SHORT)
                        .show()
                }, souvenir)
            } else {
                souvenirsViewModel.deleteSouvenirInFav({
                    Toast
                        .makeText(context, "Souvenir eliminado de favoritos", Toast.LENGTH_SHORT)
                        .show()
                }, souvenir)
            }
            // efecto de sonido
            soundEffect.start()
            souvenirsViewModel.fetchSouvenirsFav()
        }
    ) {
        Icon(
            tint = if (!isFavorite) RaisanBlack else Redwood,
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite Icon",
            modifier = Modifier.size(30.dp)
        )
    }

}


/**
 * Shoping cart button
 *
 * @param souvenir
 * @param souvenirsViewModel
 */
@Composable
fun ShopingCartButton(
    souvenir: Souvenir,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current
    var isCarrito by remember { mutableStateOf(souvenir.carrito) }

    IconToggleButton(
        checked = isCarrito,
        onCheckedChange = {
            isCarrito = !isCarrito
            souvenir.carrito = isCarrito
            if (isCarrito) {
                souvenirsViewModel.saveSouvenirInCarrito({
                    Toast.makeText(
                        context,
                        "Souvenir guardado en carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }, souvenir)
            } else {
                souvenirsViewModel.deleteSouvenirInCarrito({
                    Toast.makeText(
                        context,
                        "Souvenir eliminado de carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }, souvenir)
            }

            souvenirsViewModel.fetchSouvenirsCarrito()
        },
        modifier = Modifier.padding(top = 280.dp, end = 340.dp) // Posiciona el icono
    ) {
        Icon(
            imageVector = if (!isCarrito) Icons.Default.AddShoppingCart else Icons.Default.RemoveShoppingCart,
            tint = if (!isCarrito) RaisanBlack else Redwood,
            contentDescription = "Cesta de la compra",
            modifier = Modifier.size(30.dp)
        )
    }
}


/**
 * Eliminar button
 *
 * @param souvenirsViewModel
 * @param souvenir
 */
@Composable
fun EliminarButton(souvenirsViewModel: SouvenirsViewModel, souvenir: Souvenir) {
    val context = LocalContext.current

    IconToggleButton(
        checked = souvenir.carrito,
        onCheckedChange = {
            souvenir.carrito = !souvenir.carrito
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = Redwood,
            modifier = Modifier.clickable {
                souvenir.carrito = false
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
 * Button pedir or msg
 *
 * @param souvenirsViewModel
 * @param loginViewModel
 * @param navController
 */
@Composable
fun ButtonPedirOrMsg(souvenirsViewModel: SouvenirsViewModel, loginViewModel: LoginViewModel, navController: NavController){
    val souvenirCarrito by souvenirsViewModel.souvenirCarrito.collectAsState()
    val context = LocalContext.current
    val soundEffect = MediaPlayer.create(context, R.raw.pedido_sound)

    //si no hay souvenirs en el carrito
    if(souvenirCarrito.isNotEmpty()){
        Button(onClick = {
            souvenirsViewModel.saveSouvenirInPedido{
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


@Composable
fun ModifyButton(souvenir: Souvenir, navController: NavController) {
    var onModify by remember { mutableStateOf(souvenir.favorito) }
    
    IconToggleButton(
        checked = onModify,
        onCheckedChange = {
            onModify = !onModify
            navController.navigate("ModificarSouvenir/${souvenir.referencia}")
        }
    ) {
        Icon(
            tint = if (!onModify) RaisanBlack else Redwood,
            imageVector = if (onModify) Icons.Default.ModeEdit else Icons.Default.ModeEdit,
            contentDescription = "Favorite Icon",
            modifier = Modifier.size(30.dp)
        )
    }
}


