package com.starrydot.starrycosmo.presentation.design.description

import androidx.compose.runtime.Composable
import com.starrydot.starrycosmo.domain.device.model.DeviceInstallationMode

@Composable
fun DeviceInstallationMode.toStringDescription(): String = when (this) {
    DeviceInstallationMode.SEAT -> "Seat"
    DeviceInstallationMode.HELMET -> "Helmet"
}
