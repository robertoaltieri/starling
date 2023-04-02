package com.altieri.starling.di

import com.altieri.starling.account.bl.AccountRepository
import com.altieri.starling.account.bl.AddMoneyToSavingGoalUseCase
import com.altieri.starling.account.bl.OnNewTokensUseCase
import com.altieri.starling.account.bl.SavingGoalFilter
import com.altieri.starling.account.bl.SetupSavingGoalUseCase
import com.altieri.starling.account.bl.UpdateAccountDetailsUseCase
import com.altieri.starling.account.data.AccountsLocalDataSource
import com.altieri.starling.account.data.AccountDataToAccountMapper
import com.altieri.starling.account.data.AccountsNetworkDataSource
import com.altieri.starling.account.data.AccountRepositoryImpl
import com.altieri.starling.account.data.AccountValidator
import com.altieri.starling.account.data.SavingGoalDataToSavingGoalMapper
import com.altieri.starling.account.data.SavingGoalValidator
import com.altieri.starling.account.framework.AccountEntityToAccountDataMapper
import com.altieri.starling.account.framework.AccountsApiService
import com.altieri.starling.account.framework.AccountsLocalDataSourceImpl
import com.altieri.starling.account.framework.AccountsNetworkDataSourceImpl
import com.altieri.starling.account.framework.AccountRawToAccountDataMapper
import com.altieri.starling.account.framework.AccountToAccountEntityMapper
import com.altieri.starling.account.framework.SavingGoalRawToSavingGoalDataMapper
import com.altieri.starling.account.presentation.AccountsStateUI
import com.altieri.starling.account.presentation.SavingGoalStateUI
import com.altieri.starling.common.bl.AppUUIDGenerator
import com.altieri.starling.common.bl.currency.CurrencyInfoHelper
import com.altieri.starling.database.framework.AppDatabase
import com.altieri.starling.di.DIConst.ACCOUNTS_GOAL_STATE_FEED
import com.altieri.starling.di.DIConst.ACCOUNTS_STATE_FEED
import com.altieri.starling.networking.api.AppApiService
import com.altieri.starling.networking.domain.TokenRepository
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
object AccountsModule {
    @Provides
    @Named(ACCOUNTS_STATE_FEED)
    fun providesAccountsStateFeed(
    ): MutableStateFlow<AccountsStateUI> =
        MutableStateFlow(AccountsStateUI.Idle)

    @Provides
    @Named(ACCOUNTS_GOAL_STATE_FEED)
    fun providesSavingGoalStateUIFeed(
    ): MutableStateFlow<SavingGoalStateUI> =
        MutableStateFlow(SavingGoalStateUI.Idle)

    @Provides
    @Singleton
    fun providesAccountDataToAccountMapper(): AccountDataToAccountMapper =
        AccountDataToAccountMapper(
            currencyInfoHelper = CurrencyInfoHelper()
        )

    @Provides
    @Singleton
    fun providesAccountRepository(
        accountsNetworkDataSource: AccountsNetworkDataSource,
        accountsLocalDataSource: AccountsLocalDataSource,
        accountDataToAccountMapper: AccountDataToAccountMapper
    ): AccountRepository =
        AccountRepositoryImpl(
            accountsNetworkDataSource = accountsNetworkDataSource,
            accountsLocalDataSource = accountsLocalDataSource,
            validateAccount = AccountValidator(),
            validateSavingGoal = SavingGoalValidator(),
            mapAccount = accountDataToAccountMapper,
            mapSavingGoal = SavingGoalDataToSavingGoalMapper(),
            uuidGenerator = AppUUIDGenerator(),
            cachedAccount = null
    )

    @Provides
    @Singleton
    fun providesAccountLocalDataSource(
        database: AppDatabase
    ): AccountsLocalDataSource =
        AccountsLocalDataSourceImpl(
            accountDao = database.accountDao(),
            toAccountDataMapper = AccountEntityToAccountDataMapper(),
            toAccountDBEntityMapper = AccountToAccountEntityMapper(),
        )

    @Provides
    @Singleton
    fun providesAccountNetworkDataSource(
        apiService: AccountsApiService,
    ): AccountsNetworkDataSource =
        AccountsNetworkDataSourceImpl(
            apiService = apiService,
            mapAccount = AccountRawToAccountDataMapper(),
            mapSavingGoal = SavingGoalRawToSavingGoalDataMapper()
        )

    @Provides
    @Singleton
    fun providesUpdateAccountDetailsUseCase(
        repository: AccountRepository
    ): UpdateAccountDetailsUseCase =
        UpdateAccountDetailsUseCase(repository)

    @Provides
    @Singleton
    fun providesSetupSpendingGoalUseCase(
        repository: AccountRepository
    ): SetupSavingGoalUseCase =
        SetupSavingGoalUseCase(
            repository = repository,
            filterSavingGoal = SavingGoalFilter()
        )

    @Provides
    @Singleton
    fun providesOnNewTokensUseCase(
        repository: TokenRepository
    ): OnNewTokensUseCase =
        OnNewTokensUseCase(repository)

    @Provides
    @Singleton
    fun providesAddMoneyToSavingGoalUseCase(
        repository: AccountRepository
    ): AddMoneyToSavingGoalUseCase =
        AddMoneyToSavingGoalUseCase(repository)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindAccountsModule {
    @Binds
    abstract fun bindsAccountApiService(
        appApiService: AppApiService
    ): AccountsApiService
}
