package org.example.repository

import com.example.StorageQueries
import org.example.General
import org.example.Storage

class StorageRepository(private val queries: StorageQueries) {
    fun getAllStorages(): List<Storage>{
        return queries.selectAllStorageDao { id, title ->
            General(id,title)
        }.executeAsList()
    }

    fun createNewStorage(storage: Storage): Long?{
        return queries.insertStorageDao(storage.title).executeAsOneOrNull()
    }

    fun deleteStorage(storageId: Long): Long?{
        return queries.deleteStorageDao(storageId).executeAsOneOrNull()
    }

}