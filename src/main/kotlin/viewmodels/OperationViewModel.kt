package org.example.viewmodels

import org.example.model.domain.Operation
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.service.OperationService
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
}

