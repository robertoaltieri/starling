package com.altieri.starling.networking.framework

import com.altieri.starling.coretesting.AppTestCoroutineRule
import com.altieri.starling.networking.domain.AuthToken
import com.altieri.starling.networking.domain.CheckAccessTokenUseCase
import com.altieri.starling.networking.domain.StoredToken
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TokenRepositoryImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val rule = AppTestCoroutineRule(testDispatcher)

    @MockK
    private lateinit var service: TokenAPIService

    @MockK
    private lateinit var storedToken: StoredToken

    @MockK
    private lateinit var checkAccessTokenUseCase: CheckAccessTokenUseCase

    @MockK
    private lateinit var mapAuthToken: AuthTokenRawToAuthTokenMapper

    private lateinit var tokenRepository: TokenRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        confirmVerified(
            service,
            storedToken,
            checkAccessTokenUseCase,
            mapAuthToken
        )
    }

    @Test
    fun `GIVEN the token needs to be refreshed WHEN refreshToken THEN refresh and store the new tokens`() = runTest {
        // given
        tokenRepository = TokenRepositoryImpl(
            service = service,
            storedToken = storedToken,
            checkAccessTokenUseCase = checkAccessTokenUseCase,
            mapAuthToken = mapAuthToken,
            updatingTokens = false,
            dispatcher = testDispatcher
        )
        every { checkAccessTokenUseCase.shouldRefresh() } returns true
        coEvery {
            service.refreshToken(
                refreshToken = REFRESH_TOKEN,
                clientId = BuildConfig.STARLING_CLIENT_ID,
                clientSecret = BuildConfig.STARLING_CLIENT_SECRET,
                grantType = GrantType.REFRESH_TOKEN
            )
        } returns AUTH_TOKEN_RAW
        coEvery {
            storedToken.storeTokens(AUTH_TOKEN)
        } answers {}
        coEvery {
            mapAuthToken(AUTH_TOKEN_RAW)
        } returns AUTH_TOKEN

        // when
        tokenRepository.refreshToken(
            REFRESH_TOKEN
        )

        // then
        verify { checkAccessTokenUseCase.shouldRefresh() }
        coVerify {
            service.refreshToken(
                refreshToken = REFRESH_TOKEN,
                clientId = BuildConfig.STARLING_CLIENT_ID,
                clientSecret = BuildConfig.STARLING_CLIENT_SECRET,
                grantType = GrantType.REFRESH_TOKEN
            )
        }
        coVerify {
            storedToken.storeTokens(AUTH_TOKEN)
        }
        coVerify {
            mapAuthToken(AUTH_TOKEN_RAW)
        }
    }

    @Test
    fun `GIVEN the token needs not to be refreshed WHEN refreshToken THEN do nothing`() = runTest {
        // given
        tokenRepository = TokenRepositoryImpl(
            service = service,
            storedToken = storedToken,
            checkAccessTokenUseCase = checkAccessTokenUseCase,
            mapAuthToken = mapAuthToken,
            updatingTokens = false,
            dispatcher = testDispatcher
        )
        every { checkAccessTokenUseCase.shouldRefresh() } returns false

        // when
        tokenRepository.refreshToken(
            REFRESH_TOKEN
        )

        // then
        verify { checkAccessTokenUseCase.shouldRefresh() }
    }

    @Test
    fun `GIVEN already updating WHEN refreshToken THEN do nothing`() = runTest {
        // given
        tokenRepository = TokenRepositoryImpl(
            service = service,
            storedToken = storedToken,
            checkAccessTokenUseCase = checkAccessTokenUseCase,
            mapAuthToken = mapAuthToken,
            updatingTokens = true,
            dispatcher = testDispatcher
        )

        // when
        tokenRepository.refreshToken(
            REFRESH_TOKEN
        )

    }

    @Test
    fun `GIVEN already updating WHEN onNewTokens THEN do nothing`() = runTest {
        // given
        tokenRepository = TokenRepositoryImpl(
            service = service,
            storedToken = storedToken,
            checkAccessTokenUseCase = checkAccessTokenUseCase,
            mapAuthToken = mapAuthToken,
            updatingTokens = true,
            dispatcher = testDispatcher
        )

        // when
        tokenRepository.onNewTokens(
            AUTH_TOKEN
        )

    }

    @Test
    fun `WHEN onNewTokens THEN forward to the StoredToken`() = runTest {
        // given
        tokenRepository = TokenRepositoryImpl(
            service = service,
            storedToken = storedToken,
            checkAccessTokenUseCase = checkAccessTokenUseCase,
            mapAuthToken = mapAuthToken,
            updatingTokens = false,
            dispatcher = testDispatcher
        )
        every { storedToken.storeTokens(AUTH_TOKEN) } answers {}

        // when
        tokenRepository.onNewTokens(
            AUTH_TOKEN
        )

        // then
        verify { storedToken.storeTokens(AUTH_TOKEN) }
    }

    private companion object {
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val EXPIRES_IN = 123
        val AUTH_TOKEN_RAW = AuthTokenRaw(
            access_token = ACCESS_TOKEN,
            refresh_token = REFRESH_TOKEN,
            expires_in = EXPIRES_IN
        )
        val AUTH_TOKEN = AuthToken(
            accessToken = ACCESS_TOKEN,
            refreshToken = REFRESH_TOKEN,
            expiresIn = EXPIRES_IN
        )
    }
}
