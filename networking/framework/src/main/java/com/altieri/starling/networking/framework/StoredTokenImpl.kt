package com.altieri.starling.networking.framework

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.altieri.starling.datetime.bl.AppDateTime
import com.altieri.starling.networking.domain.AuthToken
import com.altieri.starling.networking.domain.StoredToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// I'm using blocking code because I need to retrieve and store the tokens synchronously in the interceptors
class StoredTokenImpl(
    private val store: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
    private val dateTime: AppDateTime
) : StoredToken {
    override fun storeTokens(authToken: AuthToken) {
        // in other applications I read the expiry time from the JWT. Here I'm using the expiresIn field
        // although I don't know why the API is returning that value instead of the expiry time that would
        // be clearer

        runBlocking(dispatcher) {
            cachedAccessToken = authToken.accessToken
            cachedRefreshToken = authToken.refreshToken
            val (expiresIn, expiryTime) = if (authToken.accessToken.isEmpty()) {
                Int.MAX_VALUE to Long.MAX_VALUE
            } else {
                authToken.expiresIn to dateTime.time() + authToken.expiresIn
            }
            cachedExpiryTime = expiryTime
            cachedExpiresIn = expiresIn
            store.edit {
                it[ACCESS_TOKEN] = authToken.accessToken
                it[ACCESS_TOKEN_EXP] = expiryTime
                it[ACCESS_TOKEN_EXPIRES_IN] = expiresIn
                it[REFRESH_TOKEN] = authToken.refreshToken
            }
        }
    }

    // even if now it isn't possible for accessToken to be null it's only because I'm using the value from the property file
    // this property should be nullable
    override val accessToken: String?
        get() = runBlocking(dispatcher) {
            cachedAccessToken ?: (fetchPreference(ACCESS_TOKEN)
                ?: BuildConfig.STARLING_ACCESS_TOKEN).also {
                cachedAccessToken = it
            }
        }

    // even if now it isn't possible for refreshToken to be null it's only because I'm using the value from the property file
    // this property should be nullable
    override val refreshToken: String?
        get() = runBlocking(dispatcher) {
            cachedRefreshToken ?: (fetchPreference(REFRESH_TOKEN)
                ?: BuildConfig.STARLING_REFRESH_TOKEN).also {
                cachedRefreshToken = it
            }
        }

    override val expiryTime: Long
        get() = runBlocking(dispatcher) {
            cachedExpiryTime ?: (fetchPreference(ACCESS_TOKEN_EXP) ?: IN_THE_PAST).also {
                cachedExpiryTime = it
            }
        }

    override val expiresIn: Int
        get() = runBlocking(dispatcher) {
            cachedExpiresIn ?: (fetchPreference(ACCESS_TOKEN_EXPIRES_IN) ?: Int.MAX_VALUE).also {
                cachedExpiresIn = it
            }
        }

    private var cachedAccessToken: String? = null
    private var cachedRefreshToken: String? = null
    private var cachedExpiryTime: Long? = null
    private var cachedExpiresIn: Int? = null

    private suspend fun <E> fetchPreference(key: Preferences.Key<E>): E? =
        store.data.map { preferences ->
            preferences[key]
        }.firstOrNull()

    companion object {
        private const val IN_THE_PAST = 0L
        const val AUTH_TOKENS_STORE = "auth_tokens_store"
        private val ACCESS_TOKEN = stringPreferencesKey("auth_token")
        private val ACCESS_TOKEN_EXP = longPreferencesKey("access_token_exp")
        private val ACCESS_TOKEN_EXPIRES_IN = intPreferencesKey("access_token_expires_in")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val Context.storedTokenDataStore by preferencesDataStore(AUTH_TOKENS_STORE)
    }

}
