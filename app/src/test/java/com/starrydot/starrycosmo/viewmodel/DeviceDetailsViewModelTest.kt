package com.starrydot.starrycosmo.viewmodel

import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.Device
import com.starrydot.starrycosmo.domain.device.model.DeviceCategory
import com.starrydot.starrycosmo.domain.device.model.DeviceInstallationMode
import com.starrydot.starrycosmo.domain.device.model.DeviceLightMode
import com.starrydot.starrycosmo.domain.device.model.DeviceModel
import com.starrydot.starrycosmo.presentation.details.DeviceDetailsViewModel
import com.starrydot.starrycosmo.presentation.details.State
import com.starrydot.starrycosmo.presentation.details.UIAction
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

//Test the DeviceDetailsViewModel state generation by mocking device repository
@OptIn(ExperimentalCoroutinesApi::class)
class DeviceDetailsViewModelTest {

    private val fakeDevice = Device(
        category = DeviceCategory.RIDE,
        model = DeviceModel.RIDE_LITE,
        macAddress = "4921201e38d5",
        serial = "BC892C9C-047D-8402-A9FD-7B2CC0048736",
        firmwareVersion = "2.2.2",
        installationMode = DeviceInstallationMode.HELMET,
        isLightAutoEnabled = false,
        lightMode = DeviceLightMode.OFF,
        lightPercent = 0,
        hasBrakeLight = false
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
        val viewModel = DeviceDetailsViewModel(fakeDeviceRepository)

        whenever(fakeDeviceRepository.getDeviceDetails(fakeDevice.macAddress)) doReturn fakeDevice
        //When
        viewModel.getDeviceDetails(fakeDevice.macAddress)
        //Then
        verify(
            fakeDeviceRepository,
            times(1)
        ).getDeviceDetails(fakeDevice.macAddress)
        val loadedState = viewModel.observableState.value as? State.Loaded
        assert(loadedState?.macAddress == fakeDevice.macAddress)
        assert(loadedState?.serial == fakeDevice.serial)
        assert(loadedState?.category == fakeDevice.category)
        assert(loadedState?.model == fakeDevice.model)
        assert(loadedState?.firmwareVersion == fakeDevice.firmwareVersion)
        assert(loadedState?.lightMode == fakeDevice.lightMode)
        assert(loadedState?.isLightAutoEnabled == fakeDevice.isLightAutoEnabled)
        assert(loadedState?.lightPercentValue == fakeDevice.lightPercent)
        assert(loadedState?.hasBrakeLight == fakeDevice.hasBrakeLight)
        assert(loadedState?.installationMode == fakeDevice.installationMode)
    }

    @Test
    fun `Device don't exist`() = runTest(UnconfinedTestDispatcher()) {
        //Given
        val fakeDeviceRepository = mock<DeviceRepository>()
        val viewModel = DeviceDetailsViewModel(fakeDeviceRepository)

        whenever(fakeDeviceRepository.getDeviceDetails(fakeDevice.macAddress)) doReturn null
        //When
        val deferredUIActionsObserver = async {
            viewModel.observableUIAction.first()
        }
        viewModel.getDeviceDetails(fakeDevice.macAddress)
        //Then
        verify(fakeDeviceRepository, times(1)).getDeviceDetails(fakeDevice.macAddress)
        assert(deferredUIActionsObserver.await() == UIAction.NotifyErrorWhileRetrievingDevice)
    }
}
