package com.starrydot.starrycosmo.presentation.bluetooth.list

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.starrydot.starrycosmo.R
import com.starrydot.starrycosmo.design.button.SecondaryButton
import com.starrydot.starrycosmo.design.card.InformationCard
import com.starrydot.starrycosmo.design.card.InformationSection
import com.starrydot.starrycosmo.design.color.ColorPalette
import com.starrydot.starrycosmo.design.font.FallingSky

val PERMISSIONS = arrayOf(
    android.Manifest.permission.BLUETOOTH,
    android.Manifest.permission.BLUETOOTH_ADMIN,
    android.Manifest.permission.BLUETOOTH_CONNECT,
    android.Manifest.permission.BLUETOOTH_SCAN,
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
fun BluetoothDevicesListView(
    modifier: Modifier = Modifier,
    viewModel: BluetoothDevicesListViewModel = hiltViewModel(),
    onDeviceClick: (deviceMacAddress: String) -> Unit
) {
    val state = viewModel.observableState.collectAsState()

    //Handle search View display
    var canDisplaySearchView by remember {
        mutableStateOf<Boolean?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsGranted ->
        canDisplaySearchView =
            permissionsGranted.isNotEmpty() && permissionsGranted.all { entry -> entry.value }
    }

    //Display View only if all permissions have been granted (location & Bluetooth)
    if (canDisplaySearchView == true) {
        BluetoothDevicesListView(
            modifier = modifier,
            state = state.value,
            onDeviceClick = onDeviceClick
        )

        //Search for devices when View is launched and if Bluetooth & location permissions have been given
        LaunchedEffect(key1 = Unit, block = {
            viewModel.searchForDevices()
        })
    } else if (canDisplaySearchView == false) {
        PermissionsView(
            modifier = modifier
        )
    } else {
        LoadingView()
    }

    //Check for permissions when going back in case the user gave authorizations from settings
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                launcher.launch(PERMISSIONS)
            }
            else -> {}
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
fun PermissionsView(modifier: Modifier) {
    val context = LocalContext.current
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .padding(20.dp)
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.bluetooth_devices_list_need_permissions),
                color = ColorPalette.Tertiary,
                fontSize = 22.sp,
                fontFamily = FallingSky,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(40.dp))
            SecondaryButton(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally),
                title = stringResource(id = R.string.bluetooth_devices_list_grant_permissions),
                iconResId = R.drawable.ic_next,
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.setData(uri)
                    startActivity(context, intent, null)
                }
            )
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun BluetoothDevicesListView(
    modifier: Modifier = Modifier,
    state: State,
    onDeviceClick: (deviceMacAddress: String) -> Unit
) {
    Surface(modifier = modifier.background(color = Color.White)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterVertically),
                    text = stringResource(id = R.string.bluetooth_devices_list_title),
                    color = ColorPalette.Tertiary,
                    fontSize = 36.sp,
                    fontFamily = FallingSky,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(40.dp)
                        .align(Alignment.CenterVertically),
                    color = ColorPalette.Secondary,
                    trackColor = ColorPalette.Tertiary
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            BluetoothDevicesView(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onDeviceClick = onDeviceClick
            )
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun BluetoothDevicesView(
    modifier: Modifier,
    state: State,
    onDeviceClick: (deviceMacAddress: String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(state.listBluetoothDevices) { index, listBluetoothDevice ->
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
                        onDeviceClick.invoke(listBluetoothDevice.macAddress)
                    },
                header = listBluetoothDevice.name
                    ?: stringResource(id = R.string.bluetooth_devices_list_unknown),
                sections = mutableListOf<InformationSection>().apply {
                    add(
                        InformationSection(
                            title = listBluetoothDevice.macAddress,
                            iconResId = R.drawable.ic_mac_address
                        )
                    )
                    add(
                        InformationSection(
                            title = stringResource(id = R.string.bluetooth_devices_list_bound) + " : " + if (listBluetoothDevice.isBound) stringResource(
                                id = R.string.bluetooth_devices_list_yes
                            ) else stringResource(id = R.string.bluetooth_devices_list_no),
                            iconResId = R.drawable.ic_bond
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun BluetoothDevicesListView_Preview() {
    BluetoothDevicesListView(
        modifier = Modifier.fillMaxSize(),
        state = State(
            listBluetoothDevices = listOf(
                ListBluetoothDevice(
                    name = "Pixel 6A",
                    macAddress = "4921201e38d5",
                    isBound = true
                ),
                ListBluetoothDevice(
                    name = "iPhone 15 Pro Max",
                    macAddress = "677aa677",
                    isBound = false
                ),
            )
        ),
        onDeviceClick = {
            //Do nothing
        }
    )
}
