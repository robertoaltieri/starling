package com.altieri.starling.account.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AccountValidatorTest {

    lateinit var validateAccount: AccountValidator

    @Before
    fun setUp() {
        validateAccount = AccountValidator()
    }

    @Test
    fun `GIVEN the account is valid WHEN validate the account THEN return true`() {
        // given
        val account = valid

        // when
        val isValid = validateAccount(account)

        // then
        assertTrue(isValid)

    }

    @Test
    fun `GIVEN the accountUid is null WHEN validate the account THEN return false`() {
        // given
        val account = valid.copy(
            accountUid = null
        )

        // when
        val isValid = validateAccount(account)

        // then
        assertFalse(isValid)
    }

    @Test
    fun `GIVEN the defaultCategory is null WHEN validate the account THEN return false`() {
        // given
        val account = valid.copy(
            defaultCategory = null
        )

        // when
        val isValid = validateAccount(account)

        // then
        assertFalse(isValid)
    }

    @Test
    fun `GIVEN the currency is null WHEN validate the account THEN return false`() {
        // given
        val account = valid.copy(
            currency = null
        )

        // when
        val isValid = validateAccount(account)

        // then
        assertFalse(isValid)
    }

    private companion object {
        val valid = AccountData(
            accountUid = "accountUid",
            accountType = "accountType",
            defaultCategory = "defaultCategory",
            currency = "currency"
        )
    }
}
