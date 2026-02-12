package org.example.viewmodels

import org.example.Currency
import org.example.TypeStorage
import org.example.model.domain.Storage
import org.example.model.service.StorageService


class StorageViewModel(private val service: StorageService){

    fun getListStorages(): List<Storage> {
        return service.getStorages()
    }
    fun createStorage(name: String, currency: Currency, typeStorage: TypeStorage, note: String?){
        service.createStorage(name,currency,typeStorage,note)
    }
    /*
    fun createNewGeneralStorage(storage: General): Boolean{
        return storageRepository.createNewStorage(storage) != null
    }
    fun deleteStorage(storageId: Long): Boolean{
        return storageRepository.deleteStorage(storageId) != null
    }

     */
}