package org.example.model.service

import org.example.model.domain.CreditTransaction
import org.example.model.domain.DebitTransaction
import org.example.model.domain.Operation
import org.example.model.domain.TransferTransaction
import org.example.model.repository.IOperationRepository

class OperationService(private val repo: IOperationRepository) {
    fun getOperations(): List<Operation>{
        TODO()
    }
    fun getTransferTransactions(): List<TransferTransaction>{
        TODO()
    }
    fun getDebitTransactions(): List<DebitTransaction>{
        TODO()
    }
    fun getCreditTransaction(): List<CreditTransaction>{
        TODO()
    }
}