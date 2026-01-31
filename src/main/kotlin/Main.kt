package org.example

import java.math.BigDecimal
import java.time.LocalDateTime

fun main() {
    val opVM = OperationViewModel()
    val stVM = StorageViewModel()
    val view = consoleView(opVM,stVM)


}

class StorageView(private val storageViewModel: StorageViewModel){
    fun showMenu(){
        println("1. Список Всех")
        println("2. Выбрать счет")
    }
    fun printList(){
        for (i in storageViewModel.listStorage()) println(i)
    }
    fun add(title: String, startBalance: BigDecimal){
        storageViewModel.addGeneralStorage(General(title,startBalance))
    }
    fun setNewTitle(storage: Storage,newTitle: String){
        storageViewModel.replaceGeneralStorage(storage, General(newTitle,storage.startBalance))
    }
    fun setNewStartBalance(storage: Storage,newStartBalance: BigDecimal){
        storageViewModel.replaceGeneralStorage(storage, General(storage.title,newStartBalance))
    }
    fun getStorage(index: Int): Storage{
        storageViewModel.
    }
}

class consoleView(
    private val operationViewModel: OperationViewModel,
    private val storageViewModel: StorageViewModel
){

}






