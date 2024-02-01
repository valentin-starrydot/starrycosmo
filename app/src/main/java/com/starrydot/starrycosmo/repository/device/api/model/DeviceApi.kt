package com.starrydot.starrycosmo.repository.device.api.model

data class DeviceApiResults(val devices: List<DeviceApi>)
data class DeviceApi(
    val model: DeviceApiModel,
    val product: DeviceApiProduct?,
    val macAddress: String,
    val firmwareVersion: String,
    val serial: String?,
    val installationMode: DeviceApiInstallationMode?,
    val hasBrakeLight: Boolean,
    val lightMode: DeviceApiLightMode?,
    val isLightAutoEnabled: Boolean,
    val lightPercent: Int
)

enum class DeviceApiModel {
    RIDE,
    VISION,
    REMOTE
}

enum class DeviceApiProduct {
    RIDE,
    RIDE_LITE,
    REMOTE_V1
}

enum class DeviceApiInstallationMode {
    seat,
    helmet
}

enum class DeviceApiLightMode {
    WARNING,
    POSITION,
    BOTH,
    OFF
}

