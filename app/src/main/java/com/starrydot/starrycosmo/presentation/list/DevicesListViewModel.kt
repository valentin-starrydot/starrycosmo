package com.starrydot.starrycosmo.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.domain.device.model.DeviceCategory
import com.starrydot.starrycosmo.domain.device.model.DeviceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ListDevice(val category: DeviceCategory, val model: DeviceModel?, val macAddress: String)
sealed class State {
    object Loading : State()

    data class Loaded(val listDevices: List<ListDevice>) : State()
}

sealed class UIAction {
    object ShowNoConnectionError : UIAction()
}

@HiltViewModel
class DevicesListViewModel @Inject constructor(private val deviceRepository: DeviceRepository) :
    ViewModel() {

    private val state = MutableStateFlow<State>(State.Loading)
    val observableState: StateFlow<State> = state

    private val uiAction = MutableSharedFlow<UIAction>(replay = 0)
    val observableUIAction: SharedFlow<UIAction> = uiAction

    fun getData() {
        viewModelScope.launch {
            try {
                //Get devices and map to UI Model
                val devices = deviceRepository.getDevices()
                state.value = State.Loaded(
                    listDevices = devices.map { device ->
                        ListDevice(
                            category = device.category,
                            model = device.model,
                            macAddress = device.macAddress
                        )
                    }
                )
            } catch (exception: Exception) {
                uiAction.emit(UIAction.ShowNoConnectionError)
                //If we have an Internet error, retry until it succeed
                if (state.value == State.Loading) {
                    delay(5000)
                    getData()
                }
            }
        }
    }
}
