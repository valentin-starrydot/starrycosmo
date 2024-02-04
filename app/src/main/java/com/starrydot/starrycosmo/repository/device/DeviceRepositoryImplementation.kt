package com.starrydot.starrycosmo.repository.device

import android.annotation.SuppressLint
import android.content.Context
import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.BluetoothDevice
import com.starrydot.starrycosmo.domain.device.model.BluetoothDeviceCharacteristic
import com.starrydot.starrycosmo.domain.device.model.BluetoothDeviceDetails
import com.starrydot.starrycosmo.domain.device.model.BluetoothDeviceService
import com.starrydot.starrycosmo.domain.device.model.Device
import com.starrydot.starrycosmo.repository.device.api.DeviceApiService
import com.starrydot.starrycosmo.repository.device.api.mapper.toDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import no.nordicsemi.android.kotlin.ble.client.main.callback.ClientBleGatt
import no.nordicsemi.android.kotlin.ble.core.scanner.BleScannerSettings
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
    private var bluetoothDevices = listOf<BluetoothDevice>()

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
            //Init an aggregator before scan
            val aggregator = BleScanResultAggregator()
            bleScanner.scan(
                settings = BleScannerSettings(
                    includeStoredBondedDevices = false,
                    reportDelay = 500
                )
            )
                .map { aggregator.aggregate(it) }.map { bleScanResults ->
                    bleScanResults.map { bleScanResult ->
                        BluetoothDevice(
                            macAddress = bleScanResult.device.address,
                            name = if (bleScanResult.advertisedName.isNullOrBlank()) null else bleScanResult.advertisedName,
                            isBound = bleScanResult.device.isBonded
                        )
                    }
                }.onEach {
                    //Store updated value of Bluetooth devices
                    bluetoothDevices = it
                }
        }

    @SuppressLint("MissingPermission")
    override suspend fun getBluetoothDeviceDetails(deviceMacAddress: String): BluetoothDeviceDetails? =
        withContext(Dispatchers.IO) {
            bluetoothDevices.find { it.macAddress == deviceMacAddress }?.let { bluetoothDevice ->
                try {
                    //Init connection and wait for device to be bound
                    val connection = ClientBleGatt.connect(context, bluetoothDevice.macAddress, this)
                    connection.waitForBonding()
                    //Discover all the device services
                    val discoveredServices = connection.discoverServices().services
                    //Disconnect to avoid unnecessary workload
                    connection.disconnect()
                    //Map to model
                    val bluetoothDeviceDetails = BluetoothDeviceDetails(
                        macAddress = bluetoothDevice.macAddress,
                        name = bluetoothDevice.name,
                        isBound = bluetoothDevice.isBound,
                        services = discoveredServices.map { service ->
                            BluetoothDeviceService(
                                uuid = service.uuid.toString(),
                                characteristics = service.characteristics.map { characteristic ->
                                    BluetoothDeviceCharacteristic(
                                        uuid = characteristic.uuid.toString(),
                                        type = characteristic.properties.firstOrNull()?.name
                                    )
                                }
                            )
                        }
                    )
                    bluetoothDeviceDetails
                } catch (exception: Exception) {
                    null
                }
            }
        }
}
