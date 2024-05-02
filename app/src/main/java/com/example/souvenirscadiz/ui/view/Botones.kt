package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.SouvenirState
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood

/**
 * Componente para el botón de favoritos
 * @param souvenir
 * @param souvenirsViewModel viewmodel de los souvenirs para acceder al metodo que guarda souvenirs en la BDD
 */
@Composable
fun FavoriteButton(
    souvenir: Souvenir,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current

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
                    if(!souvenir.guardadoFav){
                        //si el souvenir no esta guardado
                        souvenirsViewModel.saveSouvenirInFav({
                            Toast
                                .makeText(context, "Souvenir guardado", Toast.LENGTH_SHORT)
                                .show()
                        },souvenir)
                    }else{
                        souvenirsViewModel.deleteSouvenirInFav ({
                            Toast
                                .makeText(context, "Souvenir eliminado", Toast.LENGTH_SHORT)
                                .show()
                        },souvenir)
                    }

                    souvenir.guardadoFav = !souvenir.guardadoFav
                }
        )
    }

}


@Composable
fun FavoriteButton(
    souvenir: SouvenirState,
    souvenirsViewModel: SouvenirsViewModel
) {
    val context = LocalContext.current

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
                    if(!souvenir.guardadoFav){
                        //si el souvenir no esta guardado
                        souvenirsViewModel.saveSouvenirInFav({
                            Toast
                                .makeText(context, "Souvenir guardado", Toast.LENGTH_SHORT)
                                .show()
                        },souvenir)
                    }else{
                        souvenirsViewModel.deleteSouvenirInFav ({
                            Toast
                                .makeText(context, "Souvenir eliminado", Toast.LENGTH_SHORT)
                                .show()
                        },souvenir)
                    }

                    souvenir.guardadoFav = !souvenir.guardadoFav
                }
        )
    }

}





/**
 * icono del carrito en cada caja
 * @param souvenir souvenir actual
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
                    if(!souvenir.guardadoCarrito){
                        souvenirsViewModel.saveSouvenirInCarrito ({
                            Toast
                                .makeText(
                                    context,
                                    "Souvenir guardado en carrito",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },souvenir)
                    }else{
                        souvenirsViewModel.deleteSouvenirInCarrito({
                            Toast
                                .makeText(
                                    context,
                                    "Souvenir guardado en carrito",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },souvenir)
                    }

                    souvenir.guardadoCarrito = !souvenir.guardadoCarrito

                }

        )
    }

}


@Composable
fun ShopingCartButton(
    souvenir: SouvenirState,
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
                    if(!souvenir.guardadoCarrito){
                        souvenirsViewModel.saveSouvenirInCarrito ({
                            Toast
                                .makeText(
                                    context,
                                    "Souvenir guardado en carrito",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },souvenir)
                    }else{
                        souvenirsViewModel.deleteSouvenirInCarrito({
                            Toast
                                .makeText(
                                    context,
                                    "Souvenir guardado en carrito",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },souvenir)
                    }

                    souvenir.guardadoCarrito = !souvenir.guardadoCarrito

                }

        )
    }

}

@Composable
fun EliminarButton(souvenirsViewModel: SouvenirsViewModel, souvenir: SouvenirState) {
    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsCarrito()
    }

    var eliminar = false
    val context = LocalContext.current

    IconToggleButton(
        checked = eliminar,
        onCheckedChange = {
            eliminar = !eliminar
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = Redwood,
            modifier = Modifier.clickable {
                souvenirsViewModel.deleteSouvenirInCarrito(
                    {
                        Toast.makeText(context,
                            "Has eliminado un souvenir del carrito",
                            Toast.LENGTH_SHORT).show()
                    }, souvenir
                )

                souvenir.guardadoCarrito = false
            }
        )
    }

}