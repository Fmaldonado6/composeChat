package com.dscuanl.composechat.ui.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dscuanl.composechat.ui.screens.auth.AuthScreen
import com.dscuanl.composechat.ui.screens.auth.AuthScreenViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screens.Login.route) {
            val viewModel: AuthScreenViewModel = viewModel()
            AuthScreen(vm = viewModel, navController = navController)
        }

        composable(Screens.Home.route){
            Text("Hola")
        }


    }
}

enum class Screens(val route: String) {
    Login("login"),
    Home("home"),
    Chat("chat/{chatId}")
}