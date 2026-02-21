package org.example.model.repository.sqldelightdb

import com.example.StorageQueries
import org.example.model.domain.Color
import org.example.model.domain.Storage
import org.example.model.repository.IStorageRepository
import org.example.model.service.CurrentUserService

class StorageRepositorySQLDelight(private val queries: StorageQueries): IStorageRepository {
    override fun getById(id: Long): Storage {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Storage> {
        TODO("Not yet implemented")
    }

    override fun save(storage: Storage) {
        TODO("Not yet implemented")

    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }


}