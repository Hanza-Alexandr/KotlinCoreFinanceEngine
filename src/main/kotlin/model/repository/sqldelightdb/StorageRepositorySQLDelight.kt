package org.example.model.repository.sqldelightdb

import com.example.ColorEntity
import com.example.StorageEntity
import com.example.StorageQueries
import org.example.model.domain.ExistColor
import org.example.model.domain.Currency
import org.example.model.domain.NewStorage
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

    override fun getById(id: Long): Storage? {
        val querRes = queries.getStorageById(id).executeAsOneOrNull() ?: return null
        return StorageEntity(
            id = querRes.id,
            name = querRes.name,
            user_id = querRes.user_id,
            currency = querRes.currency,
            type_storage = querRes.type_storage,
            note = querRes.note,
            color_id = querRes.colorId,
            is_statistics = querRes.is_statistics,
            is_archive = querRes.is_archive
        ).toDomain(
            ColorEntity(
                querRes.colorId,
                querRes.colorOwnerId,
                querRes.colorHex
            ).toDomain()
        )
    }

    override fun save(storage: Storage): Storage? {
        return queries.updateStorage(
            name = storage.name,
            currency = storage.currency.toString(),
            type_storage = storage.typeStorage.toString(),
            note = storage.note,
            color_id = storage.color.id,
            is_statistics = if(storage.isStatistics) 1L else 0,
            is_archive = if(storage.isArchive) 1L else 0,
            id = storage.id
        ).executeAsOneOrNull()?.toDomain(storage.color)
    }

    override fun save(storage: NewStorage): Storage? {
        return queries.insertStorage(
            name = storage.name,
            user_id = storage.userId,
            currency = storage.currency.toString(),
            type_storage = storage.typeStorage.toString(),
            note = storage.note,
            color_id = storage.color.id,
            is_statistics = if(storage.isStatistics) 1L else 0,
            is_archive = if(storage.isArchive) 1L else 0,
        ).executeAsOneOrNull()?.toDomain(storage.color)
    }

    override fun delete(storage: Storage): Storage? = queries.deleteById(storage.id).executeAsOneOrNull()?.toDomain(storage.color)

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