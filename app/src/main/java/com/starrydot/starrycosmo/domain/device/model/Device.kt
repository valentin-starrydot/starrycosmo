package com.starrydot.starrycosmo.domain.device.model

data class Device(
    val category: DeviceCategory,
    val model: DeviceModel?,
    val macAddress: String,
    val firmwareVersion: String,
    val serial: String,
    val installationMode: DeviceInstallationMode?,
    val hasBrakeLight: Boolean,
    val lightMode: DeviceLightMode?,
    val isLightAutoEnabled: Boolean,
    val lightPercent: Int
)

enum class DeviceCategory {
    RIDE,
    VISION,
    REMOTE
}

enum class DeviceModel {
    RIDE,
    RIDE_LITE,
    REMOTE_V1
}

enum class DeviceInstallationMode {
    seat,
    helmet
}

enum class DeviceLightMode {
    WARNING,
    POSITION,
    BOTH,
    OFF
}
