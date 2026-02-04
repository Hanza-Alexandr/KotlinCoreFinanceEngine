package org.example.views

import org.example.General
import org.example.viewmodels.StorageViewModel
import java.security.Principal

class StorageView(private val storageViewModel: StorageViewModel, private val operationView: OperationView,){

    fun start(){
        var input: String
        while (true){
            showStorageMenu()
            input = readln()
            useAction(input.toInt())
            if (input.toInt() == 0){
                break
            }
        }
    }

    private fun showStorageMenu() {
        println("====================================")
        println("             СЧЕТА")
        println("====================================")
        println("1. Create storage")
        println("2. List storages")
        println("3. Operations (Операции)")
        println("TODO 4. Edit storage")
        println("TODO 5. Delete storage")
        println("0. Back")
        print("Choose option: ")
    }
    private fun useAction(num: Int){
        when (num) {
            1 -> {displayMenuStorageCreation()}
            2 -> {printStorages()}
            3 -> {operationView.start{ printStorages()} }
            4 -> {TODO("НЕСДЕЛАЛ {StorageView useAction 4}")}
            5 -> {TODO("НЕСДЕЛАЛ {StorageView useAction 5}")}
            else -> return
        }
    }
    private fun displayMenuStorageCreation(){
        while (true){
            println("Меню создание ОБЩЕГО счета:")
            print("Название:")
            val res =
            if (storageViewModel.createNewGeneralStorage(General(null,readln()))) {
                println("✅Счет успешно создан")
                printStorages()
                break
            }
            else{
                println("ОШИБКА СОЗДАНИЯ СЧЕТА")
            }
        }
    }
    private fun printStorages(){
        println("МОИ СЧЕТА:")
        for (storage in storageViewModel.getListStorages()) {
            println("ID - ${storage.id} TITLE - ${storage.title}")
        }
    }
}