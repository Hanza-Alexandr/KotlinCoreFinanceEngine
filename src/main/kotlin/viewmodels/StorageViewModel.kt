package org.example.viewmodels


import org.example.General
import org.example.Storage
import org.example.repository.StorageRepository

class StorageViewModel(private val storageRepository: StorageRepository){

    fun getListStorages(): List<Storage> {
        return storageRepository.getAllStorages()
    }
    fun createNewGeneralStorage(storage: General): Boolean{
        return storageRepository.createNewStorage(storage) != null
    }
}