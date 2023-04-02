package com.altieri.starling.ui.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

object AppColor {
    val text
        @Composable
        get() = MaterialTheme.colorScheme.onBackground

    val buttonTextEnabled
        @Composable
        get() = MaterialTheme.colorScheme.onPrimary

    val buttonTextDisabled
        @Composable
        get() = MaterialTheme.colorScheme.onBackground
}
