package org.example.model.repository.sqldelightdb

import com.example.OperationQueries
import com.example.TransferQueries
import org.example.model.domain.Operation
import org.example.model.repository.IOperationRepository

class OperationRepositorySQLDelight(private val queriesOp: OperationQueries, private val queriesTf: TransferQueries):
    IOperationRepository {
    //TODO()
}