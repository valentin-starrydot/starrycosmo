package com.starrydot.starrycosmo.presentation.description

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.domain.device.model.DeviceCategory

@Composable
fun DeviceCategory.toStringDescription(): String = stringResource(id = when (this) {
    DeviceCategory.RIDE -> R.string.device_category_ride
    DeviceCategory.VISION -> R.string.device_category_vision
    DeviceCategory.REMOTE -> R.string.device_category_remote
})
