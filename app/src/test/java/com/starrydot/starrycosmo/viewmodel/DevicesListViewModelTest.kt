package com.starrydot.starrycosmo.viewmodel

import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.Device
import com.starrydot.starrycosmo.domain.device.model.DeviceCategory
import com.starrydot.starrycosmo.domain.device.model.DeviceInstallationMode
import com.starrydot.starrycosmo.domain.device.model.DeviceLightMode
import com.starrydot.starrycosmo.domain.device.model.DeviceModel
import com.starrydot.starrycosmo.presentation.list.DevicesListViewModel
import com.starrydot.starrycosmo.presentation.list.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
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

//Test the DevicesListViewModel state generation by mocking device repository
@OptIn(ExperimentalCoroutinesApi::class)
class DevicesListViewModelTest {

    private val fakeDevicesList = listOf(
        Device(
            category = DeviceCategory.RIDE,
            model = DeviceModel.RIDE_LITE,
            macAddress = "4921201e38d5",
            serial = "BC892C9C-047D-8402-A9FD-7B2CC0048736",
            firmwareVersion = "2.2.2",
            installationMode = DeviceInstallationMode.HELMET,
            isLightAutoEnabled = false,
            lightMode = DeviceLightMode.BOTH,
            lightPercent = 0,
            hasBrakeLight = false
        ),
        Device(
            category = DeviceCategory.REMOTE,
            model = DeviceModel.REMOTE_V1,
            macAddress = "67dsvavb6",
            serial = "B65D892C9C-047D-8402-AFR5-7B2CC0048736",
            firmwareVersion = "2.3.4",
            installationMode = null,
            isLightAutoEnabled = false,
            lightMode = DeviceLightMode.OFF,
            lightPercent = 0,
            hasBrakeLight = false
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
        val viewModel = DevicesListViewModel(fakeDeviceRepository)

        whenever(fakeDeviceRepository.getDevices()) doReturn fakeDevicesList
        //When
        viewModel.getData()
        //Then
        verify(
            fakeDeviceRepository,
            times(1)
        ).getDevices()
        val loadedState = viewModel.observableState.value as? State.Loaded
        assert(loadedState?.listDevices?.all { listDevice ->
            fakeDevicesList.any { device -> listDevice.model == device.model && listDevice.category == device.category && listDevice.macAddress == device.macAddress }
        } ?: false)
    }
}
