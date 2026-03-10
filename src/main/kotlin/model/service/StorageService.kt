package org.example.model.service


import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.repository.IStorageRepository

class StorageService(private val repo: IStorageRepository, private val currentUserService: CurrentUserService) {
    fun getStorageList(): StateDomainList<Storage>{
        val list =  repo.getAll()
        return StateDomainList.Success(list)
    }
}