package com.seftian.bnicasestudies.core.di

import android.content.Context
import androidx.room.Room
import com.seftian.bnicasestudies.core.data.PromoRepository
import com.seftian.bnicasestudies.core.data.SaldoRepository
import com.seftian.bnicasestudies.core.data.TrxRepository
import com.seftian.bnicasestudies.core.data.local.CaseStudiesAppDb
import com.seftian.bnicasestudies.core.data.local.LocalDataSource
import com.seftian.bnicasestudies.core.data.remote.RemoteDataSource
import com.seftian.bnicasestudies.core.data.remote.network.Digi46Api
import com.seftian.bnicasestudies.core.domain.usecase.promo.PromoInteractor
import com.seftian.bnicasestudies.core.domain.usecase.promo.PromoUseCase
import com.seftian.bnicasestudies.core.domain.usecase.saldo.SaldoInteractor
import com.seftian.bnicasestudies.core.domain.usecase.saldo.SaldoUseCase
import com.seftian.bnicasestudies.core.domain.usecase.transaction.TransactionInteractor
import com.seftian.bnicasestudies.core.domain.usecase.transaction.TransactionUseCase
import com.seftian.bnicasestudies.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieApi(): Digi46Api {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer ${BuildConfig.BEARER_TOKEN}")
                .build()

            chain.proceed(modifiedRequest)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Digi46Api.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Digi46Api::class.java)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(caseStudiesAppDb: CaseStudiesAppDb): LocalDataSource {
        return LocalDataSource(
            caseStudiesAppDb.saldoDao,
            caseStudiesAppDb.trxDao,
            caseStudiesAppDb.promoDao
        )
    }

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(digi46Api: Digi46Api): RemoteDataSource {
        return RemoteDataSource(digi46Api)
    }

    @Provides
    @Singleton
    fun provideSaldoRepository(
        localDataSource: LocalDataSource
    ): SaldoRepository {
        return SaldoRepository(localDataSource)
    }

    @Provides
    @Singleton
    fun provideSaldoUseCase(saldoRepository: SaldoRepository): SaldoUseCase {
        return SaldoInteractor(saldoRepository)
    }

    @Provides
    @Singleton
    fun providePromoRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): PromoRepository {
        return PromoRepository(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideTrxUseCase(trxRepository: TrxRepository): TransactionUseCase {
        return TransactionInteractor(trxRepository)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        localDataSource: LocalDataSource
    ): TrxRepository {
        return TrxRepository(localDataSource)
    }

    @Provides
    @Singleton
    fun providePromoUseCase(promoRepository: PromoRepository): PromoUseCase {
        return PromoInteractor(promoRepository)
    }

    @Provides
    @Singleton
    fun provideCaseStudiesAppDb(@ApplicationContext context: Context): CaseStudiesAppDb {
        return Room.databaseBuilder(
            context,
            CaseStudiesAppDb::class.java,
            "casestudies.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}