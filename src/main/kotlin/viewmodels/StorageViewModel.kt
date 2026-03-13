package org.example.viewmodels

import org.example.model.domain.Currency
import org.example.model.domain.ExistColor
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.domain.TypeStorage
import org.example.model.service.StorageService

class StorageViewModel(private val service: StorageService){
    fun getStorages(): StateDomainList<Storage>{
        return service.getStorageList()
    }
    fun createStorage(name: String, currency: Currency, typeStorage: TypeStorage, note: String?, color: ExistColor): StateDomain<Storage>{
        return service.createStorage(name, currency, typeStorage, note, color)
    }
    fun getStorage(storageId: Int): StateDomain<Storage>{
        return service.getStorage(storageId)
    }
    fun update(changingStorage: Storage, name: String?, typeStorage: TypeStorage?, note: String?, color: ExistColor?, isStatistic: Boolean?, isArchive: Boolean?): StateDomain<Storage>{
        return service.updateStorage(changingStorage,name,typeStorage,note, color, isStatistic, isArchive)
    }
    fun delete(storage: Storage): StateDomain<Storage>{
        return service.deleteStorage(storage)
    }
}