package org.example.model.repository.sqldelightdb

import com.example.StorageQueries
import org.example.Currency
import org.example.TypeStorage
import org.example.model.Color
import org.example.model.Storage
import org.example.model.repository.IStorageRepository
import org.example.model.service.CurrentUserService

class StorageRepositorySQLDelight(private val queries: StorageQueries, private val currentUserService: CurrentUserService):
    IStorageRepository {
    override fun getById(id: Long): Storage {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Storage> {
        val userId = currentUserService.userId ?: throw NullPointerException("UserId  отсутствует в данных приложения")
       return queries.selectStoragesByUserId(userId
        ) {
            id,
            name,
            user_id,
            currency,
            type_storage,
            note,
            color_id,
            is_statistics,
            is_archive,
            hex_code,
            color_user_id ->
           Storage(
               id,
               name,
               user_id,
               Currency.valueOf(currency),
               TypeStorage.valueOf(type_storage),
               note,
               Color(
                   color_id,
                   color_user_id,
                   hex_code ?: throw NullPointerException("При получении Storage hex_code выдал Null")
               ),
               is_statistics == 1L,
               is_archive == 1L
           )
        }.executeAsList()
    }

    override fun save(storage: Storage) {
        queries.setStorage(
            name = storage.name,
            user_id = storage.userId,
            currency = storage.currency.toString(),
            type_storage = storage.typeStorage.toString(),
            note = storage.note,
            color_id = storage.color.id ?: throw NullPointerException("StorageRepositoryIml; Line 54"),
            is_statistics = if (storage.isStatistics) 1L else 0,
            is_archive = if (storage.isArchive) 1L else 0
        )
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }



}