package org.example.viewmodels

import org.example.model.domain.Category
import org.example.model.domain.GeneralTransaction
import org.example.model.domain.Operation
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.StatusOperation
import org.example.model.domain.Storage
import org.example.model.domain.TransferTransaction
import org.example.model.domain.TypeOperation
import org.example.model.service.OperationService
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass

class OperationViewModel(private val service: OperationService){
    fun getOperation(operationId: Int, operationClass: KClass<out Operation>): StateDomain<Operation>{
        return service.getOperation(operationId,operationClass)
    }
    fun getOperations(storage: Storage): StateDomainList<Operation>{
        return service.getOperations(storage)
    }

    fun delete(operation: Operation): StateDomain<Operation>{
        return service.delete(operation)
    }
    fun createOperation(storage: Storage, category: Category, amount: BigDecimal, time: LocalTime, date: LocalDate, status: StatusOperation, typeOperation: TypeOperation): StateDomain<GeneralTransaction>{
        return service.createOperation(storage, category,amount,time,date,status,typeOperation)
    }
    fun createTransfer(fromStorage: Storage, toStorage: Storage, amount: BigDecimal, time: LocalTime, date: LocalDate, status: StatusOperation): StateDomain<TransferTransaction>{
        return service.createTransfer(fromStorage,toStorage,amount,time,date,status)
    }
}


