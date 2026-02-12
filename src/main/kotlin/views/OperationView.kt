package org.example.views

import org.example.model.domain.Operation
import org.example.viewmodels.OperationViewModel

class OperationView(private val operationViewModel: OperationViewModel){
    fun start(printStorages:()-> Unit ){
        var input: String
        while (true){
            showOperationMenu()
            input = readln()
            useAction(input.toInt(), printStorages)
            if (input.toInt() == 0){
                break
            }
        }
    }
    private fun showOperationMenu() {
        println("====================================")
        println("             ОПЕРАЦИИ")
        println("====================================")
        println("TODO 1. Create operation")
        println("TODO 2. List of operations for all accounts ")
        println("3. List operations by storage(s)")
        println("TODO 4. List operations by category")
        println("TODO 5. List operations by type")
        println("TODO 6. Edit operation")
        println("TODO 7. Delete operation")
        println("0. Back")
        print("Choose option: ")
    }

    private fun useAction(num: Int, printStorages: () -> Unit){
        when (num) {
            1 -> {TODO("НЕСДЕЛАЛ {OperationView useAction 1}")}
            2 -> {TODO("НЕСДЕЛАЛ {OperationView useAction 2}")}
            3 -> {printListOperationsByStorage(printStorages)}
            4 -> {TODO("НЕСДЕЛАЛ {OperationView useAction 4}")}
            5 -> {TODO("НЕСДЕЛАЛ {OperationView useAction 5}")}
            6 -> {TODO("НЕСДЕЛАЛ {OperationView useAction 6}")}
            7 -> {TODO("НЕСДЕЛАЛ {OperationView useAction 7}")}
            else -> return
        }
    }

    private fun printListOperationsByStorage(printStorages: () -> Unit){
        //Выводим список счетов, просим указать id всех интересующих и записываем в список
        val currentsStoragesId: MutableList<Long> = mutableListOf()
        var input: String
        printStorages()
        println("ВВЕДИТЕ ID СЧЕТОВ ПО КОТОРЫМ НАДО ОПЕРАЦИИ. ПОСЛЕ ПОСЛЕДНЕГО НАЖМИТЕ ENTER/RETURN ")
        while (true){
            //TODO() Нет проверок на корректность веденных данных
            input = readln()
            if (input=="") break
            if (input.toIntOrNull() != null){
                currentsStoragesId.add(input.toLong())
            }
        }
        //Получаем список операций по нужным счетам и выводим на экран
        val operations = operationViewModel.getListOperationsByStorages(currentsStoragesId)
        for (op in operations){
            //op.printOperation()
        }
        //из viewModel я должен получить список по выбранным счетам
    }

    fun getListOperationsByStorage(storageId: Long): List<Operation>{
        return operationViewModel.getListOperationsByStorages(listOf(storageId))
    }

    fun printOperations(listOperations: List<Operation>){
        for (op in listOperations){
            //op.printOperation()
        }
    }


}