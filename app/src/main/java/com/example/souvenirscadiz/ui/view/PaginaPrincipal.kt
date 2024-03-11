package com.example.souvenirscadiz.ui.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.souvenirscadiz.R
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.Tipo

@Preview
@Composable
fun PaginaPrincipal(){
    Scaffold(
        topBar = {
            Header()
        },
        bottomBar = {
            Footer()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            //Search(souvenirsViewModel = souvenirsViewModel)
            val souvenir = Souvenir()
            souvenir.nombre = "Llavero"
            souvenir.tipo = Tipo.LLAVERO
            souvenir.precio = 3
            souvenir.url = R.drawable.img6
            LazyColumn{
                item { Cuadrado(souvenir) }
                item { Cuadrado(souvenir) }
            }
        }
    }
}

fun imagenes(context: Context):List<Souvenir>{
    val listaSouvenirs = mutableListOf<Souvenir>()

    for (i in 6..18) {
        val souvenir = Souvenir()
        souvenir.nombre = "Imagen $i" // Puedes cambiar esto según tus necesidades
        souvenir.tipo = Tipo.LLAVERO // Puedes cambiar el tipo según tus necesidades
        souvenir.precio = 3 // Puedes cambiar el precio según tus necesidades
        souvenir.url = getIdDrawable(context, "img${i}" )
        listaSouvenirs.add(souvenir)
    }
    return listaSouvenirs
}

@SuppressLint("DiscouragedApi")
private fun getIdDrawable(context: Context, nombreCarta: String) =
    context.resources.getIdentifier(
        nombreCarta,
        "drawable",
        context.packageName)