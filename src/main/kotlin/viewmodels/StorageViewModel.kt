package org.example.viewmodels


import com.oracle.svm.core.annotate.Delete
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
    fun deleteStorage(storageId: Long): Boolean{
        return storageRepository.deleteStorage(storageId) != null
    }
}