package com.example.currencyconverter.presentation.mainscreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.DataOrException
import com.example.currencyconverter.domain.ExchangeRateRepository
import com.example.currencyconverter.domain.model.ConversionRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ExchangeRateRepository
) : ViewModel() {
    val targetCurrency = mutableStateOf("")
    val baseCurrency = mutableStateOf("")
    val tcexpanded = mutableStateOf(false)
    val bcexpanded = mutableStateOf(false)
    val show = mutableStateOf(false)
    fun onShowChange() {
        show.value = !show.value
    }

    fun ontcxpandedChange() {
        tcexpanded.value = !tcexpanded.value
    }

    fun onbcxpandedChange() {
        bcexpanded.value = !bcexpanded.value
    }

    fun onTargetCurrencyChange(newCur: String) {
        targetCurrency.value = newCur
    }

    fun onBaseCurrencyChange(newCur: String) {
        baseCurrency.value = newCur
    }

    private var _fetchedData: MutableState<DataOrException<ConversionRate, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, null, null))
    val fetched = _fetchedData
    fun getConversionRate(baseCode: String, targetCode: String) {
        viewModelScope.launch {
            _fetchedData.value.isLoading = true
            _fetchedData.value = repository.getConvertRate(baseCode, targetCode)
            if (_fetchedData.value.data.toString().isNotEmpty()) {
                _fetchedData.value.isLoading = false
            }
            Log.d("TAG", _fetchedData.value.data.toString())
        }
    }
}