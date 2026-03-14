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
import org.example.model.domain.NewGeneralOperation
import org.example.model.domain.NewOperation
import org.example.model.domain.NewTransferTransaction
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
    override fun getOperationById(id: Long): GeneralTransaction? {
        val queRes =  queriesOp.selectOperationById(id).executeAsOneOrNull() ?: return null
        return OperationEntity(
            id = queRes.op_id,
            storage_id = queRes.storage_id,
            type_operation = queRes.type_operation,
            category_id = queRes.category_id,
            amount = queRes.amount,
            date = queRes.date,
            time = queRes.time,
            status = queRes.status
        ).toDomain(
            StorageEntity(
                id = queRes.storage_id,
                name = queRes.storage_name,
                user_id = queRes.user_id,
                currency = queRes.storage_currency,
                type_storage = queRes.storage_type,
                note = queRes.storage_note,
                color_id = queRes.storage_color_id,
                is_statistics = queRes.storage_is_statistics,
                is_archive = queRes.storage_is_archive
            ).toDomain(
                ColorEntity(
                    id = queRes.storage_color_id,
                    user_id = queRes.storage_color_user_id,
                    hex_code = queRes.storage_color_hex
                ).toDomain()
            ),
            category = CategoryEntity(
                id = queRes.category_id,
                path_icon = queRes.category_icon,
                user_id = queRes.category_user_id,
                color_id = queRes.category_color_id,
                name = queRes.category_name,
                parent_category_id = queRes.category_parent_category_id,
                need = queRes.category_need,
                is_hide = queRes.category_is_hide
            ).toDomain(
                ColorEntity(
                    id = queRes.category_color_id,
                    user_id = queRes.category_color_user_id,
                    hex_code = queRes.category_color_hex
                ).toDomain()
            ),
        )
    }
    override fun getTransferById(id: Long): TransferTransaction? {
        val queRes = queriesTf.selectTransferById(id).executeAsOneOrNull() ?: return null
        return TransferEntity(
            id = queRes.tr_id,
            from_storage_id = queRes.from_id,
            to_storage_id = queRes.to_id,
            amount = queRes.amount,
            date = queRes.date,
            time = queRes.time,
            status = queRes.status
        ).toDomain(
            fromStorage = StorageEntity(
                id = queRes.from_id,
                name = queRes.from_name,
                user_id = queRes.from_user_id,
                currency = queRes.from_currency,
                type_storage = queRes.from_type,
                note = queRes.from_note,
                color_id = queRes.from_color_id,
                is_statistics = queRes.from_is_statistics,
                is_archive = queRes.from_is_archive
            ).toDomain(
                ColorEntity(
                    id = queRes.from_color_id,
                    user_id = queRes.from_color_user_id,
                    hex_code = queRes.from_color_hex,
                ).toDomain()
            ),
            toStorage = StorageEntity(
                id = queRes.to_id,
                name = queRes.to_name,
                user_id = queRes.to_user_id,
                currency = queRes.to_currency,
                type_storage = queRes.to_type,
                note = queRes.to_note,
                color_id = queRes.to_color_id,
                is_statistics = queRes.to_is_statistics,
                is_archive = queRes.to_is_archive
            ).toDomain(
                ColorEntity(
                    id = queRes.to_color_id,
                    user_id = queRes.to_color_user_id,
                    hex_code = queRes.to_color_hex,
                ).toDomain()
            )
        )
    }
    override fun save(newOperation: NewOperation): Operation? {
        when(newOperation){
            is NewGeneralOperation -> {
                val queRes = queriesOp.insertOperation(
                    storage_id = newOperation.storage.id,
                    type_operation = newOperation.typeOperation.toString(),
                    category_id = newOperation.category.id,
                    amount = newOperation.amount.toDouble(),
                    date = newOperation.date.toString(),
                    time = newOperation.time.toString(),
                    status = newOperation.status.toString()
                ).executeAsOneOrNull() ?: return null
                return getOperationById(queRes.id)
            }
            is NewTransferTransaction -> {
                val queRes = queriesTf.insertTransfer(
                    from_storage_id = newOperation.fromStorage.id,
                    to_storage_id = newOperation.toStorage.id,
                    amount = newOperation.amount.toDouble(),
                    date = newOperation.date.toString(),
                    time = newOperation.time.toString(),
                    status = newOperation.status.toString()
                ).executeAsOneOrNull()?: return null
                return getTransferById(queRes.id)
            }
            else -> throw IllegalArgumentException()
        }
    }
    override fun save(operation: Operation): Operation? {
        when(operation) {
            is CreditTransaction -> {
                val queRes = queriesOp.updateOperation(
                    id = operation.id,
                    storage_id = operation.storage.id,
                    type_operation = operation.typeOperation.name,
                    category_id = operation.category.id,
                    amount = operation.amount.toDouble(),
                    date = operation.date.toString(),
                    time = operation.time.toString(),
                    status = operation.status.name
                ).executeAsOneOrNull() ?: return null
                return operation
            }

            is DebitTransaction -> {
                val queRes = queriesOp.updateOperation(
                    id = operation.id,
                    storage_id = operation.storage.id,
                    type_operation = operation.typeOperation.name,
                    category_id = operation.category.id,
                    amount = operation.amount.toDouble(),
                    date = operation.date.toString(),
                    time = operation.time.toString(),
                    status = operation.status.name
                ).executeAsOneOrNull() ?: return null
                return operation
            }

            is TransferTransaction -> {
                val queRes = queriesTf.updateTransfer(
                    id = operation.id,
                    from_storage_id = operation.fromStorage.id,
                    to_storage_id = operation.toStorage.id,
                    amount = operation.amount.toDouble(),
                    date = operation.date.toString(),
                    time = operation.time.toString(),
                    status = operation.status.name
                ).executeAsOneOrNull() ?: return null
                return operation
            }
            else -> throw IllegalArgumentException()
        }
    }
    override fun delete(operation: Operation): Operation? {

        when (operation){
            is GeneralTransaction -> {
                val queRes = queriesTf.deleteTransferById(operation.id).executeAsOneOrNull() ?: return null
                return operation
            }
            is TransferTransaction -> {
                val queRes = queriesTf.deleteTransferById(operation.id).executeAsOneOrNull() ?: return null
                return operation
            }
            else -> throw IllegalArgumentException()
        }
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