package com.altieri.starling.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.altieri.starling.R
import com.altieri.starling.ui.composable.deeplink.DeepLinks
import com.altieri.starling.ui.composable.event.OnNewToken

@Composable
fun TokenUpdatesScreen(
    backStackEntry: NavBackStackEntry,
    onNewTokens: OnNewToken
) {
    val accessToken = backStackEntry.arguments?.getString(DeepLinks.OnNewTokens.ARG_ACCESS_TOKEN)
    val refreshToken = backStackEntry.arguments?.getString(DeepLinks.OnNewTokens.ARG_REFRESH_TOKEN)
    val expiresIn = backStackEntry.arguments?.getInt(DeepLinks.OnNewTokens.ARG_EXPIRES_IN)
    onNewTokens(
        accessToken = accessToken.orEmpty(),
        refreshToken = refreshToken.orEmpty(),
        expiresIn = expiresIn ?: Int.MAX_VALUE
    )
    AppText(text = stringResource(id = R.string.tokens_updated))
}
