package com.starrydot.starrycosmo.presentation.bluetooth.details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.presentation.design.card.InformationCard
import com.starrydot.starrycosmo.presentation.design.card.InformationSection
import com.starrydot.starrycosmo.presentation.design.color.ColorPalette
import com.starrydot.starrycosmo.presentation.design.font.FallingSky
import java.util.*

@Composable
fun BluetoothDeviceDetailsView(
    modifier: Modifier = Modifier,
    deviceMacAddress: String,
    viewModel: BluetoothDeviceDetailsViewModel = hiltViewModel(),
    onNeedToGoBack: () -> Unit
) {
    val state = viewModel.observableState.collectAsState()

    //View
    BluetoothDeviceDetailsView(modifier = modifier, state = state.value)

    //Observe UIActions
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        viewModel.observableUIAction.collect { uiAction ->
            when (uiAction) {
                is UIAction.NotifyErrorWhileRetrievingDevice -> {
                    //Notify through a Toast
                    Toast.makeText(
                        context,
                        context.getString(R.string.device_details_error_while_loading_details),
                        Toast.LENGTH_LONG
                    ).show()
                    //Since device don't exist, switch back to previous screen
                    onNeedToGoBack.invoke()
                }
            }
        }
    })

    //Get device details when View is launched
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getBluetoothDeviceDetails(deviceMacAddress)
    })
}

@Composable
fun BluetoothDeviceDetailsView(modifier: Modifier = Modifier, state: State) {
    Surface(modifier = modifier.background(color = Color.White)) {
        when (state) {
            is State.Loading -> {
                LoadingView()
            }

            is State.Loaded -> {
                ContentView(state = state)
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(100.dp)
                .align(Alignment.Center),
            color = ColorPalette.Secondary,
            trackColor = ColorPalette.Tertiary
        )
    }
}

@Composable
fun ContentView(state: State.Loaded) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Start),
            text = stringResource(id = R.string.device_details_title),
            color = ColorPalette.Tertiary,
            fontSize = 28.sp,
            fontFamily = FallingSky,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(40.dp))
        InformationCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            header = "Details",
            sections = mutableListOf<InformationSection>().apply {
                add(
                    InformationSection(
                        title = state.name ?: "Unknwon",
                        iconResId = R.drawable.ic_product_identifier,
                    )
                )
                add(
                    InformationSection(
                        title = state.macAddress,
                        iconResId = R.drawable.ic_mac_address,
                    )
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Start),
            text = "Services",
            color = ColorPalette.Tertiary,
            fontSize = 18.sp,
            fontFamily = FallingSky,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(20.dp))
        state.deviceServices.forEachIndexed { index, deviceService ->
            if (index != 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }
            InformationCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                header = deviceService.uuid,
                sections = deviceService.characteristics.map { deviceCharacteristic ->
                    InformationSection(
                        title = deviceCharacteristic.uuid,
                        iconResId = R.drawable.ic_feature
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun BluetoothDeviceDetailsView_Loading_Preview() {
    BluetoothDeviceDetailsView(
        modifier = Modifier.fillMaxSize(),
        state = State.Loading
    )
}

@Preview
@Composable
fun BluetoothDeviceDetailsView_Loaded_Preview() {
    BluetoothDeviceDetailsView(
        modifier = Modifier.fillMaxSize(),
        state = State.Loaded(
            name = "Pixel 6A",
            macAddress = "4921201e38d5",
            isBound = false,
            deviceServices = listOf(
                DeviceService(
                    uuid = UUID.randomUUID().toString(),
                    characteristics = listOf(
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString()),
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString()),
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString())
                    )
                ),
                DeviceService(
                    uuid = UUID.randomUUID().toString(),
                    characteristics = listOf(
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString()),
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString()),
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString())
                    )
                ),
                DeviceService(
                    uuid = UUID.randomUUID().toString(),
                    characteristics = listOf(
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString()),
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString()),
                        DeviceCharacteristic(uuid = UUID.randomUUID().toString())
                    )
                )
            )
        )
    )
}
