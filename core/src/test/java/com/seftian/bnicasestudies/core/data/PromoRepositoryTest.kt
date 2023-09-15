package com.seftian.bnicasestudies.core.data

import com.seftian.bnicasestudies.core.data.local.LocalDataSource
import com.seftian.bnicasestudies.core.data.local.entity.PromoEntity
import com.seftian.bnicasestudies.core.data.remote.RemoteDataSource
import com.seftian.bnicasestudies.core.data.remote.network.ResponseStatus
import com.seftian.bnicasestudies.core.data.remote.response.ImgResponse
import com.seftian.bnicasestudies.core.data.remote.response.PromoResponse
import com.seftian.bnicasestudies.core.data.remote.response.PromoResponseItem
import com.seftian.bnicasestudies.core.domain.ResourceState
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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class PromoRepositoryTest{
    private val remoteDataSource = mockk<RemoteDataSource>()
    private val localDataSource = mockk<LocalDataSource>()

    private val promoRepository = PromoRepository(remoteDataSource, localDataSource)

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `when getPromos is called and local data is available, it should return local data`() = run {
        runTest {
            val promoLocal = listOf(
                PromoEntity(createdAt = "sampleDate", desc = "desc", id = 1, url = "url", nama = "name", lokasi = "location")
            )

            every { localDataSource.getAllPromo() } returns flowOf(promoLocal)

            val result = promoRepository.getPromos().toList()

            coVerify { localDataSource.getAllPromo() }
            coVerify(exactly = 0) { remoteDataSource.getPromos() }
            assert(result.last() is ResourceState.Success)
        }
    }

    @Test
    fun `when getPromo is called, it should return a promo from local data`() = run {
        runTest {
            val promoId = 1
            val promoLocal = PromoEntity(createdAt = "sampleDate", desc = "desc", id = promoId, url = "url", nama = "name", lokasi = "location")

            every { localDataSource.getSinglePromo(promoId) } returns flowOf(promoLocal)

            val result = promoRepository.getPromo(promoId).first()

            coVerify { localDataSource.getSinglePromo(promoId) }
            assert(result.id == promoId)
        }
    }

    @Test
    fun `when getPromos is called and local data is not available, it should fetch from remote then insert it to local and return the promos`() = run{
        runTest {
            every {localDataSource.getAllPromo()} returns flowOf(emptyList())

            val samplePromoResponseItemList = listOf(
                PromoResponseItem(
                    createdAt = "sample",
                    desc = "sample desc",
                    id = 1,
                    img = ImgResponse(url = "sampleUrl"),
                    lokasi = "sampleLokasi",
                    nama = "sampleNama"
                )
            )

            val samplePromoResponse = PromoResponse().apply { addAll(samplePromoResponseItemList) }

            coEvery { remoteDataSource.getPromos() } returns flowOf(ResponseStatus.Success(samplePromoResponse))

            // Mock insert action into local data source
            coEvery { localDataSource.insertPromo(any()) } just Runs

            // Act
            val result = promoRepository.getPromos().toList()

            // Assert
            // Check if local data source was queried
            coVerify { localDataSource.getAllPromo() }
            // Check if remote data source was accessed due to absence of local data
            coVerify { remoteDataSource.getPromos() }
            // Check if the fetched data was saved into the local data source
            coVerify(exactly = samplePromoResponse.size) { localDataSource.insertPromo(any()) }
            // Check if the correct data is returned
            assert(result.last() is ResourceState.Success)
            assert((result.last() as ResourceState.Success).data.size == samplePromoResponse.size)
        }
    }
}