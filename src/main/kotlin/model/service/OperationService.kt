package org.example.model.service

import jdk.jfr.DataAmount
import org.example.model.domain.Category
import org.example.model.domain.CreditTransaction
import org.example.model.domain.DebitTransaction
import org.example.model.domain.GeneralTransaction
import org.example.model.domain.NewGeneralOperation
import org.example.model.domain.NewOperation
import org.example.model.domain.NewTransferTransaction
import org.example.model.domain.Operation
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.StatusOperation
import org.example.model.domain.Storage
import org.example.model.domain.TransferTransaction
import org.example.model.domain.TypeOperation
import org.example.model.repository.IOperationRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass

class OperationService(private val repo: IOperationRepository) {
    fun getOperations(storage: Storage): StateDomainList<Operation>{
        val list = repo.getOperationsByStorage(storage.id)
        return if (list.isEmpty()) StateDomainList.Empty
        else StateDomainList.Success(list)
    }

    fun getOperation(operationId: Int, operationClass: KClass<out Operation>): StateDomain<Operation>{
        when(operationClass){
            TransferTransaction::class  -> {
                val getState = repo.getTransferById(operationId.toLong()) ?: return StateDomain.Error("❌Ошибка при получении перевода")
                return StateDomain.Success(getState)
            }
            GeneralTransaction::class-> {
                val getState = repo.getOperationById(operationId.toLong()) ?: return StateDomain.Error("❌Ошибка при получении операции")
                return StateDomain.Success(getState)
            }
            else -> throw IllegalArgumentException()
        }
    }
    fun delete(operation: Operation): StateDomain<Operation>{
        return when(val resDel = repo.delete(operation)){
            null -> StateDomain.Error("❌Ошибка удаления")
            else -> StateDomain.Success(resDel)
        }
    }

    fun createTransfer(fromStorage: Storage, toStorage: Storage, amount: BigDecimal, time: LocalTime, date: LocalDate, status: StatusOperation): StateDomain<TransferTransaction>{
        if(fromStorage.id == toStorage.id) return StateDomain.Error("❌Счет отправления не может совпадать со счетом получения")
        return when(val localStateCreate = NewTransferTransaction.create(fromStorage,toStorage,amount,time,date,status)){
            is StateDomain.Error -> StateDomain.Error(localStateCreate.message)
            is StateDomain.Success -> {
                when (val stateCreate = repo.save(localStateCreate.domain)){
                    null -> StateDomain.Error("❌Ошибка при создании")
                    is TransferTransaction -> StateDomain.Success(stateCreate)
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }
    fun createOperation(storage: Storage, category: Category, amount: BigDecimal, time: LocalTime, date: LocalDate, status: StatusOperation, typeOperation: TypeOperation): StateDomain<GeneralTransaction>{
        return when(val localStateCreate = NewGeneralOperation.create(storage, category,amount,time,date,status,typeOperation)){
            is StateDomain.Error -> StateDomain.Error(localStateCreate.message)
            is StateDomain.Success -> {
                when (val stateCreate = repo.save(localStateCreate.domain)){
                    null -> StateDomain.Error("❌Ошибка при создании")
                    is GeneralTransaction -> StateDomain.Success(stateCreate)
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }
}