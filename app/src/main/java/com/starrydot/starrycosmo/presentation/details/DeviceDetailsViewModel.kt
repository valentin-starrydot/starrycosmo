package com.starrydot.starrycosmo.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.DeviceCategory
import com.starrydot.starrycosmo.domain.device.model.DeviceInstallationMode
import com.starrydot.starrycosmo.domain.device.model.DeviceLightMode
import com.starrydot.starrycosmo.domain.device.model.DeviceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class State {
    object Loading : State()
    data class Loaded(
        val category: DeviceCategory,
        val model: DeviceModel?,
        val macAddress: String,
        val serial: String?,
        val firmwareVersion: String,
        val installationMode: DeviceInstallationMode?,
        val isLightAutoEnabled: Boolean,
        val lightMode: DeviceLightMode?,
        val lightPercentValue: Int,
        val hasBrakeLight: Boolean
    ) : State()
}

sealed class UIAction {
    object NotifyErrorWhileRetrievingDevice: UIAction()
}

@HiltViewModel
class DeviceDetailsViewModel @Inject constructor(private val deviceRepository: DeviceRepository) : ViewModel() {
    private val state = MutableStateFlow<State>(State.Loading)
    val observableState: StateFlow<State> = state

    private val uiAction = MutableSharedFlow<UIAction>(replay = 0)
    val observableUIAction: SharedFlow<UIAction> = uiAction

    fun getDeviceDetails(deviceMacAddress: String) {
        viewModelScope.launch {
            //Get device details or display an error if no devices could be found
            deviceRepository.getDeviceDetails(deviceMacAddress)?.let { device ->
                state.value = State.Loaded(
                    category = device.category,
                    model = device.model,
                    macAddress = device.macAddress,
                    serial = device.serial,
                    firmwareVersion = device.firmwareVersion,
                    installationMode = device.installationMode,
                    isLightAutoEnabled = device.isLightAutoEnabled,
                    lightMode = device.lightMode,
                    lightPercentValue = device.lightPercent,
                    hasBrakeLight = device.hasBrakeLight
                )
            } ?: run {
                uiAction.emit(UIAction.NotifyErrorWhileRetrievingDevice)
            }
        }
    }
}
