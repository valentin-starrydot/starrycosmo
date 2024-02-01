package com.starrydot.starrycosmo.presentation.bluetooth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.starrydot.starrycosmo.presentation.bluetooth.details.BluetoothDeviceDetailsView
import com.starrydot.starrycosmo.presentation.bluetooth.list.BluetoothDevicesListView

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
            BluetoothDevicesListView(
                modifier = Modifier.fillMaxSize(),
                onDeviceClick = { deviceMacAddress ->
                    navController.navigate("${Screens.DEVICE_DETAILS}/$deviceMacAddress")
                }
            )
        }

        composable("${Screens.DEVICE_DETAILS.path}/{macAddress}") { backStackEntry ->
            backStackEntry.arguments?.getString("macAddress")?.let { macAddress ->
                BluetoothDeviceDetailsView(
                    modifier = Modifier.fillMaxSize(),
                    deviceMacAddress = macAddress,
                    onNeedToGoBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

    }
}
