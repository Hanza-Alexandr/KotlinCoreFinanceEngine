package org.example.model.repository.sqldelightdb

import com.example.OperationQueries
import com.example.TransferQueries
import org.example.model.domain.NewOperation
import org.example.model.domain.Operation
import org.example.model.repository.IOperationRepository

class OperationRepositorySQLDelight(private val queriesOp: OperationQueries, private val queriesTf: TransferQueries): IOperationRepository {
    override fun getAll(): List<Operation> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Operation? {
        TODO("Not yet implemented")
    }

    override fun save(newOperation: NewOperation): Operation? {
        TODO("Not yet implemented")
    }

    override fun save(operation: Operation): Operation? {
        TODO("Not yet implemented")
    }

    override fun delete(operation: Operation): Operation? {
        TODO("Not yet implemented")
    }
}