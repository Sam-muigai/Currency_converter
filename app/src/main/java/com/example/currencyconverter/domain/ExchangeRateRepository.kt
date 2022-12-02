package com.example.currencyconverter.domain

import com.example.currencyconverter.data.DataOrException
import com.example.currencyconverter.domain.model.ConversionRate

interface ExchangeRateRepository {
    suspend fun getConvertRate(baseCode:String,targetCode:String):
            DataOrException<ConversionRate,Boolean,Exception>
}