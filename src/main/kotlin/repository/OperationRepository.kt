package org.example.repository

import com.example.OperationsQueries
import org.example.Category
import org.example.CreditTransaction
import org.example.DebitTransaction
import org.example.Formater
import org.example.General
import org.example.Operation
import org.example.Storage
import org.example.TransferTransaction
import org.example.TypeOperation
import java.time.LocalDateTime

class OperationRepository(private val queries: OperationsQueries) {


    fun getOperationsDaoByToStorages(storagesId: List<Long>): List<Operation> {
        return queries.selectFullOperationByStorages(storagesId) { id,
                                                                    amount,
                                                                    occurred_at,
                                                                    type_operation,
                                                                    storage_id,
                                                                    storage_title,
                                                                    category_id,
                                                                    category_title,
                                                                    category_parent_id,
                                                                    transfer_id,
                                                                    transfer_amount,
                                                                    transfer_occurred_at,
                                                                    transfer_from_id,
                                                                    transfer_from_title,
                                                                    transfer_to_id,
                                                                    transfer_to_title, ->
            if (transfer_id !== null &&
                transfer_amount !== null &&
                transfer_occurred_at !== null &&
                transfer_from_id !== null &&
                transfer_from_title !== null &&
                transfer_to_id !== null &&
                transfer_to_title !== null
            ) {
                return@selectFullOperationByStorages TransferTransaction(
                    transfer_id,
                    General(transfer_from_id, transfer_from_title),
                    General(transfer_to_id, transfer_to_title),
                    TypeOperation.valueOf(type_operation),
                    amount.toBigDecimal(),
                    LocalDateTime.parse(occurred_at, Formater.DATE_TIME_FORMATTER)
                )
            }
            if (category_id !== null &&
                category_title !== null
            ) {
                when (TypeOperation.valueOf(type_operation)) {
                    TypeOperation.CREDIT -> {
                        return@selectFullOperationByStorages CreditTransaction(
                            id,
                            General(storage_id, storage_title),
                            LocalDateTime.parse(occurred_at, Formater.DATE_TIME_FORMATTER),
                            Category(
                                category_id,
                                category_title,
                                null
                            ), //Ставлю null сам т.к мне нужна просто название категории и не надо всю иерархию
                            amount.toBigDecimal()
                        )

                    }

                    TypeOperation.DEBIT -> {
                        return@selectFullOperationByStorages DebitTransaction(
                            id,
                            General(storage_id, storage_title),
                            LocalDateTime.parse(occurred_at, Formater.DATE_TIME_FORMATTER),
                            Category(
                                category_id,
                                category_title,
                                null
                            ), //Ставлю null сам т.к мне нужна просто название категории и не надо всю иерархию
                            amount.toBigDecimal()
                        )
                    }
                }
            }
            error("Некорректные данные в БД: id=$id")
        }.executeAsList()
    }
}