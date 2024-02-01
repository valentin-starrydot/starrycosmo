package com.starrydot.starrycosmo.presentation.description

import androidx.compose.runtime.Composable
import com.starrydot.starrycosmo.domain.device.model.DeviceModel

@Composable
fun DeviceModel.toStringDescription(): String = when (this) {
    DeviceModel.RIDE -> "Ride"
    DeviceModel.RIDE_LITE -> "Ride Lite"
    DeviceModel.REMOTE_V1 -> "Remote V1"
}
