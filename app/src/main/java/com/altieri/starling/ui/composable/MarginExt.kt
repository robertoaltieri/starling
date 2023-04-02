package com.altieri.starling.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

fun Modifier.appPadding(dp: Dp = AppMargin.default) = padding(dp)

fun Modifier.paddingStart(dp: Dp = AppMargin.default) = padding(start = dp)

