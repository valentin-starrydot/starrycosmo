package com.starrydot.starrycosmo.presentation.bluetooth.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starrydot.starrycosmo.domain.device.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ListBluetoothDevice(val name: String?, val macAddress: String, val isBound: Boolean)
data class State(val listBluetoothDevices: List<ListBluetoothDevice>)

@HiltViewModel
class BluetoothDevicesListViewModel @Inject constructor(private val deviceRepository: DeviceRepository) :
    ViewModel() {

    private val state = MutableStateFlow(State(listBluetoothDevices = listOf()))
    val observableState: StateFlow<State> = state

    fun searchForDevices() {
        viewModelScope.launch {
            //Get devices and map to an UI Model
            deviceRepository.searchForBluetoothDevices().collect { bluetoothDevices ->
                state.value = State(
                    listBluetoothDevices = bluetoothDevices.map { bluetoothDevice ->
                        ListBluetoothDevice(
                            name = bluetoothDevice.name,
                            macAddress = bluetoothDevice.macAddress,
                            isBound = bluetoothDevice.isBound
                        )
                    }
                )
            }
        }
    }
}
