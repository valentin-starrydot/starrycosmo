package com.starrydot.starrycosmo.presentation.description

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.domain.device.model.DeviceModel

@Composable
fun DeviceModel.toStringDescription(): String = stringResource(id = when (this) {
    DeviceModel.RIDE -> R.string.device_model_ride
    DeviceModel.RIDE_LITE -> R.string.device_model_ride_lite
    DeviceModel.REMOTE_V1 -> R.string.device_model_remote_v1
})
