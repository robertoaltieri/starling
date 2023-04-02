package com.altieri.starling

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController

@Composable
fun setTestNavController(): TestNavHostController {
    val hostController = TestNavHostController(LocalContext.current).apply {
        navigatorProvider.addNavigator(
            ComposeNavigator()
        )
    }
    return hostController
}
