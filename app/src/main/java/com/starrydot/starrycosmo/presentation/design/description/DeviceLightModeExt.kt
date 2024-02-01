package com.starrydot.starrycosmo.presentation.design.description

import androidx.compose.runtime.Composable
import com.starrydot.starrycosmo.domain.device.model.DeviceLightMode

@Composable
fun DeviceLightMode.toStringDescription(): String = when (this) {
    DeviceLightMode.WARNING -> "Warning"
    DeviceLightMode.POSITION -> "Position"
    DeviceLightMode.BOTH -> "Both"
    DeviceLightMode.OFF -> "Off"
}
