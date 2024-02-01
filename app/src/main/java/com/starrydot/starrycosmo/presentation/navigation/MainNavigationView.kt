package com.starrydot.starrycosmo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screens(val path: String) {
    DEVICES(path = "devices"),
    DEVICE_DETAILS(path = "device_details")
}

@Composable
fun MainNavigationView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.DEVICES.path
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screens.DEVICES.path) {
            //TODO -> Implement Compose View here
        }

        composable("${Screens.DEVICE_DETAILS.path}/{macAddress}") { backStackEntry ->
            backStackEntry.arguments?.getString("macAddress")?.let { macAddress ->
                //TODO -> Implement Compose View here by passing macAddress
            }
        }

    }
}
