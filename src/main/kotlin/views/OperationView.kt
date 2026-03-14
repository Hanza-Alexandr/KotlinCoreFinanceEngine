package org.example.views

import org.example.NavigationIntent
import org.example.ViewService
import org.example.model.domain.BaseStorage
import org.example.model.domain.Category
import org.example.model.domain.CreditTransaction
import org.example.model.domain.DebitTransaction
import org.example.model.domain.GeneralTransaction
import org.example.model.domain.Operation
import org.example.model.domain.ResultMenu
import org.example.model.domain.StateDomain
import org.example.model.domain.StatusOperation
import org.example.model.domain.Storage
import org.example.model.domain.TransferTransaction
import org.example.model.domain.TypeOperation
import org.example.model.domain.TypeStorage
import org.example.viewmodels.OperationViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass

class OperationView(private val operationViewModel: OperationViewModel, private val categoryView: CategoryView){
    fun startOperationMenu(operationId: Int, operationClass: KClass<out Operation>, selectStorage: () -> ResultMenu<Storage>): ResultMenu<Operation>{
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
                    when(val resMenu = startEditStorageMenu(currentOperation, selectStorage)){
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

    private fun startEditStorageMenu(operation: Operation, selectStorage: () -> ResultMenu<Storage>): ResultMenu<Operation>{
        return when(operation){
            is GeneralTransaction -> startEditingGeneralOperation(operation,selectStorage)
            is TransferTransaction -> startEditingTransferOperation(operation, selectStorage)
            else -> throw IllegalArgumentException()
        }
    }
    private fun startEditingGeneralOperation(operation: GeneralTransaction, selectStorage: () -> ResultMenu<Storage>): ResultMenu<GeneralTransaction>{
        var newFromStorage: Storage? =null
        var newCategory: Category? = null
        var newAmount: BigDecimal? = null
        var newDate: LocalDate? = null
        var newTime: LocalTime? = null
        var newStaus: StatusOperation? = null

        while (true){
            ViewService.printHeadersForMenu("Editing operation menu", "|${operation.id}|${operation.storage.name}|${operation.category.name}|${operation.amount}|${operation.date}|${operation.time}|${operation.status.name}")
            ViewService.printActionsForMenu("-1. Выйти","1. Изменить счет","2. Изменить категорию","3. Редактировать сумму", "4. Изменить дату", "5. Изменить время", "6. Изменить статус", "7. SAVE")
            println("Не сохраненные изменения: |${if (newFromStorage!=null)"$newCategory" else ""}|${if (newAmount != null) "$newDate" else ""}|${if (newTime != null) "$newStaus" else ""}|}")
            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            when(inp){
                -1 -> return ResultMenu.NavigationOnly(NavigationIntent.Exit)
                1 -> {
                    newFromStorage = when(val selectState = selectStorage()){
                        is ResultMenu.Exception -> return ResultMenu.Exception(selectState.message)
                        is ResultMenu.Complete -> selectState.item
                        is ResultMenu.NavigationOnly -> continue
                    }
                }
                2 -> {
                    newCategory = when(val selectState = categoryView.startCategorySelectionMenu()){
                        is ResultMenu.Exception -> return ResultMenu.Exception(selectState.message)
                        is ResultMenu.Complete -> selectState.item
                        is ResultMenu.NavigationOnly -> continue
                    }
                }
                3 -> {
                    print("Amount: ")
                    newAmount = readln().toBigDecimal()
                }
                4 -> {
                    print("Date:")
                    newDate= LocalDate.parse(readln())
                }
                5 -> {
                    print("Time:")
                    newTime= LocalTime.parse(readln())
                }
                6 -> {
                    newStaus = StatusOperation.selectStatus() ?: continue
                }
                7 -> {
                    return when(val changingState = operationViewModel.updateOperation(operation,newFromStorage,newCategory,newAmount,newDate,newTime,newStaus)){
                        is StateDomain.Error -> ResultMenu.Exception(changingState.message)
                        is StateDomain.Success -> {
                            when(changingState.domain){
                                is GeneralTransaction -> {
                                    println("✅Успешно")
                                    ResultMenu.Complete(changingState.domain, NavigationIntent.Exit)
                                }
                                else -> throw IllegalArgumentException()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun startEditingTransferOperation(transfer: TransferTransaction, selectStorage: ()-> ResultMenu<Storage>): ResultMenu<TransferTransaction>{
        var newFromStorage: Storage? =null
        var newToStorage: Storage? = null
        var newAmount: BigDecimal? = null
        var newDate: LocalDate? = null
        var newTime: LocalTime? = null
        var newStaus: StatusOperation? = null

        while (true){
            ViewService.printHeadersForMenu("Editing operation menu", "|${transfer.id}|${transfer.fromStorage.name}|${transfer.toStorage.name}|${transfer.amount}|${transfer.date}|${transfer.time}|${transfer.status.name}")
            ViewService.printActionsForMenu("-1. Выйти","1. Изменить счет получателя","2. Изменить счет отправителя","3. Редактировать сумму", "4. Изменить дату", "5. Изменить время", "6. Изменить статус", "7. SAVE")
            println("Не сохраненные изменения: |${if (newFromStorage!=null)"$newToStorage" else ""}|${if (newAmount != null) "$newDate" else ""}|${if (newTime != null) "$newStaus" else ""}|}")
            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            when(inp){
                -1 -> return ResultMenu.NavigationOnly(NavigationIntent.Exit)
                1 -> {
                    newFromStorage = when(val selectState = selectStorage()){
                        is ResultMenu.Exception -> return ResultMenu.Exception(selectState.message)
                        is ResultMenu.Complete -> selectState.item
                        is ResultMenu.NavigationOnly -> continue
                    }
                }
                2 -> {
                    newFromStorage = when(val selectState = selectStorage()){
                        is ResultMenu.Exception -> return ResultMenu.Exception(selectState.message)
                        is ResultMenu.Complete -> selectState.item
                        is ResultMenu.NavigationOnly -> continue
                    }
                }
                3 -> {
                    print("Amount: ")
                    newAmount = readln().toBigDecimal()
                }
                4 -> {
                    print("Date:")
                    newDate= LocalDate.parse(readln())
                }
                5 -> {
                    print("Time:")
                    newTime= LocalTime.parse(readln())
                }
                6 -> {
                    newStaus = StatusOperation.selectStatus() ?: continue
                }
                7 -> {
                    return when(val changingState = operationViewModel.updateTransfer(transfer,newFromStorage,newToStorage,newAmount,newDate,newTime,newStaus)){
                        is StateDomain.Error -> ResultMenu.Exception(changingState.message)
                        is StateDomain.Success -> {
                            when(changingState.domain){
                                is TransferTransaction -> {
                                    println("✅Успешно")
                                    ResultMenu.Complete(changingState.domain, NavigationIntent.Exit)
                                }
                                else -> throw IllegalArgumentException()
                            }
                        }
                    }
                }
            }
        }
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