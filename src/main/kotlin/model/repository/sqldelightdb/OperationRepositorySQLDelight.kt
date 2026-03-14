package org.example.model.repository.sqldelightdb

import com.example.CategoryEntity
import com.example.ColorEntity
import com.example.OperationEntity
import com.example.OperationQueries
import com.example.StorageEntity
import com.example.TransferEntity
import com.example.TransferQueries
import org.example.model.domain.Category
import org.example.model.domain.CreditTransaction
import org.example.model.domain.DebitTransaction
import org.example.model.domain.GeneralTransaction
import org.example.model.domain.NewOperation
import org.example.model.domain.Operation
import org.example.model.domain.StatusOperation
import org.example.model.domain.Storage
import org.example.model.domain.TransferTransaction
import org.example.model.domain.TypeOperation
import org.example.model.domain.toDomain
import org.example.model.repository.IOperationRepository
import java.time.LocalDate
import java.time.LocalTime

class OperationRepositorySQLDelight(private val queriesOp: OperationQueries, private val queriesTf: TransferQueries): IOperationRepository {
    override fun getAll(): List<Operation> {
        TODO("Not yet implemented")
    }

    override fun getOperationsByStorage(storageId: Long): List<Operation> {
        val listGenOp = queriesOp.selectOperationsByStorageId(storageId).executeAsList().map {
            OperationEntity(
                id = it.op_id,
                storage_id = it.storage_id,
                type_operation = it.type_operation,
                category_id = it.category_id,
                amount = it.amount,
                date = it.date,
                time = it.time,
                status = it.status
            ).toDomain(
                StorageEntity(
                    id = it.storage_id,
                    name = it.storage_name,
                    user_id = it.user_id,
                    currency = it.storage_currency,
                    type_storage = it.storage_type,
                    note = it.storage_note,
                    color_id = it.storage_color_id,
                    is_statistics = it.storage_is_statistics,
                    is_archive = it.storage_is_archive
                ).toDomain(
                    ColorEntity(
                        id = it.storage_color_id,
                        user_id = it.storage_color_user_id,
                        hex_code = it.storage_color_hex
                    ).toDomain()
                ),
                category = CategoryEntity(
                    id = it.category_id,
                    path_icon = it.category_icon,
                    user_id = it.category_user_id,
                    color_id = it.category_color_id,
                    name = it.category_name,
                    parent_category_id = it.category_parent_category_id,
                    need = it.category_need,
                    is_hide = it.category_is_hide
                ).toDomain(
                    ColorEntity(
                        id = it.category_color_id,
                        user_id = it.category_color_user_id,
                        hex_code = it.category_color_hex
                    ).toDomain()
                ),
            )
        }
        val listTf = queriesTf.selectTransfersByStorageId(storageId).executeAsList().map {
            TransferEntity(
                id = it.tr_id,
                from_storage_id = it.from_id,
                to_storage_id = it.to_id,
                amount = it.amount,
                date = it.date,
                time = it.time,
                status = it.status
            ).toDomain(
                fromStorage = StorageEntity(
                    id = it.from_id,
                    name = it.from_name,
                    user_id = it.from_user_id,
                    currency = it.from_currency,
                    type_storage = it.from_type,
                    note = it.from_note,
                    color_id = it.from_color_id,
                    is_statistics = it.from_is_statistics,
                    is_archive = it.from_is_archive
                ).toDomain(
                    ColorEntity(
                        id = it.from_color_id,
                        user_id = it.from_color_user_id,
                        hex_code = it.from_color_hex,
                    ).toDomain()
                ),
                toStorage = StorageEntity(
                    id = it.to_id,
                    name = it.to_name,
                    user_id = it.to_user_id,
                    currency = it.to_currency,
                    type_storage = it.to_type,
                    note = it.to_note,
                    color_id = it.to_color_id,
                    is_statistics = it.to_is_statistics,
                    is_archive = it.to_is_archive
                ).toDomain(
                    ColorEntity(
                        id = it.to_color_id,
                        user_id = it.to_color_user_id,
                        hex_code = it.to_color_hex,
                    ).toDomain()
                )
            )
        }
        return listTf+listGenOp
    }

    override fun getById(id: Long): Operation? {
        TODO("Not yet implemented")
    }

    override fun save(newOperation: NewOperation): Operation? {
        TODO("Not yet implemented")
    }

    override fun save(operation: Operation): Operation? {
        TODO("Not yet implemented")
    }

    override fun delete(operation: Operation): Operation? {
        TODO("Not yet implemented")
    }
}

fun OperationEntity.toDomain(storage: Storage, category: Category): GeneralTransaction{
    return when(type_operation){
        TypeOperation.DEBIT.toString() -> {
            DebitTransaction(
                id = id,
                storage = storage,
                category = category,
                amount = amount.toBigDecimal(),
                time = LocalTime.parse(time),
                date = LocalDate.parse(date),
                status = StatusOperation.valueOf(status)
            )
        }
        TypeOperation.CREDIT.toString() -> {
            CreditTransaction(
                id = id,
                storage = storage,
                category = category,
                amount = amount.toBigDecimal(),
                time = LocalTime.parse(time),
                date = LocalDate.parse(date),
                status = StatusOperation.valueOf(status)
            )
        }
        else -> throw IllegalArgumentException()
    }
}

fun TransferEntity.toDomain(fromStorage: Storage, toStorage: Storage): TransferTransaction{
    return TransferTransaction(
        id = id,
        fromStorage = fromStorage,
        toStorage = toStorage,
        amount = amount.toBigDecimal(),
        date = LocalDate.parse(date),
        time = LocalTime.parse(time),
        status = StatusOperation.valueOf(status)
    )
}