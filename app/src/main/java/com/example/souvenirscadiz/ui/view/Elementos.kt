package com.example.souvenirscadiz.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver
import java.time.format.TextStyle

@Composable
fun Cuadrado(){
    val souvenir = Souvenir()
    souvenir.nombre = "Llavero"
    souvenir.tipo = Tipo.LLAVERO
    souvenir.precio = 3
    souvenir.url = R.drawable.img4
    Box(modifier = Modifier
        .size(120.dp)
        .background(Silver, shape = RoundedCornerShape(5.dp))
        .border(1.dp, RaisanBlack, shape = RoundedCornerShape(5.dp))){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = souvenir.url),
                contentDescription = souvenir.nombre,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp)) // Espacio entre la imagen y el texto
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = souvenir.nombre,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )
                Text(
                    text = "${souvenir.precio}€",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 3.dp, end = 4.dp)
                )
            }
        }
    }

}


@Composable
fun Footer(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
        .background(Silver)){
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home Icon",
                    tint = RaisanBlack,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text("Home")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite Icon",
                    tint = RaisanBlack,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text("Favorites")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = RaisanBlack,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text("Profile")
            }
        }
    }
}


@Preview
@Composable
fun Header(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(Silver)){
        Row {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
            Text(text = "SOUVENIRS CADIZ")
        }
    }
}