package org.example.views

import org.example.NavigationIntent
import org.example.ViewService
import org.example.model.domain.CreditTransaction
import org.example.model.domain.DebitTransaction
import org.example.model.domain.Operation
import org.example.model.domain.ResultMenu
import org.example.model.domain.StateDomain
import org.example.model.domain.TransferTransaction
import org.example.viewmodels.OperationViewModel

class OperationView(private val operationViewModel: OperationViewModel){
    fun startOperationMenu(operationId: Int): ResultMenu<Operation>{
        while (true){
            val currentOperation = when(val state = operationViewModel.getOperation(operationId)){
                is StateDomain.Success -> {
                    state.domain
                }
                is StateDomain.Error -> {
                    return ResultMenu.Exception(state.message)
                }
            }
            ViewService.printHeadersForMenu("Меню операции", displayOperations(currentOperation).toString())
            ViewService.printActionsForMenu("-1. Выйти", "-2. Изменить", "-3. Удалить")
            ViewService.printBottom()
            ViewService.printHeaderChoose()
            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            when(inp){
                -1 -> {
                    return ResultMenu.NavigationOnly(NavigationIntent.Exit)
                }
                -2 -> {
                    when(val resMenu = startEditStorageMenu(currentOperation)){
                        is ResultMenu.NavigationOnly -> continue
                        else -> return resMenu
                    }
                }
                -3 -> {
                    when(val resMenu = startDeleteStorageMenu(currentOperation)){
                        is ResultMenu.NavigationOnly -> continue
                        else -> return resMenu
                    }
                }
                else -> {
                    println("⚠️Нет такого действия")
                    continue
                }
            }
        }

    }

    private fun startEditStorageMenu(operation: Operation): ResultMenu<Operation>{
        TODO()
    }

    private fun startDeleteStorageMenu(operation: Operation): ResultMenu<Operation>{
        TODO()
    }

    fun startCreationMenu(): ResultMenu<Operation>{
        TODO()
    }

    fun displayOperations(operation: Operation){
        when(operation){
            is DebitTransaction -> {
                println("ПРИХОД|${operation.date}|${operation.time}|+${operation.amount}|${operation.category}|$")
            }
            is CreditTransaction -> {
                println("РАСХОД|${operation.date}|${operation.time}|-${operation.amount}|${operation.category}|$")
            }
            is TransferTransaction -> {
                println("ПЕРЕВОД|${operation.date}|${operation.time}|${operation.fromStorage} --${operation.amount}--> ${operation.toStorage}|")
            }
            else -> throw IllegalArgumentException("Поступил неверный тип операции")
        }
    }
}
//Счета без счетов не имеют пока ни какого смысла.
//Поэтому пока делаем view конкретно для одной операции

//МЕНЮ ОПЕРАЦИИ
//Отображение: атрибутов операции в зависимости от ее типа(перевод, дебит, кредит)
//Действия: удалить, редактировать,

//Доп функции
//Отображение списка