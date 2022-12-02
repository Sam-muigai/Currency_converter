package com.example.currencyconverter.data

import com.example.currencyconverter.domain.model.ConversionRate
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface CurrencyApi {
    @GET("pair/{baseCode}/{targetCode}")
    suspend fun getConversionRate(
        @Path("baseCode") baseCode: String,
        @Path("targetCode") targetCode: String
    ): ConversionRate
}