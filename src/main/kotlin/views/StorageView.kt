package org.example.views

import org.example.Currency
import org.example.TypeStorage
import org.example.model.Color
import org.example.model.Storage
import org.example.viewmodels.StorageViewModel

class StorageView(private val storageViewModel: StorageViewModel, private val operationView: OperationView,){

    fun start(){
        var input: String
        while (true){
            //TODO() Нет проверок на корректность веденных данных
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
        println("5. Delete storage")
        println("0. Back")
        print("Choose option: ")
    }
    private fun useAction(num: Int){
        when (num) {
            1 -> {displayMenuStorageCreation()}
            2 -> {printStorages()}
            3 -> {operationView.start{ printStorages()} }
            4 -> {TODO("НЕСДЕЛАЛ {StorageView useAction 4}")}
            5 -> {TODO()}
            else -> return
        }
    }

    /*
    private fun displayMenuStorageDeletion(){
        var currentStorage: Storage
        var inputTittleStorage: String
        println("МЕНЮ УДАЛЕНИЯ СЧЕТА:")
        //TODO() Нет проверок на корректность веденных данных
        while (true){
            val listStorages = printStorages()
            println("Введите № счета которые нужно удалить")
            currentStorage = listStorages[readln().toInt()-1]
            println("УДАЛЕНИЕ ПРИВЕДЕТ К УДАЛЕНИЮ ВСЕХ СВЯЗАННЫХ ОПЕРАЦИЙ С СЧЕТОМ")
            println("Будут удалены следующие операции: ")
            operationView.printOperations(operationView.getListOperationsByStorage(currentStorage.id?:throw NullPointerException("Почему то ID не появилось")))
            print("ДЛЯ ПОДТВРЕЖДЕНИЯ УДАЛЕНИЯ ВВЕДИТЕ НАЗВАНИЕ СЧЕТА <${currentStorage.title}>:")
            inputTittleStorage = readln()
            if (inputTittleStorage == currentStorage.title){
                storageViewModel.deleteStorage(currentStorage.id ?: throw NullPointerException("Почему то ID не появилось"))
                break
            }
            println("НЕВЕРНО")
        }
    }

     */


    private fun displayMenuStorageCreation(){
        println("Меню создание счета:")
        var inp: String
        while (true){
            var name: String
            var currency: Currency
            var type: TypeStorage
            var note: String?

            print("Название:")
            name= readln()
            print("Валюта:")
            currency = Currency.valueOf(readln())
            println("Тип счета:")
            type = TypeStorage.valueOf(readln())
            println("Описание:")
            inp = readln()
            note = if (inp=="") null else inp;
                storageViewModel.createStorage(name,currency,type,note)
                println("✅Счет успешно создан")



        }
    }

    private fun printStorages(){
        val storages= storageViewModel.getListStorages()
        if (storages.isEmpty()) {println("СЧЕТОВ НЕТ"); return}
        for (s in storages){
            println("${s.name}|${s.typeStorage}|${s.currency}")
        }
    }
}