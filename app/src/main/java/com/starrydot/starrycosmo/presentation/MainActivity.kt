package com.starrydot.starrycosmo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import com.starrydot.starrycosmo.presentation.navigation.MainNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Configure Status Bar colors
        window?.apply {
            statusBarColor = Color.White.toArgb()
            val windowInsetsController = WindowInsetsControllerCompat(this, decorView)
            windowInsetsController.isAppearanceLightStatusBars = true
        }

        //Define View
        setContent {
            MainNavigationView(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
