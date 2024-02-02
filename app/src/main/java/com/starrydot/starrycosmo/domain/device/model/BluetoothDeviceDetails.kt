package com.starrydot.starrycosmo.domain.device.model

data class BluetoothDeviceDetails(
    val macAddress: String,
    val name: String?,
    val isBound: Boolean,
    val services: List<BluetoothDeviceService>
)

data class BluetoothDeviceService(
    val uuid: String,
    val characteristics: List<BluetoothDeviceCharacteristic>
)

data class BluetoothDeviceCharacteristic(val uuid: String, val type: String?)
