package com.starrydot.starrycosmo.repository.device

import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.Device

class DeviceRepositoryImplementation: DeviceRepository {
    override suspend fun getDevices(): List<Device> {
        TODO("Not yet implemented")
    }

    override suspend fun getDeviceDetails(deviceMacAddress: String): Device? {
        TODO("Not yet implemented")
    }
}
