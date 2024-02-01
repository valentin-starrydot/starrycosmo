package com.starrydot.starrycosmo.presentation.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed class State {
    object Loading : State()
    data class Loaded(
        val category: String,
        val model: String,
        val macAddress: String,
        val serial: String?,
        val firmwareVersion: String,
        val installationMode: String,
        val isLightAutoEnabled: Boolean,
        val lightMode: String,
        val lightPercentValue: Int,
        val hasBrakeLight: Boolean
    ) : State()
}

sealed class UIAction {
    object ErrorWhileLoading: UIAction()
}

@HiltViewModel
class DeviceDetailsViewModel @Inject constructor() : ViewModel() {
    private val state = MutableStateFlow<State>(State.Loading)
    val observableState: StateFlow<State> = state

    private val uiAction = MutableSharedFlow<UIAction>(replay = 0)
    val observableUIAction: SharedFlow<UIAction> = uiAction

    fun getDeviceDetails(deviceMacAddress: String) {
        //TODO -> Implement getDeviceDetails with later repository implementation
    }
}
