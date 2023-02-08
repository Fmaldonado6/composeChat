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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dscuanl.composechat.data.models.Message
import com.dscuanl.composechat.data.models.User
import com.dscuanl.composechat.ui.navigation.Screens
import com.dscuanl.composechat.ui.theme.ComposeChatTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun HomeScreen(
    vm: HomeViewModel,
    navController: NavController
) {
    val state by vm.uiState.collectAsState()
    val messages by vm.messages.collectAsState(initial = mutableListOf())
    val user by vm.currentUser.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeAppBar(
                title = "Chat",
                isDropdownExpanded = vm.dropDownExpanded,
                userPhotoUrl = user?.photoUrl,
                onIconClicked = { vm.dropDownExpanded = true },
                onSignOutClicked = { vm.signOut() },
                onDropDownDismissed = { vm.dropDownExpanded = false }
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
                is HomeUiState.Empty -> Text("No users")
                is HomeUiState.Loading -> HomeLoading()
                is HomeUiState.SignOut -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(Screens.Login.route) {
                            popUpTo(Screens.Home.route) { inclusive = true }
                        }
                    }
                    HomeLoading()
                }
                is HomeUiState.Error -> Button(onClick = {
                }) {
                    Text("Reintentar")
                }
            }

        }

    }
}

@Composable
fun HomeLoading(modifier: Modifier = Modifier) {
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
                    author = currentMessage?.author ?: "",
                    myMessage = currentMessage?.myMessage ?: false,
                    authorPicture = currentMessage?.authorPicture
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
fun HomeAppBar(
    modifier: Modifier = Modifier,
    title: String,
    isDropdownExpanded: Boolean,
    userPhotoUrl: String?,
    onIconClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onDropDownDismissed: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(title, style = TextStyle(fontSize = 30.sp))
        },
        modifier = Modifier.height(70.dp),
        elevation = 4.dp,
        actions = {
            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopStart)
            ) {

                IconButton(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = onIconClicked
                ) {
                    Surface(
                        shape = CircleShape,
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(userPhotoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Foto de perfil"
                        )
                    }
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = onDropDownDismissed
                ) {
                    DropdownMenuItem(onClick = onSignOutClicked) {
                        Text("Cerrar sesión")
                    }
                }
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: String,
    author: String,
    authorPicture: String?,
    myMessage: Boolean = false
) {
    Row(
        modifier = modifier.padding(5.dp),
        horizontalArrangement = if (myMessage) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!myMessage)
            Surface(
                shape = CircleShape,
                modifier = Modifier.padding(end = 10.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(authorPicture)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto de perfil"
                )
            }
        Surface(
            color =
            if (myMessage)
                MaterialTheme.colors.primary.copy(alpha = .5f)
            else
                MaterialTheme.colors.primary,
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
        if (myMessage)
            Surface(
                shape = CircleShape,
                modifier = Modifier.padding(start = 10.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(authorPicture)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto de perfil"
                )
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
    }
}