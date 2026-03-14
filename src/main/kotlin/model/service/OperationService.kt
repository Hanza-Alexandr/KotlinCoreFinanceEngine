package org.example.model.service

import org.example.model.domain.CreditTransaction
import org.example.model.domain.DebitTransaction
import org.example.model.domain.GeneralTransaction
import org.example.model.domain.Operation
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.domain.TransferTransaction
import org.example.model.repository.IOperationRepository
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
}