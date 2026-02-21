package org.example.model.service


import org.example.model.domain.Color
import org.example.model.domain.Currency
import org.example.model.domain.Storage
import org.example.model.domain.TypeStorage
import org.example.model.repository.IStorageRepository

class StorageService(private val repo: IStorageRepository, private val currentUserService: CurrentUserService) {
    fun getStorages(): List<Storage>{
        return repo.getAll()
    }
    fun createStorage(name: String, currency: Currency, typeStorage: TypeStorage, note: String?){

        val newColor = Color(1, null, "asd", false) //Пока так но нужно получать из вне и колор
        val newStorage = Storage(
            id = null,
            name = name,
            userId = currentUserService.userId ?: throw NullPointerException("StorageService line 17"),
            currency = currency,
            typeStorage = typeStorage ,
            note = note,
            color = newColor,
        )


        repo.save(newStorage)
    }
    //Изменение сущности
    fun renameStorage(id: Long, newName: String) {
        val storage = repo.getById(id)

        val updated = storage.rename(newName)

        repo.save(updated)
    }
    //TODO("И еще куча методов по изменению сущности")
    //
    //

}