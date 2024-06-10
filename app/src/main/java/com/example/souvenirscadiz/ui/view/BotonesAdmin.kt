package com.example.souvenirscadiz.ui.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import com.example.souvenirscadiz.data.model.Pedido
import com.example.souvenirscadiz.data.model.User
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood

/**
 * Cancel button
 *
 * @param pedido
 * @param souvenirsViewModel
 */
@Composable
fun CancelButton(
    pedido: Pedido,
    souvenirsViewModel: SouvenirsViewModel
) {

    LaunchedEffect(pedido.pedidoCancelado){
        souvenirsViewModel.fetchSouvenirsPedido()
    }

    IconToggleButton(
        checked = pedido.pedidoCancelado,
        onCheckedChange = {
            pedido.pedidoCancelado = !pedido.pedidoCancelado
        }
    ) {
        Icon(
            tint = if (!pedido.pedidoCancelado) RaisanBlack else Redwood,
            imageVector = if (!pedido.pedidoCancelado) Icons.Outlined.Cancel else Icons.Default.Cancel,
            contentDescription = "Cancel Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    pedido.pedidoCancelado = !pedido.pedidoCancelado
                }
        )
    }

}

/**
 * Accept button
 *
 * @param pedido
 * @param souvenirsViewModel
 */
@Composable
fun AcceptButton(
    pedido: Pedido,
    souvenirsViewModel: SouvenirsViewModel
) {

    LaunchedEffect(pedido.pedidoAceptado){
        souvenirsViewModel.fetchSouvenirsPedido()
    }

    IconToggleButton(
        checked = pedido.pedidoAceptado,
        onCheckedChange = {
            pedido.pedidoAceptado = !pedido.pedidoAceptado
        }
    ) {
        Icon(
            tint = if (!pedido.pedidoAceptado) RaisanBlack else Redwood,
            imageVector = if (!pedido.pedidoAceptado) Icons.Outlined.AddCircle else Icons.Default.AddCircle,
            contentDescription = "Cancel Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    pedido.pedidoAceptado = !pedido.pedidoAceptado
                }
        )
    }

}


/**
 * Eliminar button
 *
 * @param userState
 */
@Composable
fun EliminarUser(
    userState: User,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current

    IconToggleButton(
        checked = userState.eliminado,
        onCheckedChange = {
            userState.eliminado != userState.eliminado
        }
    ) {
        Icon(
            tint = if (!userState.eliminado) RaisanBlack else Redwood,
            imageVector = if (!userState.eliminado) Icons.Outlined.Cancel else Icons.Default.Cancel,
            contentDescription = "Eliminado Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    userState.eliminado != userState.eliminado
                    loginViewModel.deleteUser({
                        Toast.makeText(
                            context,
                            "Usuario eliminado",
                            Toast.LENGTH_LONG
                        ).show() },userState)
                }
        )
    }

}


/**
 * Eliminar pedido
 *
 * @param pedido
 */
@Composable
fun EliminarPedido(souvenirsViewModel: SouvenirsViewModel, pedido: Pedido, ){
    val context = LocalContext.current
    IconToggleButton(
        checked = pedido.pedidoCancelado,
        onCheckedChange = {
            pedido.pedidoCancelado = !pedido.pedidoCancelado
        }
    ) {
        Log.d("PedidoCancelado",pedido.pedidoCancelado.toString())
        Icon(
            tint = if (!pedido.pedidoCancelado) RaisanBlack else Redwood,
            imageVector = if (!pedido.pedidoCancelado) Icons.Outlined.Cancel else Icons.Default.Cancel,
            contentDescription = "Eliminado Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    pedido.pedidoCancelado = !pedido.pedidoCancelado
                    Log.d("pedidoCancelado",pedido.pedidoCancelado.toString())
                    if(pedido.pedidoCancelado){
                        souvenirsViewModel.deletePedido ({
                            Toast.makeText(
                                context,
                                "Pedido Cancelado",
                                Toast.LENGTH_LONG
                            ).show()
                        }, pedido)
                    }
                }
        )
    }
}


/**
 * Aceptar pedido
 *
 * @param pedido
 */
@Composable
fun AceptarPedido(souvenirsViewModel: SouvenirsViewModel, pedido: Pedido){
    val context = LocalContext.current

    IconToggleButton(
        checked = pedido.pedidoAceptado,
        onCheckedChange = {
            pedido.pedidoAceptado = !pedido.pedidoAceptado
        }
    ) {
        Icon(
            tint = if (!pedido.pedidoAceptado) RaisanBlack else Redwood,
            imageVector = if (!pedido.pedidoAceptado) Icons.Outlined.AddTask else Icons.Default.AddTask,
            contentDescription = "Eliminado Icon",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    pedido.pedidoAceptado = !pedido.pedidoAceptado
                    Log.d("PedidoAceptado",pedido.pedidoAceptado.toString())
                    souvenirsViewModel.saveSouvenirInHistorial({},pedido)
                    if(pedido.pedidoAceptado){
                        souvenirsViewModel.deletePedido( {
                            Toast.makeText(
                                context,
                                "Pedido Completado",
                                Toast.LENGTH_LONG
                            ).show()
                        }, pedido)
                    }
                }
        )
    }
}
