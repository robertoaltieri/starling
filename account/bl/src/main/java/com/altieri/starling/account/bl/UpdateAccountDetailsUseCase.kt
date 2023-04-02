package com.altieri.starling.account.bl

import java.lang.IllegalStateException

/**
 * It uses the [AccountRepository] to fetch the primary account
 * In case the repository does not return an account it throws an [IllegalStateException]
 */
class UpdateAccountDetailsUseCase(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(): Account =
        repository.primaryAccount() ?: throw IllegalStateException("account not found")
}
