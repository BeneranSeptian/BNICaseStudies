package com.seftian.bnicasestudies.core.data

import com.seftian.bnicasestudies.core.data.local.LocalDataSource
import com.seftian.bnicasestudies.core.data.local.entity.PromoEntity
import com.seftian.bnicasestudies.core.data.remote.RemoteDataSource
import com.seftian.bnicasestudies.core.data.remote.network.ResponseStatus
import com.seftian.bnicasestudies.core.data.remote.response.PromoResponseItem
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
                        mapPromoResponseItemToPromoItem(it)
                    }
                    val promoEntities = promoItemList.map {
                        mapPromoItemToPromoEntity(it)
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
            val convertedPromo = mapPromoEntityToPromoItem(promo)
            emit(convertedPromo)
        }.flowOn(Dispatchers.IO)
    }


    private fun mapPromoEntityToPromoItem(promoEntity: PromoEntity): PromoItem {
        return PromoItem(
            createdAt = promoEntity.createdAt,
            desc = promoEntity.desc,
            id = promoEntity.id,
            img = Img(url = promoEntity.url),
            nama = promoEntity.nama,
            lokasi = promoEntity.lokasi
        )
    }

    private fun mapPromoItemToPromoEntity(promoItem: PromoItem): PromoEntity {
        return PromoEntity(
            createdAt = promoItem.createdAt,
            desc = promoItem.desc,
            id = promoItem.id,
            url = promoItem.img.url,
            nama = promoItem.nama,
            lokasi = promoItem.lokasi
        )
    }

    private fun mapPromoResponseItemToPromoItem(promoResponseItem: PromoResponseItem): PromoItem {
        return PromoItem(
            createdAt = promoResponseItem.createdAt,
            desc = promoResponseItem.desc,
            id = promoResponseItem.id,
            img = Img(url = promoResponseItem.img.url),
            nama = promoResponseItem.nama,
            lokasi = promoResponseItem.lokasi
        )
    }
}