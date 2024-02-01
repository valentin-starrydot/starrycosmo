package com.starrydot.starrycosmo.domain.device

import com.starrydot.starrycosmo.domain.device.model.BluetoothDevice
import com.starrydot.starrycosmo.domain.device.model.BluetoothDeviceDetails
import com.starrydot.starrycosmo.domain.device.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun getDevices(): List<Device>
    suspend fun getDeviceDetails(deviceMacAddress: String): Device?
    suspend fun searchForBluetoothDevices(): Flow<List<BluetoothDevice>>
    suspend fun getBluetoothDeviceDetails(deviceMacAddress: String): BluetoothDeviceDetails?
}
