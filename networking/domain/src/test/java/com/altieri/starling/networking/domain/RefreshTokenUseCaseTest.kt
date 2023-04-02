package com.altieri.starling.networking.domain

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class RefreshTokenUseCaseTest {

    @MockK
    private lateinit var storedToken: StoredToken

    @MockK
    private lateinit var checkAccessTokenUseCase: CheckAccessTokenUseCase

    @MockK
    private lateinit var tokenRepository: TokenRepository

    private lateinit var refreshTokenUseCase: RefreshTokenUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        refreshTokenUseCase = RefreshTokenUseCase(
            storedToken = storedToken,
            checkAccessTokenUseCase = checkAccessTokenUseCase,
            tokenRepository = tokenRepository
        )
    }

    @After
    fun tearDown() {
        confirmVerified(
            storedToken,
            checkAccessTokenUseCase,
            tokenRepository
        )
    }

    @Test
    fun `GIVEN token does not need to be refreshed WHEN refreshTokenUseCase is called THEN do nothing`() {
        // given
        every { checkAccessTokenUseCase.shouldRefresh() } returns false

        // when
        refreshTokenUseCase()

        // then
        verify { checkAccessTokenUseCase.shouldRefresh() }
    }

    @Test
    fun `GIVEN token needs to be refreshed WHEN refreshTokenUseCase is called THEN do nothing`() {
        // given
        every { checkAccessTokenUseCase.shouldRefresh() } returns true
        every { storedToken.refreshToken } returns REFRESH_TOKEN
        coEvery {
            tokenRepository.refreshToken(
                REFRESH_TOKEN
            )
        } answers {}

        // when
        refreshTokenUseCase()

        // then
        verify { checkAccessTokenUseCase.shouldRefresh() }
        verify { storedToken.refreshToken }
        coVerify {
            tokenRepository.refreshToken(
                REFRESH_TOKEN
            )
        }
    }

    private companion object {
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }
}
