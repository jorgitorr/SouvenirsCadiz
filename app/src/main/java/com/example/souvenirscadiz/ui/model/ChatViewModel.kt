package com.example.souvenirscadiz.ui.model

import androidx.lifecycle.ViewModel
import com.example.souvenirscadiz.data.model.User
import com.example.souvenirscadiz.data.util.Constant.Companion.IS_CURRENT_USER
import com.example.souvenirscadiz.data.util.Constant.Companion.MESSAGE
import com.example.souvenirscadiz.data.util.Constant.Companion.MESSAGES
import com.example.souvenirscadiz.data.util.Constant.Companion.SENT_BY
import com.example.souvenirscadiz.data.util.Constant.Companion.SENT_ON
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel() : ViewModel() {
    /**
     * @param _message
     * @param message
     * @param _messages
     * @param messages
     */
    private val _message = MutableStateFlow("")
    val message  = _message

    private var _messages = MutableStateFlow(emptyList<Map<String, Any>>())
    val messages = _messages


    init {
        getMessages()
        loadMessages()
    }

    /**
     * Update the message value as user types
     */
    fun updateMessage(message: String) {
        _message.value = message
    }

    /**
     * Send message
     */
    fun addMessage(onSuccess: () -> Unit) {
        val message: String = _message.value ?: throw IllegalArgumentException("message empty")
        if (message.isNotEmpty()) {
            Firebase.firestore.collection(MESSAGES).document().set(
                hashMapOf(
                    MESSAGE to message,
                    SENT_BY to Firebase.auth.currentUser?.uid,
                    SENT_ON to System.currentTimeMillis()
                )
            ).addOnSuccessListener {
                _message.value = ""
            }.addOnFailureListener {
                onSuccess()
            }
        }
    }

    /**
     * Get the messages
     */
    private fun getMessages() {
        Firebase.firestore.collection(MESSAGES)
            .orderBy(SENT_ON)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val list = emptyList<Map<String, Any>>().toMutableList()

                if (value != null) {
                    for (doc in value) {
                        val data = doc.data
                        data[IS_CURRENT_USER] =
                            Firebase.auth.currentUser?.uid.toString() == data[SENT_BY].toString()

                        list.add(data)
                    }
                }

                updateMessages(list)
            }
    }

    /**
     * Update the list after getting the details from firestore
     */
    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }


    private fun loadMessages() {
        // Load messages from a data source
        _messages.value = listOf(
            mapOf("user" to "User1", "message" to "Hello", "isCurrentUser" to false),
            mapOf("user" to "User1", "message" to "Hi there", "isCurrentUser" to true),
            mapOf("user" to "User2", "message" to "How are you?", "isCurrentUser" to false)
        )
    }

    fun sendMessageToUser(user: String, message: String, onError: () -> Unit) {
        // Add message to the list
        _messages.value = _messages.value + mapOf("user" to user, "message" to message, "isCurrentUser" to true)
    }
}