package org.example.model.service


import org.example.model.domain.Currency
import org.example.model.domain.ExistColor
import org.example.model.domain.NewStorage
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.domain.TypeStorage
import org.example.model.repository.IStorageRepository

class StorageService(private val repo: IStorageRepository, private val currentUserService: CurrentUserService) {
    fun getStorageList(): StateDomainList<Storage>{
        val list =  repo.getAll()
        return if (list.isEmpty()) StateDomainList.Empty
        else StateDomainList.Success(list)
    }
    fun getStorage(storageId: Int): StateDomain<Storage>{
       return when(val stateGet = repo.getById(storageId.toLong())){
           null -> StateDomain.Error("❌Ошибка при получении счета")
           else -> StateDomain.Success(stateGet)
       }
    }
    fun createStorage(name: String, currency: Currency, typeStorage: TypeStorage, note: String?, color: ExistColor): StateDomain<Storage>{
        val stateNewStorage = NewStorage.create(
            name = name,
            userId = currentUserService.userId,
            currency = currency,
            typeStorage = typeStorage,
            note = note,
            color = color
        )
        return when(stateNewStorage){
            is StateDomain.Error-> StateDomain.Error(stateNewStorage.message)
            is StateDomain.Success -> {
                when(val stateCreate = repo.save(stateNewStorage.domain)){
                    null -> StateDomain.Error("❌Ошибка создания счета")
                    else -> StateDomain.Success(stateCreate)
                }
            }
        }
    }
    fun updateStorage(changingStorage: Storage, name: String?, typeStorage: TypeStorage?, note: String?, color: ExistColor?, isStatistic: Boolean?, isArchive: Boolean?): StateDomain<Storage>{
        var storage = changingStorage
        name?.let { storage = storage.changeName(it) }
        note?.let { storage = storage.changeNote(it) }
        typeStorage?.let { storage = storage.changeType(it) }
        color?.let { storage = storage.changeColor(it) }
        isStatistic?.let { storage = storage.switchStatistic(it) }
        isArchive?.let { storage = storage.switchArchive(it) }
        return when(val stateChanging = repo.save(storage)){
            null -> StateDomain.Error("❌Ошибка при попытке редактирования")
            else -> StateDomain.Success(stateChanging)
        }
    }
    fun deleteStorage(storage: Storage): StateDomain<Storage>{
       return when(val stateDelete = repo.delete(storage)){
            null -> StateDomain.Error("❌Ошибка удаления storage")
            else -> StateDomain.Success(stateDelete)
       }
    }

}