package com.starrydot.starrycosmo.presentation.description

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.domain.device.model.DeviceInstallationMode

@Composable
fun DeviceInstallationMode.toStringDescription(): String = stringResource(id = when (this) {
    DeviceInstallationMode.SEAT -> R.string.device_installation_mode_seat
    DeviceInstallationMode.HELMET -> R.string.device_installation_mode_helmet
})
