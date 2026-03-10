package org.example.model.repository.sqldelightdb

import com.example.ColorEntity
import com.example.StorageEntity
import com.example.StorageQueries
import org.example.model.domain.ExistColor
import org.example.model.domain.Currency
import org.example.model.domain.Storage
import org.example.model.domain.TypeStorage
import org.example.model.repository.IStorageRepository

class StorageRepositorySQLDelight(private val queries: StorageQueries): IStorageRepository {
    override fun getAll(): List<Storage> {
        return queries.selectAllStorage().executeAsList().map {
            StorageEntity(
                id = it.id,
                name = it.name,
                user_id = it.user_id,
                currency = it.currency,
                type_storage = it.type_storage,
                note = it.note,
                color_id = it.color_id,
                is_statistics = it.is_statistics,
                is_archive = it.is_archive
            ).toDomain(
                ColorEntity(
                        it.color_id,
                        it.color_user_id,
                        it.color_hex
                ).toDomain()
            )
        }
    }

    fun StorageEntity.toDomain(color: ExistColor): Storage{
        return Storage(
            id = id,
            name = name,
            userId = user_id,
            currency = Currency.valueOf(currency),
            typeStorage = TypeStorage.valueOf(type_storage),
            note = note,
            color = color,
            isStatistics = is_statistics==1L,
            isArchive = is_archive==1L
        )
    }
}