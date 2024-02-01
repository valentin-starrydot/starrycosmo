package com.starrydot.starrycosmo.repository.device

import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.Device
import com.starrydot.starrycosmo.repository.device.api.DeviceApiService
import com.starrydot.starrycosmo.repository.device.api.mapper.toDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_BASE_URL = "https://cosmo-api.develop-sr3snxi-x6u2x52ooksf4.de-2.platformsh.site"

class DeviceRepositoryImplementation : DeviceRepository {

    private val retrofit = Retrofit.Builder().baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val deviceApiService = retrofit.create(DeviceApiService::class.java)

    //Temp storage for devices in the app lifecycle
    private val devices = mutableListOf<Device>()
    override suspend fun getDevices(): List<Device> = withContext(Dispatchers.IO) {
        devices.clear()
        val devicesApi = deviceApiService.getDevices()
        val mappedDevices = devicesApi.devices.map { deviceApi -> deviceApi.toDevice() }
        devices.addAll(mappedDevices)
        devices
    }

    override suspend fun getDeviceDetails(deviceMacAddress: String): Device? =
        devices.find { device -> device.macAddress == deviceMacAddress }
}
