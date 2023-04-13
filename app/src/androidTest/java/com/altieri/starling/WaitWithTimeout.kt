package com.altieri.starling

import androidx.compose.ui.test.junit4.ComposeContentTestRule

fun ComposeContentTestRule.waitWithTimeout(condition: () -> Boolean) {
    waitUntil(
        timeoutMillis = 5000,
        condition = condition
    )
}
