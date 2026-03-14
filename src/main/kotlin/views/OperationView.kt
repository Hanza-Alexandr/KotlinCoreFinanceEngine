package org.example.views

import org.example.NavigationIntent
import org.example.ViewService
import org.example.model.domain.CreditTransaction
import org.example.model.domain.DebitTransaction
import org.example.model.domain.GeneralTransaction
import org.example.model.domain.NeedCategory
import org.example.model.domain.Operation
import org.example.model.domain.ResultMenu
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.StatusOperation
import org.example.model.domain.Storage
import org.example.model.domain.TransferTransaction
import org.example.model.domain.TypeOperation
import org.example.viewmodels.OperationViewModel
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass

class OperationView(private val operationViewModel: OperationViewModel, private val categoryView: CategoryView){
    fun startOperationMenu(operationId: Int, operationClass: KClass<out Operation>): ResultMenu<Operation>{
        while (true){
            val currentOperation = when(val state = operationViewModel.getOperation(operationId,operationClass)){
                is StateDomain.Success -> {
                    state.domain
                }
                is StateDomain.Error -> {
                    return ResultMenu.Exception(state.message)
                }
            }
            ViewService.printHeadersForMenu("Меню операции")
            displayOperation(currentOperation)
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

        return when(val stateDelete = operationViewModel.delete(operation)){
            is StateDomain.Error -> ResultMenu.Exception(stateDelete.message)
            is StateDomain.Success -> {
                println("✅Успешно")
                return ResultMenu.Complete(stateDelete.domain, NavigationIntent.Exit)
            }
        }
    }

    fun startCreationMenu(storage: Storage, selectStorage: ()-> ResultMenu<Storage>): ResultMenu<Operation>{
        while (true) {
            ViewService.printHeadersForMenu("Create operation menu")
            ViewService.printActionsForMenu("-1. Exit", "1. Создать доход", "2. Создать расход", "3. Создать перевод")
            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            return when(inp){
                -1 -> ResultMenu.NavigationOnly(NavigationIntent.Exit)
                1 ->  startCreateGeneralOperationMenu(TypeOperation.DEBIT,storage)
                2 -> startCreateGeneralOperationMenu(TypeOperation.CREDIT,storage)
                3 -> startCreateTransferOperation(storage, selectStorage)
                else -> {println("❌Нет такого действия"); continue}
            }
        }
    }

    fun startCreateGeneralOperationMenu(type: TypeOperation, fromStorage: Storage): ResultMenu<GeneralTransaction>{
        while (true){
            ViewService.printHeadersForMenu("Creation")
            val category = when(val stateSelectCategory = categoryView.startCategorySelectionMenu()){
                is ResultMenu.Exception -> return ResultMenu.Exception(stateSelectCategory.message)
                is ResultMenu.NavigationOnly -> continue
                is ResultMenu.Complete -> stateSelectCategory.item
            }
            print("СУММА:")
            val amount = readln().toBigDecimal()
            val time = LocalTime.now()
            val date = LocalDate.now()
            val status = StatusOperation.selectStatus() ?: continue
            val stateCreate = operationViewModel.createOperation(fromStorage, category, amount, time,date, status, type)
            return when(stateCreate){
                is StateDomain.Error -> ResultMenu.Exception(stateCreate.message)
                is StateDomain.Success -> ResultMenu.Complete(stateCreate.domain, NavigationIntent.Exit)
            }
        }
    }
    fun startCreateTransferOperation(fromStorage: Storage, selectStorage: ()-> ResultMenu<Storage>): ResultMenu<TransferTransaction>{
        while (true){
            ViewService.printHeadersForMenu("Creation")
            val toStorage = when(val stateSelectStorage= selectStorage()){
                is ResultMenu.Exception -> return ResultMenu.Exception(stateSelectStorage.message)
                is ResultMenu.NavigationOnly -> continue
                is ResultMenu.Complete -> stateSelectStorage.item
            }
            print("СУММА:")
            val amount = readln().toBigDecimal()
            val time = LocalTime.now()
            val date = LocalDate.now()
            val status = StatusOperation.selectStatus() ?: continue
            val stateCreate = operationViewModel.createTransfer(fromStorage, toStorage, amount, time,date, status)
            return when(stateCreate){
                is StateDomain.Error -> ResultMenu.Exception(stateCreate.message)
                is StateDomain.Success -> ResultMenu.Complete(stateCreate.domain, NavigationIntent.Exit)
            }
        }
    }


    fun displayOperation(operation: Operation){
        when(operation){
            is DebitTransaction -> {
                println("${operation.id}|ПРИХОД|${operation.date}|${operation.time}|+${operation.amount}|${operation.category}|$")
            }
            is CreditTransaction -> {
                println("${operation.id}|РАСХОД|${operation.date}|${operation.time}|-${operation.amount}|${operation.category}|$")
            }
            is TransferTransaction -> {
                println("${operation.id}|ПЕРЕВОД|${operation.date}|${operation.time}|${operation.fromStorage} --${operation.amount}--> ${operation.toStorage}|")
            }
            else -> throw IllegalArgumentException("Поступил неверный тип операции")
        }
    }

    fun displayListOperationsByStorage(storage: Storage){
        val stateListOperation = operationViewModel.getOperations(storage)
        ViewService.printListDomain(stateListOperation){
            displayOperation(it)
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