package com.starrydot.starrycosmo.domain.device

import com.starrydot.starrycosmo.domain.device.model.Device

interface DeviceRepository {
    suspend fun getDevices(): List<Device>
    suspend fun getDeviceDetails(deviceMacAddress: String): Device?
}
