package com.example.currencyconverter.data

data class DataOrException<T,Boolean,E:Exception> (
    var data:T? = null,
    var isLoading:Boolean? = null,
    var exc:E? = null
        )