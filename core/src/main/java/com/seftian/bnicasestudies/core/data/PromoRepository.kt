package com.seftian.bnicasestudies.core.data

import android.util.Log
import com.seftian.bnicasestudies.core.data.local.LocalDataSource
import com.seftian.bnicasestudies.core.data.local.entity.PromoEntity
import com.seftian.bnicasestudies.core.data.remote.RemoteDataSource
import com.seftian.bnicasestudies.core.data.remote.network.ResponseStatus
import com.seftian.bnicasestudies.core.data.remote.response.PromoResponse
import com.seftian.bnicasestudies.core.domain.ResourceState
import com.seftian.bnicasestudies.core.domain.model.Img
import com.seftian.bnicasestudies.core.domain.model.PromoItem
import com.seftian.bnicasestudies.core.domain.repository.IPromoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class PromoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
):IPromoRepository {
    override fun getPromos(): Flow<ResourceState<List<PromoItem>>> {
        return flow {
            emit(ResourceState.Loading)

            val promoFromLocal = localDataSource.getAllPromo().first()

            if(promoFromLocal.isNotEmpty()){
                val converted = promoFromLocal.map {
                    PromoItem(
                        createdAt = it.createdAt,
                        desc = it.desc,
                        id = it.id,
                        img = Img(
                            url = it.url
                        ),
                        nama = it.nama,
                        lokasi = it.lokasi,
                    )
                }
                emit(ResourceState.Success(converted))
                return@flow
            }

            when(val response  = remoteDataSource.getPromos().last()){
                is ResponseStatus.Success -> {
                    val promoItemList = response.data.map {
                        PromoItem(
                            createdAt = it.createdAt,
                            desc = it.desc,
                            id = it.id,
                            img = Img(
                                url = it.img.url
                            ),
                            nama = it.nama,
                            lokasi = it.lokasi,
                        )
                    }
                    val promoEntities = promoItemList.map {
                        PromoEntity(
                            createdAt = it.createdAt,
                            desc = it.desc,
                            id = it.id,
                            url = it.img.url,
                            nama = it.nama,
                            lokasi = it.lokasi,
                        )
                    }

                    promoEntities.forEach {
                        localDataSource.insertPromo(it)
                    }

                    emit(ResourceState.Success(promoItemList))
                }
                is ResponseStatus.Error -> emit(ResourceState.Error(response.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getPromo(id: Int): Flow<PromoItem> {
        return flow {
            val promo = localDataSource.getSinglePromo(id).first()
            val convertedPromo = PromoItem(
                createdAt = promo.createdAt,
                desc = promo.desc,
                id = promo.id,
                img = Img(
                    url = promo.url
                ),
                nama = promo.nama,
                lokasi = promo.lokasi
            )
            emit(convertedPromo)
        }.flowOn(Dispatchers.IO)
    }
}