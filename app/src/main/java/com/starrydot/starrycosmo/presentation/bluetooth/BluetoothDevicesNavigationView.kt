package com.starrydot.starrycosmo.presentation.bluetooth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screens(val path: String) {
    SEARCH(path = "search"),
    DEVICE_DETAILS(path = "device_details")
}

@Composable
fun BluetoothDevicesNavigationView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.SEARCH.path
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screens.SEARCH.path) {
            //TODO -> Add screen
        }

        composable("${Screens.DEVICE_DETAILS.path}/{address}") { backStackEntry ->
            backStackEntry.arguments?.getString("address")?.let { address ->
                //TODO -> Connect to device according to its address and search for services & characteristics
            }
        }

    }
}
