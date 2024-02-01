package com.starrydot.starrycosmo.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.starrydot.starrycosmo.presentation.list.DevicesListView

enum class Screens(val path: String) {
    DEVICES_LIST(path = "devices_list"),
    DEVICE_DETAILS(path = "device_details")
}

@Composable
fun MainNavigationView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screens.DEVICES_LIST.path
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screens.DEVICES_LIST.path) {
            DevicesListView(
                modifier = Modifier.fillMaxSize(),
                onDeviceClick = { deviceMacAddress ->
                    navController.navigate("${Screens.DEVICE_DETAILS.path}/$deviceMacAddress")
                }
            )
        }

        composable("${Screens.DEVICE_DETAILS.path}/{macAddress}") { backStackEntry ->
            backStackEntry.arguments?.getString("macAddress")?.let { macAddress ->
                //TODO -> Implement Compose View here by passing macAddress
            }
        }

    }
}
