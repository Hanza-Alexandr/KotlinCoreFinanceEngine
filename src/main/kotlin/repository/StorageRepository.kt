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

}