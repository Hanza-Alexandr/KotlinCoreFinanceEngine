package org.example.model.service


import org.example.model.domain.Currency
import org.example.model.domain.ExistColor
import org.example.model.domain.NewStorage
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.domain.TypeStorage
import org.example.model.repository.IStorageRepository
import java.awt.Color

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
    fun save(name: String, currency: Currency, typeStorage: TypeStorage, note: String?, color: ExistColor): StateDomain<Storage>{
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
    fun save(storage: Storage): StateDomain<Storage>{
        TODO()
    }

}