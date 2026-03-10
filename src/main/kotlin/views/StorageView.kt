package org.example.views

import org.example.NavigationIntent
import org.example.ViewService
import org.example.model.domain.ResultMenu
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.viewmodels.StorageViewModel

class StorageView(private val storageViewModel: StorageViewModel, private val operationView: OperationView,){
    fun startMainMenu(){
        while (true){
            when(val result = startStoragesMenu()){
                is ResultMenu.NavigationOnly -> return
                is ResultMenu.Exception ->{
                    println(result.message)
                    return
                }
                else -> continue
            }
        }
    }
    private fun startStoragesMenu(): ResultMenu<Storage> {
        while (true){
            ViewService.printHeadersForMenu("Меню счетов")
            ViewService.printListDomain(storageViewModel.getStorages()){
                println("|${it.id}|${it.name}|${it.currency.name}|${it.typeStorage}")
            }
            ViewService.printActionsForMenu("0. Создать новый счет", "-1. Выйти")
            ViewService.printBottom()
            ViewService.printHeaderChoose()
            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            when(inp){
                0 ->{
                    when(val result = startCreateStorageMenu()){
                        is ResultMenu.Exception -> {
                            println(result.message)
                            continue
                        }
                        else -> continue
                    }
                }
                -1 ->{
                    return ResultMenu.NavigationOnly(NavigationIntent.Exit)
                }
                else -> {
                    when(val result = startStorageMenu(inp)){
                        is ResultMenu.Exception -> {
                            println(result.message)
                            continue
                        }
                        else -> continue
                    }
                }
            }
        }
    }
    private fun startStorageMenu(storageId: Int): ResultMenu<Storage>{
        while (true){
            val currentStorage = when(val state = storageViewModel.getStorage(storageId)){
                is StateDomain.Success -> {
                    state.domain
                }
                is StateDomain.Error -> {
                    return ResultMenu.Exception(state.message)
                }
            }
            ViewService.printHeadersForMenu("Меню счета", currentStorage.name)
            displayOperationsByStorage(currentStorage)
            ViewService.printActionsForMenu("0. Добавить операцию", "-1. Выйти", "-2. Изменить", "-3. Удалить")
            ViewService.printBottom()
            ViewService.printHeaderChoose()
            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            when(inp){
                0 -> {
                    when(val result = operationView.startCreationMenu()){
                        is ResultMenu.Exception -> {
                            println(result.message)
                            continue
                        }
                        else -> continue
                    }
                }
                -1 -> {
                    return ResultMenu.NavigationOnly(NavigationIntent.Exit)
                }
                -2 -> {
                    when(val result = startEditingStorageMenu(currentStorage)){
                        is ResultMenu.Exception -> {
                            println(result.message)
                            continue
                        }
                        else -> continue
                    }
                }
                -3 -> {
                    when(val result = startDeleteMenu(currentStorage)){
                        is ResultMenu.Exception -> {
                            println(result.message)
                            continue
                        }
                        else -> continue
                    }
                }
                else -> {
                    when(val result = operationView.startOperationMenu(inp)){
                        is ResultMenu.Exception -> {
                            println(result.message)
                            continue
                        }
                        else -> continue
                    }
                }
            }
        }
    }
    private fun startDeleteMenu(storage: Storage): ResultMenu<Storage>{
        while (true){
            TODO()
        }
    }
    private fun startCreateStorageMenu(): ResultMenu<Storage>{
        while (true){
            TODO()
        }
    }
    private fun startEditingStorageMenu(storage: Storage): ResultMenu<Storage>{
        while (true){
            TODO()
        }
    }
    private fun displayOperationsByStorage(storage: Storage){
        TODO()
    }
}

//Отображать список счетов
// Действия: Выбрать счет(переход в МЕНЮ СЧЕТА); Создать новый счет(переход в МЕНЮ СОЗДАНИЯ СЧЕТОВ).

//МЕНЮ СЧЕТА
//Отображение атрибутов счета: название, валюта, тип счета, цвет, учитывать в статистике, в архиве
//Действия: открыть список операций по счету, удалить счет(МЕНЮ УДАЛЕНИЯ СЧЕТА); Изменить счет() ; Создать новую операцию

//МЕНЮ СОЗДАНИЯ СЧЕТОВ
//

//МЕНЮ УДАЛЕНИЯ СЧЕТА
//