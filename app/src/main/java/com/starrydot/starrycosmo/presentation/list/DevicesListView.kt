package com.starrydot.starrycosmo.presentation.list

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
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
import com.starrydot.starrycosmo.presentation.design.card.InformationCard
import com.starrydot.starrycosmo.presentation.design.card.InformationSection
import com.starrydot.starrycosmo.presentation.design.color.ColorPalette
import com.starrydot.starrycosmo.presentation.design.font.FallingSky

@Composable
fun DevicesListView(
    modifier: Modifier = Modifier,
    viewModel: DevicesListViewModel = hiltViewModel(),
    onDeviceClick: (deviceMacAddress: String) -> Unit
) {
    val state = viewModel.observableState.collectAsState()

    //View
    DevicesListView(
        modifier = modifier,
        state = state.value,
        onDeviceClick = onDeviceClick
    )

    //Observe UIActions
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        viewModel.observableUIAction.collect { uiAction ->
            when (uiAction) {
                is UIAction.ShowNoConnectionError -> {
                    Toast.makeText(
                        context,
                        "Can't load more items while offline",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    })

    //Get data when View is launched
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getData()
    })
}

@Composable
fun DevicesListView(
    modifier: Modifier = Modifier,
    state: State,
    onDeviceClick: (deviceMacAddress: String) -> Unit
) {
    Surface(modifier = modifier.background(color = Color.White)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Start),
                text = "Devices",
                color = ColorPalette.Tertiary,
                fontSize = 36.sp,
                fontFamily = FallingSky,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            when (state) {
                is State.Loading -> {
                    LoadingView(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is State.Loaded -> {
                    DevicesView(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        onDeviceClick = onDeviceClick
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingView(modifier: Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(100.dp)
                .align(Alignment.Center),
            color = ColorPalette.Secondary,
            trackColor = ColorPalette.Tertiary
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun DevicesView(
    modifier: Modifier,
    state: State.Loaded,
    onDeviceClick: (deviceMacAddress: String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(state.listDevices) { index, listDevice ->
            if (index != 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }
            InformationCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple()
                    ) {
                        onDeviceClick.invoke(listDevice.macAddress)
                    },
                header = listDevice.category,
                sections = listOf(
                    InformationSection(
                        title = listDevice.model,
                        iconResId = R.drawable.ic_product_identifier
                    ),
                    InformationSection(
                        title = listDevice.macAddress,
                        iconResId = R.drawable.ic_mac_address
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun DevicesListView_Loading_Preview() {
    DevicesListView(
        modifier = Modifier.fillMaxSize(),
        state = State.Loading,
        onDeviceClick = {
            //Do nothing
        }
    )
}

@Preview
@Composable
fun DevicesListView_Preview() {
    DevicesListView(
        modifier = Modifier.fillMaxSize(),
        state = State.Loaded(
            listDevices = listOf(
                ListDevice(
                    category = "Ride",
                    model = "Ride Lite",
                    macAddress = "4921201e38d5"
                ),
                ListDevice(
                    category = "Ride",
                    model = "Ride Lite",
                    macAddress = "4921201e38d5"
                ),
                ListDevice(
                    category = "Ride",
                    model = "Ride Lite",
                    macAddress = "4921201e38d5"
                )
            )
        ),
        onDeviceClick = {
            //Do nothing
        }
    )
}
