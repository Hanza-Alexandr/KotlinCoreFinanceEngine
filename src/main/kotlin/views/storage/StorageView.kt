package org.example.views.storage

import org.example.viewmodels.StorageViewModel
import org.example.views.OperationView

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
        println("3. Operations")
        println("4. Edit storage (TODO)")
        println("5. Delete storage (TODO)")
        println("0. Back")
        print("Choose option: ")
    }
    private fun useAction(num: Int) {
        when (num) {
            1 -> displayMenuStorageCreation()
            2 -> printStorages()
            3 -> operationView.start { printStorages() }
        }
    }

    private fun displayMenuStorageCreation() {
        println("Меню создания счета:")

        print("Название: ")
        val name = readln()

        print("Валюта (например USD): ")
        val currency = readln()

        print("Тип счета (например CARD): ")
        val type = readln()

        print("Описание (можно пусто): ")
        val note = readln().ifBlank { null }

        when (val state = storageViewModel.createStorage(name, currency, type, note)) {
            is StorageState.Success -> println("✅ Счёт успешно создан")
            is StorageState.Error -> println("❌ Ошибка: ${state.message}")
        }
    }

    private fun printStorages() {
        when (val state = storageViewModel.getStorages()) {

            StorageListState.Empty -> {
                println("СЧЕТОВ НЕТ")
            }

            is StorageListState.Success -> {
                println("Список счетов:")
                state.storages.forEach {
                    println("${it.name} | ${it.type} | ${it.currency}")
                }
            }
        }
    }

}