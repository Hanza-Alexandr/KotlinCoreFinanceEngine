package org.example.model.service

import org.example.model.CreditTransaction
import org.example.model.DebitTransaction
import org.example.model.Operation
import org.example.model.TransferTransaction
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