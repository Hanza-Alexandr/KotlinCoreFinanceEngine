package org.example.viewmodels


import org.example.model.domain.Currency
import org.example.model.domain.TypeStorage
import org.example.model.service.StorageService
import org.example.views.storage.StorageListState
import org.example.views.storage.StorageState
import org.example.views.storage.StorageUi



class StorageViewModel(private val service: StorageService){

    fun getStorages(): StorageListState {
        val list = service.getStorages()

        if (list.isEmpty()) return StorageListState.Empty

        val uiList = list.map {
            StorageUi(
                name = it.name,
                currency = it.currency.name,
                type = it.typeStorage.name,
                note = it.note
            )
        }

        return StorageListState.Success(uiList)
    }

    fun createStorage(
        name: String,
        currency: String,
        typeStorage: String,
        note: String?
    ): StorageState {

        return try {
            val currencyEnum = Currency.valueOf(currency)
            val typeEnum = TypeStorage.valueOf(typeStorage)

            service.createStorage(name, currencyEnum, typeEnum, note)
            StorageState.Success

        } catch (e: IllegalArgumentException) {
            StorageState.Error("Неверный тип валюты или типа счёта")
        }
    }

}