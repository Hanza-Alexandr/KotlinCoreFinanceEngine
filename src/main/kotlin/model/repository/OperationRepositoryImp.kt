package org.example.model.repository

import com.example.OperationQueries
import com.example.TransferQueries
import org.example.model.Operation
import org.example.model.Storage

class OperationRepositoryImp(private val queriesOp: OperationQueries, private val queriesTf: TransferQueries): IOperationRepository {
    override fun getAllByStorage(storageId: Long): List<Operation> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Operation {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Operation> {
        TODO("Not yet implemented")
    }

    override fun save(operation: Operation) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }


}