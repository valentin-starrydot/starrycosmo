package com.starrydot.starrycosmo.viewmodel

import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.BluetoothDeviceCharacteristic
import com.starrydot.starrycosmo.domain.device.model.BluetoothDeviceDetails
import com.starrydot.starrycosmo.domain.device.model.BluetoothDeviceService
import com.starrydot.starrycosmo.presentation.bluetooth.details.BluetoothDeviceDetailsViewModel
import com.starrydot.starrycosmo.presentation.bluetooth.details.State
import com.starrydot.starrycosmo.presentation.bluetooth.details.UIAction
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

//Test the BluetoothDeviceDetailsViewModel state generation by mocking device repository
@OptIn(ExperimentalCoroutinesApi::class)
class BluetoothDeviceDetailsViewModelTest {

    private val fakeBluetoothDeviceDetails = BluetoothDeviceDetails(
        name = "Pixel 6A",
        macAddress = "4921201e38d5",
        isBound = false,
        services = listOf(
            BluetoothDeviceService(
                uuid = UUID.randomUUID().toString(),
                characteristics = listOf(
                    BluetoothDeviceCharacteristic(uuid = UUID.randomUUID().toString(), type = null),
                    BluetoothDeviceCharacteristic(
                        uuid = UUID.randomUUID().toString(),
                        type = "PROPERTY_READ"
                    ),
                    BluetoothDeviceCharacteristic(
                        uuid = UUID.randomUUID().toString(),
                        type = "PROPERTY_WRITE"
                    )
                )
            ),
            BluetoothDeviceService(
                uuid = UUID.randomUUID().toString(),
                characteristics = listOf(
                    BluetoothDeviceCharacteristic(uuid = UUID.randomUUID().toString(), type = null),
                    BluetoothDeviceCharacteristic(
                        uuid = UUID.randomUUID().toString(),
                        type = "PROPERTY_READ"
                    ),
                    BluetoothDeviceCharacteristic(
                        uuid = UUID.randomUUID().toString(),
                        type = "PROPERTY_WRITE"
                    )
                )
            ),
            BluetoothDeviceService(
                uuid = UUID.randomUUID().toString(),
                characteristics = listOf(
                    BluetoothDeviceCharacteristic(uuid = UUID.randomUUID().toString(), type = null),
                    BluetoothDeviceCharacteristic(
                        uuid = UUID.randomUUID().toString(),
                        type = "PROPERTY_READ"
                    ),
                    BluetoothDeviceCharacteristic(
                        uuid = UUID.randomUUID().toString(),
                        type = "PROPERTY_WRITE"
                    )
                )
            )
        )
    )

    private val mainThread = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThread.cancel()
    }

    @Test
    fun `Load initial state`() = runTest {
        //Given
        val fakeDeviceRepository = mock<DeviceRepository>()
        val viewModel = BluetoothDeviceDetailsViewModel(fakeDeviceRepository)

        whenever(fakeDeviceRepository.getBluetoothDeviceDetails(fakeBluetoothDeviceDetails.macAddress)) doReturn fakeBluetoothDeviceDetails
        //When
        viewModel.getBluetoothDeviceDetails(fakeBluetoothDeviceDetails.macAddress)
        //Then
        verify(
            fakeDeviceRepository,
            times(1)
        ).getBluetoothDeviceDetails(fakeBluetoothDeviceDetails.macAddress)
        val loadedState = viewModel.observableState.value as? State.Loaded
        assert(loadedState?.name == fakeBluetoothDeviceDetails.name)
        assert(loadedState?.macAddress == fakeBluetoothDeviceDetails.macAddress)
        assert(loadedState?.isBound == fakeBluetoothDeviceDetails.isBound)
        assert(loadedState?.deviceServices?.all { deviceService -> fakeBluetoothDeviceDetails.services.any { bluetoothDeviceService -> bluetoothDeviceService.uuid == deviceService.uuid && bluetoothDeviceService.characteristics.all { bluetoothDeviceCharacteristic -> deviceService.characteristics.any { it.uuid == bluetoothDeviceCharacteristic.uuid && it.type == bluetoothDeviceCharacteristic.type } } } }
            ?: false)
    }

    @Test
    fun `Bluetooth device don't exist`() = runTest(UnconfinedTestDispatcher()) {
        //Given
        val fakeDeviceRepository = mock<DeviceRepository>()
        val viewModel = BluetoothDeviceDetailsViewModel(fakeDeviceRepository)

        whenever(fakeDeviceRepository.getBluetoothDeviceDetails(fakeBluetoothDeviceDetails.macAddress)) doReturn null
        //When
        val deferredUIActionsObserver = async {
            viewModel.observableUIAction.first()
        }
        //When
        viewModel.getBluetoothDeviceDetails(fakeBluetoothDeviceDetails.macAddress)
        //Then
        verify(fakeDeviceRepository, times(1)).getBluetoothDeviceDetails(fakeBluetoothDeviceDetails.macAddress)
        assert(deferredUIActionsObserver.await() == UIAction.NotifyErrorWhileRetrievingDevice)
    }
}
