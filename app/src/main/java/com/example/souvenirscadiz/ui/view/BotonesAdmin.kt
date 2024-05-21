package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.souvenirscadiz.data.model.Pedido
import com.example.souvenirscadiz.data.model.User
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Redwood

/**
 * Boton de cancelar
 * @param pedido state del pedido
 * @param souvenirsViewModel viewmodel de souvenir
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
 * Boton de aceptar
 * @param pedido pedido state
 * @param souvenirsViewModel viewmodel del souvenir
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


@Composable
fun EliminarButton(
    userState: User
) {

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
                }
        )
    }

}