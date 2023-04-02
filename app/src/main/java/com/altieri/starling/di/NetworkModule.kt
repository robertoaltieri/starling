package com.altieri.starling.di

import android.content.Context
import com.altieri.starling.BuildConfig
import com.altieri.starling.datetime.bl.AppDateTime
import com.altieri.starling.di.DIConst.APP_BASE_URL
import com.altieri.starling.di.DIConst.SINGLE_THREAD_DISPATCHER
import com.altieri.starling.di.DIConst.TOKEN_REPOSITORY_DISPATCHER
import com.altieri.starling.networking.framework.StoredTokenImpl.Companion.storedTokenDataStore
import com.altieri.starling.networking.domain.ENV
import com.altieri.starling.networking.domain.StoredToken
import com.altieri.starling.networking.api.AppApiService
import com.altieri.starling.networking.domain.CheckAccessTokenUseCase
import com.altieri.starling.networking.domain.RefreshTokenUseCase
import com.altieri.starling.networking.domain.TokenRepository
import com.altieri.starling.networking.framework.AuthTokenRawToAuthTokenMapper
import com.altieri.starling.networking.framework.DefaultHeadersInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.altieri.starling.networking.framework.EmptyInterceptor
import com.altieri.starling.networking.framework.RefreshTokenInterceptor
import com.altieri.starling.networking.framework.StoredTokenImpl
import com.altieri.starling.networking.framework.TokenAPIService
import com.altieri.starling.networking.framework.TokenRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named(RETROFIT_FOR_TOKEN)
    fun providesRetrofitForToken(
        okHttpBuilder: OkHttpClient.Builder,
        moshiConverterFactory: MoshiConverterFactory,
        @Named(LOGGING_INTERCEPTOR)
        httpLoggingInterceptor: Interceptor,
        @Named(APP_BASE_URL)
        baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(moshiConverterFactory)
        .client(
            okHttpBuilder
                .addInterceptor(httpLoggingInterceptor)
                .build()
        )
        .build()

    @Provides
    @Singleton
    fun providesTokenApiService(
        @Named(RETROFIT_FOR_TOKEN)
        retrofit: Retrofit
    ): TokenAPIService = retrofit.create(TokenAPIService::class.java)

    @Provides
    @Singleton
    fun providesAppApiService(
        retrofit: Retrofit
    ): AppApiService = retrofit.create(AppApiService::class.java)

    @Provides
    @Singleton
    fun providesOKHttpClientBuilder(
        defaultHeadersInterceptor: DefaultHeadersInterceptor
    ): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_CONNECT)
            .writeTimeout(TIMEOUT)
            .readTimeout(TIMEOUT)
            .addInterceptor(defaultHeadersInterceptor)

    @Provides
    fun providesRefreshTokenInterceptor(
        refreshTokenUseCase: RefreshTokenUseCase
    ) = RefreshTokenInterceptor(
        refreshTokenUseCase = refreshTokenUseCase
    )

    @Provides
    @Singleton
    fun providesOKHttpClient(
        builder: OkHttpClient.Builder,
        @Named(LOGGING_INTERCEPTOR)
        httpLoggingInterceptor: Interceptor,
        refreshTokenInterceptor: RefreshTokenInterceptor
    ): OkHttpClient = builder
        .addInterceptor(refreshTokenInterceptor)
        .addInterceptor(httpLoggingInterceptor) // keep it as last interceptor when you add more
        .build()

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providesMoshiConverter(
        moshi: Moshi
    ): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun providesRetrofitWithMoshi(
        client: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(ENV.PROD)
        .addConverterFactory(moshiConverterFactory)
        .client(client)
        .build()

    @Provides
    @Singleton
    @Named(LOGGING_INTERCEPTOR)
    fun providesHttpLoggingInterceptor(): Interceptor =
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            logging
        } else {
            EmptyInterceptor()
        }

    @Provides
    @Singleton
    fun providesDefaultHeadersInterceptor(
        storedToken: StoredToken
    ) = DefaultHeadersInterceptor(
        storedToken = storedToken
    )

    @Provides
    @Singleton
    fun providesStoredToken(
        @ApplicationContext
        context: Context,
        @Named(SINGLE_THREAD_DISPATCHER)
        dispatcher: CoroutineDispatcher,
        dateTimeUtil: AppDateTime
    ): StoredToken {
        return StoredTokenImpl(
            store = context.storedTokenDataStore,
            dispatcher = dispatcher,
            dateTime = dateTimeUtil
        )
    }

    @Provides
    fun providesTokenRepository(
        tokenAPIService: TokenAPIService,
        storedToken: StoredToken,
        checkAccessTokenUseCase: CheckAccessTokenUseCase,
        @Named(SINGLE_THREAD_DISPATCHER)
        dispatcher: CoroutineDispatcher
    ): TokenRepository = TokenRepositoryImpl(
        service = tokenAPIService,
        storedToken = storedToken,
        checkAccessTokenUseCase = checkAccessTokenUseCase,
        mapAuthToken = AuthTokenRawToAuthTokenMapper(),
        updatingTokens = false,
        dispatcher = dispatcher
    )

    @Provides
    fun providesCheckAccessTokenUseCase(
        storedToken: StoredToken,
        dateTimeUtil: AppDateTime
    ): CheckAccessTokenUseCase = CheckAccessTokenUseCase(
        storedToken = storedToken,
        dateTimeUtil = dateTimeUtil
    )

    @Provides
    fun providesRefreshTokenUseCase(
        storedToken: StoredToken,
        checkAccessTokenUseCase: CheckAccessTokenUseCase,
        tokenService: TokenRepository,
    ): RefreshTokenUseCase = RefreshTokenUseCase(
        storedToken = storedToken,
        checkAccessTokenUseCase = checkAccessTokenUseCase,
        tokenRepository = tokenService,
    )

    private const val LOGGING_INTERCEPTOR = "LOGGING_INTERCEPTOR"
    private const val RETROFIT_FOR_TOKEN = "RETROFIT_FOR_TOKEN"
    private val TIMEOUT = Duration.ofMillis(TimeUnit.MINUTES.toMillis(10))
    private val TIMEOUT_CONNECT = Duration.ofMillis(TimeUnit.SECONDS.toMillis(10))
}
