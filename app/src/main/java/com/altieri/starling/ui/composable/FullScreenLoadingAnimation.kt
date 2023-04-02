package com.altieri.starling.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun FullScreenLoadingAnimation(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag(
                    TestTags.FullScreenLoadingAnimation
                )
        ) {
            AppCircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
