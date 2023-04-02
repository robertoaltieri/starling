package com.altieri.starling.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

sealed interface Destinations {
    val route: String

    object Token : Destinations {
        override val route: String = "Token"
    }

    object Account : Destinations {
        override val route: String = "Account"
    }

    object SavingGoal : Destinations {
        override val route: String = "SavingGoal"
    }
}

fun NavGraphBuilder.composable(
    destination: Destinations,
    composableBlock: @Composable (NavBackStackEntry) -> Unit
) {
   composable(destination.route) {
        composableBlock(it)
    }
}
