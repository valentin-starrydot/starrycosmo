package com.starrydot.starrycosmo.repository.device.api

import com.starrydot.starrycosmo.repository.device.api.model.DeviceApiResults
import retrofit2.http.GET

interface DeviceApiService {
    @GET("/test/devices")
    suspend fun getDevices(): DeviceApiResults
}
