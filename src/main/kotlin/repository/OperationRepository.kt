package org.example.repository

import com.example.OperationsQueries
import com.example.TransferDao
import com.example.TransferQueries
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

class OperationRepository(private val queriesOp: OperationsQueries, private val queriesTf: TransferQueries) {


    fun getOperationsDaoByToStorages(storagesId:List<Long>): List<Operation>{
        TODO("Поменял принцип, теперь в в БД в таблице Операции категории операции обязательна даже для перевода(для нее надо сделать базовою скрытую категорию какую нить). И теперь любая операция мпппится нормально. НО надо сделаьб так что бы операции перевода мапились в отдельную сущность для нее предназначенную")
        //getTransferByStorages(storagesId)
        return getGeneralOperationsDaoByToStorages(storagesId)
    }
    private fun getTransferByStorages(storagesId: List<Long>){
        TODO()
        //queriesTf.selectTransferByStorages(storagesId){transfer_id, transfer_occurred_at, from_operation_id, from_operation_amount, from_operation_occurred_at, from_operation_type, from_storage_id, from_storage_title, to_operation_id, to_operation_amount, to_operation_occurred_at, to_operation_type, to_storage_id, to_storage_title -> }
    }

     fun getGeneralOperationsDaoByToStorages(storagesId: List<Long>): List<Operation> {

        return queriesOp.selectFullOperationByStorages(storagesId) { id,
                                                                    amount,
                                                                    occurred_at,
                                                                    type_operation,
                                                                    storage_id,
                                                                    storage_title,
                                                                    category_id,
                                                                    category_title,
                                                                    category_parent_id,
                                                                    ->

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