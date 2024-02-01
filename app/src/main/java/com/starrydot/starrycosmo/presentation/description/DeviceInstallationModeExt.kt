package com.starrydot.starrycosmo.presentation.description

import androidx.compose.runtime.Composable
import com.starrydot.starrycosmo.domain.device.model.DeviceInstallationMode

@Composable
fun DeviceInstallationMode.toStringDescription(): String = when (this) {
    DeviceInstallationMode.SEAT -> "Seat"
    DeviceInstallationMode.HELMET -> "Helmet"
}
