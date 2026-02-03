package org.example.repository

import com.example.OperationsQueries
import org.example.Category
import org.example.Formater
import org.example.General
import org.example.LossTransaction
import org.example.Operation
import org.example.ProfitTransaction
import org.example.Storage
import org.example.TransferTransaction
import org.example.TypeOperation
import java.time.LocalDateTime

class OperationRepository(private val queries: OperationsQueries){


    fun getOperationsDaoByToStorages(storagesId: List<Long>): List<Operation>{
        return queries.selectFullOperationsByToStorages(storagesId)
        { id,
          amount,
          date_time,
          from_storage,
          to_storage,
          category,
          type_operation,
          from_id,
          from_title,
          from_balance,
          to_id,
          to_title,
          to_balance,
          category_id,
          category_title,
          type_id,
          type_title ->
            requireNotNull(from_id)
            requireNotNull(from_title)
            requireNotNull(from_balance)
            requireNotNull(type_title)

            when(TypeOperation.valueOf(type_title)){
                TypeOperation.TRANSFER -> {
                    requireNotNull(to_id)
                    requireNotNull(to_title)
                    requireNotNull(to_balance)
                    TransferTransaction(id,
                        General(from_id,from_title,from_balance.toBigDecimal()),
                        General(to_id,to_title,to_balance.toBigDecimal()),
                        LocalDateTime.parse(date_time, Formater.DATE_TIME_FORMATTER),
                        amount.toBigDecimal())
                }
                TypeOperation.PROFIT -> {
                    requireNotNull(category_id)
                    requireNotNull(category_title)
                    ProfitTransaction(id, General(from_id,from_title,from_balance.toBigDecimal()), LocalDateTime.parse(date_time,
                        Formater.DATE_TIME_FORMATTER),
                        Category(category_id,category_title,null),amount.toBigDecimal())

                }
                TypeOperation.LOSS -> {
                    requireNotNull(category_id)
                    requireNotNull(category_title)
                    LossTransaction(id, General(from_id,from_title,from_balance.toBigDecimal()), LocalDateTime.parse(date_time,
                        Formater.DATE_TIME_FORMATTER),
                        Category(category_id,category_title,null),amount.toBigDecimal())
                }
            }
        }.executeAsList()
    }
    fun getOperationsDaoByFromStorages(storagesId: List<Long>): List<Operation>{
        return queries.selectFullOperationsByFromStorages(storagesId)
        { id,
          amount,
          date_time,
          from_storage,
          to_storage,
          category,
          type_operation,
          from_id,
          from_title,
          from_balance,
          to_id,
          to_title,
          to_balance,
          category_id,
          category_title,
          type_id,
          type_title ->
            requireNotNull(from_id)
            requireNotNull(from_title)
            requireNotNull(from_balance)
            requireNotNull(type_title)

            when(TypeOperation.valueOf(type_title)){
                TypeOperation.TRANSFER -> {
                    requireNotNull(to_id)
                    requireNotNull(to_title)
                    requireNotNull(to_balance)
                    TransferTransaction(id,
                        General(from_id,from_title,from_balance.toBigDecimal()),
                        General(to_id,to_title,to_balance.toBigDecimal()),
                        LocalDateTime.parse(date_time, Formater.DATE_TIME_FORMATTER),
                        amount.toBigDecimal())
                }
                TypeOperation.PROFIT -> {
                    requireNotNull(category_id)
                    requireNotNull(category_title)
                    ProfitTransaction(id, General(from_id,from_title,from_balance.toBigDecimal()), LocalDateTime.parse(date_time,
                        Formater.DATE_TIME_FORMATTER),
                        Category(category_id,category_title,null),amount.toBigDecimal())

                }
                TypeOperation.LOSS -> {
                    requireNotNull(category_id)
                    requireNotNull(category_title)
                    LossTransaction(id, General(from_id,from_title,from_balance.toBigDecimal()), LocalDateTime.parse(date_time,
                        Formater.DATE_TIME_FORMATTER),
                        Category(category_id,category_title,null),amount.toBigDecimal())
                }
            }
        }.executeAsList()
    }

}