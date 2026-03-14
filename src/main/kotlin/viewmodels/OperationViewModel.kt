package org.example.viewmodels

import org.example.model.domain.Operation
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.service.OperationService

class OperationViewModel(private val service: OperationService){
    fun getOperation(operationId: Int): StateDomain<Operation>{
        TODO()
    }
    fun getOperations(storage: Storage): StateDomainList<Operation>{
        return service.getOperations(storage)
    }
}

