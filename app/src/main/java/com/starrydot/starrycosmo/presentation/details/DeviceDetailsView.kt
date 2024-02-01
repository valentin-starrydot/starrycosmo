package com.starrydot.starrycosmo.presentation.details

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.domain.device.model.DeviceCategory
import com.starrydot.starrycosmo.domain.device.model.DeviceInstallationMode
import com.starrydot.starrycosmo.domain.device.model.DeviceLightMode
import com.starrydot.starrycosmo.domain.device.model.DeviceModel
import com.starrydot.starrycosmo.presentation.design.card.InformationCard
import com.starrydot.starrycosmo.presentation.design.card.InformationSection
import com.starrydot.starrycosmo.presentation.design.color.ColorPalette
import com.starrydot.starrycosmo.presentation.design.description.toStringDescription
import com.starrydot.starrycosmo.presentation.design.font.FallingSky

@Composable
fun DeviceDetailsView(
    modifier: Modifier = Modifier,
    deviceMacAddress: String,
    viewModel: DeviceDetailsViewModel = hiltViewModel(),
    onNeedToGoBack: () -> Unit
) {
    val state = viewModel.observableState.collectAsState()

    //View
    DeviceDetailsView(modifier = modifier, state = state.value)

    //Observe UIActions
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        viewModel.observableUIAction.collect { uiAction ->
            when (uiAction) {
                is UIAction.ErrorWhileLoading -> {
                    //Notify through a Toast
                    Toast.makeText(
                        context,
                        "Error while loading device details",
                        Toast.LENGTH_LONG
                    ).show()
                    //Since contact don't exist, switch back to previous screen
                    onNeedToGoBack.invoke()
                }
            }
        }
    })

    //Get contact details when View is launched
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getDeviceDetails(deviceMacAddress)
    })
}

@Composable
fun DeviceDetailsView(modifier: Modifier = Modifier, state: State) {
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
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Start),
            text = "Device details",
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
            header = "${state.category.toStringDescription()}${state.model?.let { model -> " | ${model.toStringDescription()}" } ?: ""}",
            sections = mutableListOf<InformationSection>().apply {
                add(
                    InformationSection(
                        title = state.macAddress,
                        iconResId = R.drawable.ic_mac_address,
                    )
                )
                state.serial?.let { serial ->
                    add(
                        InformationSection(
                            title = serial,
                            iconResId = R.drawable.ic_product_identifier
                        )
                    )
                }
                add(
                    InformationSection(
                        title = "Firmware : ${state.firmwareVersion}",
                        iconResId = R.drawable.ic_microchip
                    )
                )
                add(
                    InformationSection(
                        title = "${state.lightMode?.let { lightMode -> "Mode ${lightMode.toStringDescription()} | " } ?: ""} ${state.lightPercentValue}% | Auto ${if (state.isLightAutoEnabled) "On" else "Off"}",
                        iconResId = R.drawable.ic_light
                    )
                )
                add(
                    InformationSection(
                        title = "Brake Light : ${if (state.hasBrakeLight) "On" else "Off"}",
                        iconResId = R.drawable.ic_brake
                    )
                )
                state.installationMode?.let { installationMode ->
                    add(
                        InformationSection(
                            title = "Installation mode : ${installationMode.toStringDescription()}",
                            iconResId = R.drawable.ic_position
                        )
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun DeviceDetailsView_Loading_Preview() {
    DeviceDetailsView(
        modifier = Modifier.fillMaxSize(),
        state = State.Loading
    )
}

@Preview
@Composable
fun DeviceDetailsView_Loaded_Preview() {
    DeviceDetailsView(
        modifier = Modifier.fillMaxSize(),
        state = State.Loaded(
            category = DeviceCategory.RIDE,
            model = DeviceModel.RIDE_LITE,
            macAddress = "4921201e38d5",
            serial = "BC892C9C-047D-8402-A9FD-7B2CC0048736",
            firmwareVersion = "2.2.2",
            installationMode = DeviceInstallationMode.HELMET,
            isLightAutoEnabled = false,
            lightMode = DeviceLightMode.OFF,
            lightPercentValue = 0,
            hasBrakeLight = false
        )
    )
}
