package org.example

import java.time.Period

class OperationViewModel(private val operations: MutableList<Operation> = mutableListOf()){
    fun addGeneralTransaction(tx: GeneralTransaction){
        operations.add(tx)
    }
    fun delGeneralTransaction(tx: GeneralTransaction){
        operations.remove(tx)
    }

    fun addTransferTransaction(tx: TransferTransaction){
        operations.add(tx)
    }
    fun delTransferTransaction(tx: TransferTransaction){
        operations.remove(tx)
    }


    fun listOperations(): List<String> {
        return operations.map {
            when (it) {
                is GeneralTransaction -> "${it.dateTime}: ${it.typeOperation} ${it.amount} (${it.category.title})"
                is TransferTransaction -> "${it.dateTime}: Перевод ${it.amount} → ${it.toStorage.title}"
                else -> "Неизвестная операция"
            }
        }
    }

}

class StorageViewModel(private val storages: MutableSet<Storage> = mutableSetOf()){
    fun addGeneralStorage(st: General){
        storages.add(st)
    }
    fun getStorage(index: Int): Storage{

    }
    fun delGeneralStorage(st: General){
        storages.add(st)
    }
    fun replaceGeneralStorage(old: Storage, new: Storage){
        storages.remove(old)
        storages.add(new)
    }
    fun listStorage(): List<String> {
        return storages.map {"${it}" }
    }

}