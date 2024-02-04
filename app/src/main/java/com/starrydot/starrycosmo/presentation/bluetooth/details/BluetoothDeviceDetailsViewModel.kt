package com.starrydot.starrycosmo.presentation.bluetooth.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starrydot.starrycosmo.domain.device.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DeviceService(val uuid: String, val characteristics: List<DeviceCharacteristic>)
data class DeviceCharacteristic(val uuid: String, val type: String?)

sealed class State {
    object Loading : State()

    data class Loaded(
        val name: String?,
        val macAddress: String,
        val isBound: Boolean,
        val deviceServices: List<DeviceService>
    ) : State()
}

sealed class UIAction {
    object NotifyErrorWhileRetrievingDevice : UIAction()
}

@HiltViewModel
class BluetoothDeviceDetailsViewModel @Inject constructor(private val deviceRepository: DeviceRepository) :
    ViewModel() {

    private val state = MutableStateFlow<State>(State.Loading)
    val observableState: StateFlow<State> = state

    private val uiAction = MutableSharedFlow<UIAction>(replay = 0)
    val observableUIAction: SharedFlow<UIAction> = uiAction

    fun getBluetoothDeviceDetails(deviceMacAddress: String) {
        viewModelScope.launch {
            //Get device details or display an error if no device could be found
            deviceRepository.getBluetoothDeviceDetails(deviceMacAddress)
                ?.let { bluetoothDeviceDetails ->
                    state.value = State.Loaded(
                        name = bluetoothDeviceDetails.name,
                        macAddress = bluetoothDeviceDetails.macAddress,
                        isBound = bluetoothDeviceDetails.isBound,
                        deviceServices = bluetoothDeviceDetails.services.map { bluetoothDeviceService ->
                            DeviceService(
                                uuid = bluetoothDeviceService.uuid,
                                characteristics = bluetoothDeviceService.characteristics.map { bluetoothDeviceCharacteristic ->
                                    DeviceCharacteristic(
                                        uuid = bluetoothDeviceCharacteristic.uuid,
                                        type = bluetoothDeviceCharacteristic.type
                                    )
                                }
                            )
                        }
                    )
                } ?: run {
                uiAction.emit(UIAction.NotifyErrorWhileRetrievingDevice)
            }
        }
    }
}
