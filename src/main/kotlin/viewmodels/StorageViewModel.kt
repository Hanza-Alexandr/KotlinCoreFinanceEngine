package org.example.viewmodels


import org.example.Storage
import org.example.repository.StorageRepository

class StorageViewModel(private val storageRepository: StorageRepository){

    fun getListStorages(): List<Storage> {
        return storageRepository.getAllStorages()
    }
}