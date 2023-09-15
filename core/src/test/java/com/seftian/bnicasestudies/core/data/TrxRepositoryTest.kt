package com.seftian.bnicasestudies.core.data

import com.seftian.bnicasestudies.core.data.local.LocalDataSource
import com.seftian.bnicasestudies.core.data.local.entity.TrxEntity
import com.seftian.bnicasestudies.core.domain.model.Transaction
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class TrxRepositoryTest{
    private val localDataSource = mockk<LocalDataSource>()

    private val trxRepository = TrxRepository(localDataSource)

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `when getAllTransactions is called, it should return transactions from local data source`() = run {
        runTest {
            val trxLocalList = listOf(
                // Sample data for a TrxEntity, repeat as necessary
                TrxEntity(id = "1", sourceBank = "BankA", merchantName = "MerchantA", trxAmount = 1000)
            )
            every { localDataSource.getAllTrx() } returns flowOf(trxLocalList)

            // Act
            val result = trxRepository.getAllTransactions().first()

            // Assert
            coVerify { localDataSource.getAllTrx() }
            assert(result.size == trxLocalList.size)
        }
    }

    @Test
    fun `when insertTrx is called, it should insert transaction to local data source`() = run {
        runTest{
            val transaction = Transaction(id = "1", sourceBank = "BankA", merchantName = "MerchantA", trxAmount = 1000)

            coEvery { localDataSource.insertTrx(match {
                it.id == transaction.id &&
                        it.sourceBank == transaction.sourceBank &&
                        it.merchantName == transaction.merchantName &&
                        it.trxAmount == transaction.trxAmount
            }) } just Runs

            // Act
            trxRepository.insertTrx(transaction)

            // Assert
            coVerify { localDataSource.insertTrx(match {
                it.id == transaction.id &&
                        it.sourceBank == transaction.sourceBank &&
                        it.merchantName == transaction.merchantName &&
                        it.trxAmount == transaction.trxAmount
            }) }
        }
    }

}