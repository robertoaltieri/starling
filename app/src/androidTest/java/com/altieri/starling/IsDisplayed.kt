package com.altieri.starling

import androidx.compose.ui.test.SemanticsNodeInteractionCollection

fun SemanticsNodeInteractionCollection.isDisplayed() = fetchSemanticsNodes().size == 1
