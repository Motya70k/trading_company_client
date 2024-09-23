package com.example.trading_company_client.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trading_company_client.data.model.Purchase
import com.example.trading_company_client.data.model.requests.AddPurchaseRequest
import com.example.trading_company_client.data.model.requests.UpdatePurchaseRequest
import com.example.trading_company_client.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.launch

class PurchaseViewModel(
    private val purchaseUseCase: PurchaseUseCase
) : ViewModel() {
    private val _purchases = MutableLiveData<List<Purchase>>()
    val purchases: LiveData<List<Purchase>> get() = _purchases

    private fun loadPurchase(token: String) {
        viewModelScope.launch {
            try {
                val purchaseList = purchaseUseCase.getAllPurchase(token)
                _purchases.postValue(purchaseList)
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun addPurchase(token: String, purchaseRequest: AddPurchaseRequest) {
        viewModelScope.launch {
            try {
                purchaseUseCase.addPurchase(token, purchaseRequest)
                loadPurchase(token)
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun updatePurchase(token: String, purchaseRequest: UpdatePurchaseRequest) {
        viewModelScope.launch {
            try {
                purchaseUseCase.updatePurchase(token, purchaseRequest)
                loadPurchase(token)
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun deletePurchase(token: String, purchaseId: Int) {
        viewModelScope.launch {
            try {
                purchaseUseCase.deletePurchase(token, purchaseId)
                loadPurchase(token)
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun searchPurchase(token: String, itemName: String) {
        viewModelScope.launch {
            try {
                val purchases = purchaseUseCase.fetchPurchaseByItemName(token, itemName)
                _purchases.postValue(purchases)
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }
}

class PurchaseViewModelFactory(
    private val purchaseUseCase: PurchaseUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PurchaseViewModel::class.java)) {
            return PurchaseViewModel(purchaseUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}