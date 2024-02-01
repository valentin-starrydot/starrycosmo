package com.starrydot.starrycosmo.presentation.description

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.domain.device.model.DeviceLightMode

@Composable
fun DeviceLightMode.toStringDescription(): String = stringResource(id = when (this) {
    DeviceLightMode.WARNING -> R.string.device_light_mode_warning
    DeviceLightMode.POSITION -> R.string.device_light_mode_position
    DeviceLightMode.BOTH -> R.string.device_light_mode_both
    DeviceLightMode.OFF -> R.string.device_light_mode_off
})
