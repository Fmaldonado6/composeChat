package com.dscuanl.composechat.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.dscuanl.composechat.data.models.User
import com.dscuanl.composechat.ui.theme.ComposeChatTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun HomeScreen(
    vm: HomeViewModel,
    navController: NavController
) {
    val state by vm.uiState.collectAsState()
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
                is HomeUiState.Loaded -> HomeLoaded(users = mutableListOf())
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
fun HomeLoaded(users: List<User?>) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(users) { user ->
                Row(modifier = Modifier.padding(5.dp)) {
                    Text(user?.displayName ?: "Hola")
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
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
                ChatTextInput(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(10.dp))
                ChatSendButton()
            }
        }

    }
}

@Composable
fun ChatSendButton(modifier: Modifier = Modifier) {
    Surface(
        elevation = 4.dp,
        shape = CircleShape,
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .width(45.dp)
            .height(45.dp)

    ) {
        IconButton(
            onClick = {},
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun ChatTextInput(modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf("") }
    BasicTextField(
        value = textState,
        onValueChange = {
            textState = it
        },
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

        HomeLoaded(users = mutableListOf())
    }
}