package com.seftian.bnicasestudies.core.data

import com.seftian.bnicasestudies.core.data.local.LocalDataSource
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class SaldoRepositoryTest {

    private val localDataSource = mockk<LocalDataSource>()
    private val saldoRepository = SaldoRepository(localDataSource)

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `when getSaldo is called, it should call getSaldoFromLocal from LocalDataSource`() = run {
        runTest {

            val expectedSaldo = 100L
            coEvery { localDataSource.getSaldoFromLocal() } returns flowOf(expectedSaldo)

            val result = saldoRepository.getSaldo().toList().first()

            coVerify { localDataSource.getSaldoFromLocal() }
            assert(result == expectedSaldo)
        }
    }

    @Test
    fun `when updateSaldo is called, it should update saldo in LocalDataSource`() = run {
        runTest {
            val newSaldo = 200L
            coEvery { localDataSource.updateSaldoInLocal(newSaldo) } just Runs

            saldoRepository.updateSaldo(newSaldo)

            coVerify { localDataSource.updateSaldoInLocal(newSaldo) }
        }
    }
}