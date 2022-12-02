package com.example.currencyconverter.data

import android.util.Log
import com.example.currencyconverter.domain.ExchangeRateRepository
import com.example.currencyconverter.domain.model.ConversionRate
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: CurrencyApi
): ExchangeRateRepository {
    private var dataOrException:DataOrException<ConversionRate,Boolean,Exception> = DataOrException()
    override suspend fun getConvertRate(
        baseCode: String,
        targetCode: String
    ): DataOrException<ConversionRate, Boolean, Exception> {
        try {
            dataOrException.data = api.getConversionRate(baseCode,targetCode)
        }catch (e:Exception){
            dataOrException.exc = e
        }
        return dataOrException
    }
}