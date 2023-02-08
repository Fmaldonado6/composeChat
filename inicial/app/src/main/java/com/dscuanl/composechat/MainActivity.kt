package com.dscuanl.composechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.dscuanl.composechat.ui.navigation.AppNavigation
import com.dscuanl.composechat.ui.navigation.Screens
import com.dscuanl.composechat.ui.theme.ComposeChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeChatTheme {
                    AppNavigation(startDestination = Screens.Login.route)
            }
        }
    }
}

