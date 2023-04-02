package com.altieri.starling.ui.composable.deeplink

import androidx.navigation.NavType
import androidx.navigation.navArgument

object DeepLinks {
    const val BASE_URL = "https://www.altieri.org/cc1"

    object OnNewTokens {
        private const val TOKENS = "tokens"
        const val ARG_ACCESS_TOKEN = "access_token"
        const val ARG_REFRESH_TOKEN = "refresh_token"
        const val ARG_EXPIRES_IN = "expires_int"

        const val URI_PATTERN =
            "$BASE_URL/$TOKENS/{$ARG_ACCESS_TOKEN}/{$ARG_REFRESH_TOKEN}/{$ARG_EXPIRES_IN}"
        val ON_NEW_TOKENS_DEEP_LINK_ARGS = listOf(
            navArgument(ARG_ACCESS_TOKEN) { type = NavType.StringType },
            navArgument(ARG_REFRESH_TOKEN) { type = NavType.StringType },
            navArgument(ARG_EXPIRES_IN) { type = NavType.IntType }
        )
    }
}
