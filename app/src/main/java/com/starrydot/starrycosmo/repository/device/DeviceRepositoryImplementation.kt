package com.starrydot.starrycosmo.repository.device

import android.annotation.SuppressLint
import android.content.Context
import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.BluetoothDevice
import com.starrydot.starrycosmo.domain.device.model.BluetoothDeviceDetails
import com.starrydot.starrycosmo.domain.device.model.Device
import com.starrydot.starrycosmo.repository.device.api.DeviceApiService
import com.starrydot.starrycosmo.repository.device.api.mapper.toDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import no.nordicsemi.android.kotlin.ble.client.main.callback.ClientBleGatt
import no.nordicsemi.android.kotlin.ble.scanner.BleScanner
import no.nordicsemi.android.kotlin.ble.scanner.aggregator.BleScanResultAggregator
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_BASE_URL = "https://cosmo-api.develop-sr3snxi-x6u2x52ooksf4.de-2.platformsh.site"

class DeviceRepositoryImplementation(private val context: Context) : DeviceRepository {

    private val retrofit = Retrofit.Builder().baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val deviceApiService = retrofit.create(DeviceApiService::class.java)

    private val bleScanner = BleScanner(context)

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

    @SuppressLint("MissingPermission")
    override suspend fun searchForBluetoothDevices(): Flow<List<BluetoothDevice>> =
        withContext(Dispatchers.IO) {
            //Init a new aggregator before a new scan
            val aggregator = BleScanResultAggregator()
            bleScanner.scan().map { aggregator.aggregateDevices(it) }.map { bleScanResults ->
                bleScanResults.map { bleScanResult ->
                    BluetoothDevice(
                        address = bleScanResult.address,
                        name = bleScanResult.name,
                        isBound = bleScanResult.isBonded
                    )
                }
            }
        }

    @SuppressLint("MissingPermission")
    override suspend fun getBluetoothDeviceDetails(deviceAddress: String): BluetoothDeviceDetails? =
        withContext(Dispatchers.IO) {
            try {
                val connection = ClientBleGatt.connect(context, deviceAddress, this)
                val services = connection.discoverServices()
                services.services.forEach { service ->
                    service.characteristics.forEach { characteristic ->
                    }
                }
                //TODO -> Bind to data with each service/characteristics data we need
                null
            } catch (exception: Exception) {
                null
            }
        }
}
