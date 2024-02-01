package com.starrydot.starrycosmo.domain.device.model

data class BluetoothDeviceDetails(
    val macAddress: String,
    val name: String?,
    val isBonded: Boolean,
    val services: List<BluetoothDeviceService>
)

data class BluetoothDeviceService(
    val uuid: String,
    val characteristics: List<BluetoothDeviceCharacteristic>
)

data class BluetoothDeviceCharacteristic(val uuid: String)
