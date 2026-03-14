package org.example.model.service

import org.example.model.domain.CreditTransaction
import org.example.model.domain.DebitTransaction
import org.example.model.domain.Operation
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.domain.TransferTransaction
import org.example.model.repository.IOperationRepository

class OperationService(private val repo: IOperationRepository) {
    fun getOperations(storage: Storage): StateDomainList<Operation>{
        val list = repo.getOperationsByStorage(storage.id)
        return if (list.isEmpty()) StateDomainList.Empty
        else StateDomainList.Success(list)
    }
}