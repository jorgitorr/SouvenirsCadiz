package com.example.souvenirscadiz.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.souvenirscadiz.data.util.Constant.Companion.IS_CURRENT_USER
import com.example.souvenirscadiz.data.util.Constant.Companion.MESSAGE
import com.example.souvenirscadiz.ui.model.ChatViewModel
import com.example.souvenirscadiz.ui.model.LoginViewModel
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.example.souvenirscadiz.ui.theme.RaisanBlack
import com.example.souvenirscadiz.ui.theme.Silver

@Composable
fun Chat(souvenirsViewModel: SouvenirsViewModel, navController: NavController, loginViewModel: LoginViewModel,
         chatViewModel: ChatViewModel){
    LaunchedEffect(true){
        souvenirsViewModel.fetchSouvenirsFav() //devuelve los souvenirs guardados en fav
        souvenirsViewModel.fetchSouvenirsCarrito() //devuelve los souvenirs guardados en carritos
    }

    Scaffold(
        topBar = {
            if(loginViewModel.checkAdmin()){
                HeaderAdmin(navController, souvenirsViewModel) //tipo de header del administrador de la BDD
            }else{
                Header(navController, souvenirsViewModel)
            }
        },
        bottomBar = {
            if(loginViewModel.checkAdmin()){
                FooterAdmin(navController, souvenirsViewModel, loginViewModel)
            }else{
                Footer(navController, souvenirsViewModel, loginViewModel)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Silver),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            //HomeView(chatViewModel)
            ChatAdmin(chatViewModel, loginViewModel)

        }
    }
}


@Composable
fun HomeView(chatViewModel: ChatViewModel) {
    var userMessages by remember { mutableStateOf("") }
    val message: String by chatViewModel.message.collectAsState(initial = "")
    val messages: List<Map<String, Any>> by chatViewModel.messages.collectAsState(initial = emptyList<Map<String, Any>>().toMutableList())
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.85f, fill = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                val isCurrentUser = message[IS_CURRENT_USER] as Boolean
                userMessages = message["message"].toString()
                
                SingleMessage(
                    message = message[MESSAGE].toString(),
                    isCurrentUser = isCurrentUser
                )
            }
        }
        
        Text(text = userMessages,
            color = RaisanBlack,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )

        OutlinedTextField(
            value = message,
            onValueChange = {
                chatViewModel.updateMessage(it)
            },
            label = {
                Text(
                    "Type Your Message"
                )
            },
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 1.dp)
                .fillMaxWidth()
                .weight(weight = 0.09f, fill = true),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        chatViewModel.addMessage{
                            Toast.makeText(context, "ERROR, NO HAS INICIADO SESION", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Button"
                    )
                }
            }
        )
    }
}


@Composable
fun SingleMessage(message: String, isCurrentUser: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = message,
            textAlign =
            if (isCurrentUser)
                TextAlign.End
            else
                TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = if (!isCurrentUser) Silver else Color.White
        )
    }
}


@Composable
fun ChatAdmin(chatViewModel: ChatViewModel, loginViewModel: LoginViewModel) {
    val users by loginViewModel.users.collectAsState()
    val selectedUser = remember { mutableStateOf(users.firstOrNull()) }

    LaunchedEffect(true){
        loginViewModel.fetchUsers()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // User list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(users) { user ->
                Text(
                    text = user.email,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedUser.value = user }
                        .padding(16.dp),
                    style = TextStyle(fontWeight = if (selectedUser.value == user) FontWeight.Bold else FontWeight.Normal)
                )
            }
        }
    }
}


