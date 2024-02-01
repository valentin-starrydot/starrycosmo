package com.starrydot.starrycosmo.presentation.design.description

import androidx.compose.runtime.Composable
import com.starrydot.starrycosmo.domain.device.model.DeviceCategory

@Composable
fun DeviceCategory.toStringDescription(): String = when (this) {
    DeviceCategory.RIDE -> "Ride"
    DeviceCategory.VISION -> "Vision"
    DeviceCategory.REMOTE -> "Remote"
}
