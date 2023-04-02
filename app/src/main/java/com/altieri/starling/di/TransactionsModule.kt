package com.altieri.starling.di

import com.altieri.starling.datetime.bl.AppDateTime
import com.altieri.starling.di.DIConst.TRANSACTIONS_STATE_FEED
import com.altieri.starling.networking.api.AppApiService
import com.altieri.starling.transactions.bl.RoundUpCalculator
import com.altieri.starling.transactions.bl.RoundUpUpdateUseCase
import com.altieri.starling.transactions.bl.TransactionsRepository
import com.altieri.starling.transactions.bl.TransactionsStateUI
import com.altieri.starling.transactions.data.TransactionDataToTransactionMapper
import com.altieri.starling.transactions.data.TransactionValidator
import com.altieri.starling.transactions.data.TransactionsNetworkDataSource
import com.altieri.starling.transactions.data.TransactionsRepositoryImpl
import com.altieri.starling.transactions.framework.RawTransactionMapper
import com.altieri.starling.transactions.framework.TransactionsApiService
import com.altieri.starling.transactions.framework.TransactionsNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TransactionsModule {
    @Provides
    @Named(TRANSACTIONS_STATE_FEED)
    fun providesTransactionsStateFeed(
    ): MutableStateFlow<TransactionsStateUI> =
        MutableStateFlow(TransactionsStateUI.Idle)

    @Provides
    @Singleton
    fun providesRoundUpUpdateUseCase(
        transactionsRepository: TransactionsRepository,
        dateTime: AppDateTime
    ): RoundUpUpdateUseCase =
        RoundUpUpdateUseCase(
            transactionsRepository = transactionsRepository,
            roundUpCalculator = RoundUpCalculator(),
            dateTime = dateTime
        )

    @Provides
    @Singleton
    fun providesTransactionsNetworkDataSource(
        apiService: TransactionsApiService
    ): TransactionsNetworkDataSource =
        TransactionsNetworkDataSourceImpl(
            apiService = apiService,
            rawTransactionMapper = RawTransactionMapper()
        )

    @Provides
    @Singleton
    fun providesTransactionsRepository(
        transactionsNetworkDataSource: TransactionsNetworkDataSource,
    ): TransactionsRepository =
        TransactionsRepositoryImpl(
            transactionsNetworkDataSource = transactionsNetworkDataSource,
            mapTransaction = TransactionDataToTransactionMapper(),
            isTransactionValid = TransactionValidator()
        )

}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindTransactionsModule {
    @Binds
    abstract fun bindsTransactionsApiService(
        appApiService: AppApiService
    ): TransactionsApiService
}
