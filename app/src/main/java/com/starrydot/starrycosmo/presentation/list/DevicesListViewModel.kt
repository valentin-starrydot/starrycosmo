package com.starrydot.starrycosmo.presentation.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

data class ListDevice(val category: String, val model: String, val macAddress: String)
sealed class State {
    object Loading: State()

    data class Loaded(val listDevices: List<ListDevice>): State()
}

sealed class UIAction {
    object ShowNoConnectionError: UIAction()
}

@HiltViewModel
class DevicesListViewModel @Inject constructor() :
    ViewModel() {

    private val state = MutableStateFlow<State>(State.Loading)
    val observableState: StateFlow<State> = state

    private val uiAction = MutableSharedFlow<UIAction>(replay = 0)
    val observableUIAction: SharedFlow<UIAction> = uiAction

    fun getData() {
        //TODO -> Implement getData with later repository implementation
    }
}
