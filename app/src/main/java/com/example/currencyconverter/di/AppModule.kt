package com.example.currencyconverter.di

import com.example.currencyconverter.data.CurrencyApi
import com.example.currencyconverter.data.ExchangeRateRepositoryImpl
import com.example.currencyconverter.domain.ExchangeRateRepository
import com.example.currencyconverter.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideCurrencyApi():CurrencyApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(CurrencyApi::class.java)
    }
    @Singleton
    @Provides
    fun provideExchangeRepository(api: CurrencyApi):ExchangeRateRepository{
        return ExchangeRateRepositoryImpl(api)
    }
}