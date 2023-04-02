package com.altieri.starling.networking.framework

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.altieri.starling.datetime.bl.AppDateTime
import com.altieri.starling.networking.domain.AuthToken
import com.altieri.starling.networking.domain.StoredToken
import com.altieri.starling.networking.framework.StoredTokenImpl.Companion.AUTH_TOKENS_STORE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoredTokenImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataStore: DataStore<Preferences>
    private val Context.dataStore by preferencesDataStore(AUTH_TOKENS_STORE)
    private lateinit var storedToken: StoredToken
    private val dateTime = object: AppDateTime {
        override fun oneWeekAgo(): String = "UNUSED"
        override fun time(): Long = TIME
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        dataStore = context.dataStore
        storedToken = StoredTokenImpl(
            store = dataStore,
            dispatcher = Dispatchers.Main,
            dateTime = dateTime
        )
    }

    @After
    fun cleanup() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }

    @Test
    fun givenNewTokensAreStoredWhenFetchingTheTokensThenReturnTheCorrectOnesAndTheCorrectRefreshTime() {
        // given
        val authToken = AuthToken(TEST_ACCESS_TOKEN, REFRESH_TOKEN, EXPIRE_IN)
        storedToken.storeTokens(authToken)

        // when
        val accessToken = storedToken.accessToken
        val refreshToken = storedToken.refreshToken
        val refreshTime = storedToken.expiryTime
        val expiresIn = storedToken.expiresIn

        // then
        assertEquals(authToken.accessToken, accessToken)
        assertEquals(authToken.refreshToken, refreshToken)
        assertEquals(authToken.expiresIn, expiresIn)
        val expire = authToken.expiresIn.toLong()
        assertEquals(TIME + expire, refreshTime)
    }

    private companion object {
        const val TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val EXPIRE_IN = 123
        const val TIME = 12345L

    }
}
