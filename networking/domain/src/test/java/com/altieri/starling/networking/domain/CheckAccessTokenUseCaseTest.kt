package com.altieri.starling.networking.domain

import com.altieri.starling.datetime.bl.AppDateTime
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckAccessTokenUseCaseTest {

    @MockK
    private lateinit var storedToken: StoredToken

    @MockK
    private lateinit var dateTimeUtil: AppDateTime

    private lateinit var checkAccessTokenUseCase: CheckAccessTokenUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        checkAccessTokenUseCase = CheckAccessTokenUseCase(
            storedToken = storedToken,
            dateTimeUtil = dateTimeUtil
        )
    }

    @After
    fun tearDown() {
        confirmVerified(
            storedToken,
            dateTimeUtil
        )
    }

    @Test
    fun `GIVEN the access token is not set WHEN shouldRefresh THEN return false`() = runTest {
        // given
        every { storedToken.accessToken } returns null

        // when
        val ret = checkAccessTokenUseCase.shouldRefresh()

        // then
        verify { storedToken.accessToken }
        assertFalse(ret)
    }

    @Test
    fun `GIVEN the refresh token is not set WHEN shouldRefresh THEN return false`() = runTest {
        // given
        every { storedToken.accessToken } returns ACCESS_TOKEN
        every { storedToken.refreshToken } returns null

        // when
        val ret = checkAccessTokenUseCase.shouldRefresh()

        // then
        verify { storedToken.accessToken }
        verify { storedToken.refreshToken }
        assertFalse(ret)

    }

    @Test
    fun `GIVEN the access and refresh tokens are set and the current time is not after the creation + 70 percent of the expiresIn WHEN shouldRefresh THEN return false`() =
        runTest {
            // given
            every { storedToken.accessToken } returns ACCESS_TOKEN
            every { storedToken.refreshToken } returns REFRESH_TOKEN
            every { storedToken.expiryTime } returns CREATION_TIME + EXPIRES_IN
            every { storedToken.expiresIn } returns EXPIRES_IN
            every { dateTimeUtil.time() } returns CREATION_TIME + (EXPIRES_IN * 0.7).toLong()

            // when
            val ret = checkAccessTokenUseCase.shouldRefresh()

            // then
            verify { storedToken.accessToken }
            verify { storedToken.refreshToken }
            verify { storedToken.expiryTime }
            verify { storedToken.expiresIn }
            verify { dateTimeUtil.time() }

            assertFalse(ret)

        }

    @Test
    fun `GIVEN the access and refresh tokens are set and the current time is after the creation + 70 percent of the expiresIn WHEN shouldRefresh THEN return true`() =
        runTest {
            // given
            every { storedToken.accessToken } returns ACCESS_TOKEN
            every { storedToken.refreshToken } returns REFRESH_TOKEN
            every { storedToken.expiryTime } returns CREATION_TIME + EXPIRES_IN
            every { storedToken.expiresIn } returns EXPIRES_IN
            every { dateTimeUtil.time() } returns CREATION_TIME + (EXPIRES_IN * 0.7).toLong() + 1

            // when
            val ret = checkAccessTokenUseCase.shouldRefresh()

            // then
            verify { storedToken.accessToken }
            verify { storedToken.refreshToken }
            verify { storedToken.expiryTime }
            verify { storedToken.expiresIn }
            verify { dateTimeUtil.time() }

            assertTrue(ret)

        }

    private companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val CREATION_TIME = 1000L
        const val EXPIRES_IN = 1000
    }
}
