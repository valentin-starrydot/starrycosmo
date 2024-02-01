package com.starrydot.starrycosmo.repository.device.api.mapper

import com.starrydot.starrycosmo.domain.device.model.Device
import com.starrydot.starrycosmo.domain.device.model.DeviceCategory
import com.starrydot.starrycosmo.domain.device.model.DeviceInstallationMode
import com.starrydot.starrycosmo.domain.device.model.DeviceLightMode
import com.starrydot.starrycosmo.domain.device.model.DeviceModel
import com.starrydot.starrycosmo.repository.device.api.model.DeviceApi
import com.starrydot.starrycosmo.repository.device.api.model.DeviceApiInstallationMode
import com.starrydot.starrycosmo.repository.device.api.model.DeviceApiLightMode
import com.starrydot.starrycosmo.repository.device.api.model.DeviceApiModel
import com.starrydot.starrycosmo.repository.device.api.model.DeviceApiProduct

fun DeviceApi.toDevice(): Device = Device(
    category = when (this.model) {
        DeviceApiModel.RIDE -> DeviceCategory.RIDE
        DeviceApiModel.VISION -> DeviceCategory.VISION
        DeviceApiModel.REMOTE -> DeviceCategory.REMOTE
    },
    model = when (this.product) {
        DeviceApiProduct.RIDE -> DeviceModel.RIDE
        DeviceApiProduct.RIDE_LITE -> DeviceModel.RIDE_LITE
        DeviceApiProduct.REMOTE_V1 -> DeviceModel.REMOTE_V1
        null -> null
    },
    macAddress = this.macAddress,
    firmwareVersion = this.firmwareVersion,
    serial = this.serial,
    installationMode = when (this.installationMode) {
        DeviceApiInstallationMode.seat -> DeviceInstallationMode.SEAT
        DeviceApiInstallationMode.helmet -> DeviceInstallationMode.HELMET
        null -> null
    },
    hasBrakeLight = this.hasBrakeLight,
    lightMode = when (this.lightMode) {
        DeviceApiLightMode.WARNING -> DeviceLightMode.WARNING
        DeviceApiLightMode.POSITION -> DeviceLightMode.POSITION
        DeviceApiLightMode.BOTH -> DeviceLightMode.BOTH
        DeviceApiLightMode.OFF -> DeviceLightMode.OFF
        null -> null
    },
    isLightAutoEnabled = this.isLightAutoEnabled,
    lightPercent = this.lightPercent
)
