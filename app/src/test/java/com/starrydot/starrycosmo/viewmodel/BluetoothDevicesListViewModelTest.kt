package com.starrydot.starrycosmo.viewmodel

import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.BluetoothDevice
import com.starrydot.starrycosmo.presentation.bluetooth.list.BluetoothDevicesListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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

//Test the BluetoothDevicesListViewModel state generation by mocking device repository
@OptIn(ExperimentalCoroutinesApi::class)
class BluetoothDevicesListViewModelTest {

    private val fakeBluetoothDevicesList = flowOf(
        listOf(
            BluetoothDevice(
                name = "Pixel 6A",
                macAddress = "4921201e38d5",
                isBound = true
            ),
            BluetoothDevice(
                name = "iPhone 15 Pro Max",
                macAddress = "677aa677",
                isBound = false
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
        val viewModel = BluetoothDevicesListViewModel(fakeDeviceRepository)

        whenever(fakeDeviceRepository.searchForBluetoothDevices()) doReturn fakeBluetoothDevicesList
        //When
        viewModel.searchForDevices()
        //Then
        verify(
            fakeDeviceRepository,
            times(1)
        ).searchForBluetoothDevices()
        val loadedState = viewModel.observableState.value
        assert(loadedState.listBluetoothDevices.all { listBluetoothDevice ->
            fakeBluetoothDevicesList.first()
                .any { bluetoothDevice -> listBluetoothDevice.macAddress == bluetoothDevice.macAddress && listBluetoothDevice.name == bluetoothDevice.name && listBluetoothDevice.isBound == bluetoothDevice.isBound }
        })
    }
}
