package org.example.viewmodels

import org.example.Operation
import org.example.repository.OperationRepository

class OperationViewModel(private val operationRepository: OperationRepository){


    fun getListOperationsByStorages(listStorageId: List<Long>): List<Operation>{

        val listTo = operationRepository.getOperationsDaoByToStorages(listStorageId)
        //val listFrom = operationRepository.getOperationsDaoByFromStorages(listStorageId)
        val list = operationRepository.getOperationsDaoByToStorages(listStorageId)
        return list
        //return (listTo+listFrom).toSet().toList()
    }

}

