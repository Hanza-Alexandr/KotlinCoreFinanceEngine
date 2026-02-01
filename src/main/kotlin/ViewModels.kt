package org.example

import com.example.StorageQueries
import com.example.StorageDao

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

class StorageViewModel( val storageQueries: StorageQueries){

    fun getList(): List<StorageDao>{
        val aasd = mutableListOf<StorageDao>()
        storageQueries.selectAllStorageDao{id, title, start_balance -> aasd.add(StorageDao(id,title,start_balance))}
        return aasd.toList()
    }
    fun addGeneralStorage(st: General){
        storageQueries.insertStorageDao(st.title,st.startBalance.toDouble())
    }
    fun getStorage(st: Storage){

    }
    fun getStorage(id: Int){

    }
    fun delGeneralStorage(st: General){

    }
    fun replaceGeneralStorage(old: Storage, new: Storage){

    }
    fun listStorage(): List<String> {
        TODO()
    }
}