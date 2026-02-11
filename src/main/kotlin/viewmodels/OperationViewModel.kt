package org.example.viewmodels

import org.example.model.Operation
import org.example.model.service.OperationService

class OperationViewModel(private val service: OperationService){

    fun getListOperationsByStorages(listStorageId: List<Long>): List<Operation>{
       TODO()
    }
}

