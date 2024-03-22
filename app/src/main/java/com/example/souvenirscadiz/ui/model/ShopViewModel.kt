package com.example.souvenirscadiz.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.Souvenir
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ShopViewModel : ViewModel(){
    /**
     * @param _souvenirsAniadidos son los souvenirs que se añaden a la lista de compra
     */
    private val _souvenirsAniadidos = MutableStateFlow<List<Souvenir>>(emptyList())
    val souvenirsAniadidos = _souvenirsAniadidos
    val list: MutableList<Souvenir> = mutableListOf()

    fun addSouvenir(souvenir: Souvenir){
        viewModelScope.launch {
            list.add(souvenir)
            _souvenirsAniadidos.value = list
        }
    }
}