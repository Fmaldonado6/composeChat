package com.dscuanl.composechat.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dscuanl.composechat.data.models.Message
import com.dscuanl.composechat.data.models.User
import com.dscuanl.composechat.ui.theme.ComposeChatTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun HomeScreen(
    vm: HomeViewModel,
    navController: NavController
) {
    val state by vm.uiState.collectAsState()
    val messages by vm.messages.collectAsState(initial = mutableListOf())
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Chat", style = TextStyle(fontSize = 35.sp))
                },
                modifier = Modifier.height(80.dp),
                elevation = 0.dp,

                backgroundColor = MaterialTheme.colors.background
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (state) {
                is HomeUiState.Loaded -> HomeLoaded(
                    messages = messages,
                    vm.messageState,
                    onMessageTyped = { value ->
                        vm.onMessageTyped(value = value)
                    },
                    onButtonClicked = {
                        vm.sendMessage()
                    }
                )
                is HomeUiState.Loading -> HomeLoading()
                is HomeUiState.Empty -> Text("No users")
                is HomeUiState.Error -> Button(onClick = {
                }) {
                    Text("Reintentar")
                }
            }

        }

    }
}

@Composable
fun HomeLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun HomeLoaded(
    messages: List<Message?>,
    message: String,
    onMessageTyped: (value: String) -> Unit,
    onButtonClicked: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true,

            ) {

            itemsIndexed(messages) { index, _ ->
                val currentMessageIndex = messages.count() - index - 1
                val currentMessage = messages[currentMessageIndex]
                MessageBox(
                    modifier = Modifier.fillMaxWidth(),
                    message = currentMessage?.message ?: "",
                    author = currentMessage?.author ?: ""
                )
            }

        }
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxWidth(),
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChatTextInput(
                    modifier = Modifier.weight(1f),
                    text = message,
                    onValueChange = onMessageTyped
                )
                Spacer(modifier = Modifier.width(10.dp))
                ChatSendButton {
                    onButtonClicked()
                }
            }
        }

    }
}

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: String,
    author: String,
    myMessage: Boolean = false
) {
    Row(
        modifier = modifier.padding(5.dp),
        horizontalArrangement = if (myMessage) Arrangement.End else Arrangement.End
    ) {
        Surface(
            color = MaterialTheme.colors.primary,
            shape = RoundedCornerShape(10.dp),

            ) {
            Column(
                modifier = Modifier.padding(
                    top = 5.dp,
                    bottom = 5.dp,
                    start = 10.dp,
                    end = 10.dp
                ),
            ) {
                Text(
                    "$author:",
                    style = MaterialTheme.typography.caption
                )
                Text(message)
            }
        }
    }
}

@Composable
fun ChatSendButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        elevation = 4.dp,
        shape = CircleShape,
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .width(45.dp)
            .height(45.dp)

    ) {
        IconButton(
            onClick = { onClick() },
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun ChatTextInput(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (value: String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier
            .height(40.dp)
            .background(
                Color(0xFFc7c7c7),
                RoundedCornerShape(20.dp),
            )
            .fillMaxWidth(),
        decorationBox = { textField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                textField()
            }
        }
    )
}

@Preview
@Composable
fun preview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        HomeLoaded(messages = mutableListOf(), "", {})
        {}
    }
}