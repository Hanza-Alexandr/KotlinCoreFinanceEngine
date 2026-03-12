package org.example.views

import org.example.NavigationIntent
import org.example.ViewService
import org.example.model.domain.BaseStorage
import org.example.model.domain.Currency
import org.example.model.domain.ExistColor
import org.example.model.domain.ResultMenu
import org.example.model.domain.StateDomain
import org.example.model.domain.Storage
import org.example.model.domain.TypeStorage
import org.example.viewmodels.StorageViewModel
import java.awt.Color
import javax.swing.text.View

class StorageView(private val storageViewModel: StorageViewModel, private val operationView: OperationView, private val colorView: ColorView){
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
            ViewService.printHeadersForMenu("Storage menu creation")
            println("Название:")
            val name = readln()
            if (!BaseStorage.isNameValid(name)) {println("❌Некорректное имя"); continue}
            println("Валюта:")
            val currency: Currency = when(val stateCurrency = Currency.selectCurrency()){
                is ResultMenu.NavigationOnly -> continue
                is ResultMenu.Exception -> {println(stateCurrency.message); continue}
                is ResultMenu.Complete -> stateCurrency.item
            }
            println("Тип:")
            val typeStorage: TypeStorage = when(val stateTypeStorage = TypeStorage.selectTypeStorage()){
                is ResultMenu.NavigationOnly -> continue
                is ResultMenu.Exception -> {println(stateTypeStorage.message); continue}
                is ResultMenu.Complete -> stateTypeStorage.item
            }
            println("Заметка:")
            val inpNote: String = readln() //Ну да так калечно. че поделать
            val note =if(inpNote=="") null else inpNote
            if (!BaseStorage.isNoteValid(note)) {println("❌Некорректная заметка"); continue}
            val color = when(val stateColor = colorView.startColorSelectionMenu()){
                is StateDomain.Error -> {println(stateColor.message); continue}
                is StateDomain.Success -> stateColor.domain
            }
            when(val stateCreate = storageViewModel.createStorage(name,currency,typeStorage, note,color)){
                is StateDomain.Error -> return ResultMenu.Exception(stateCreate.message)
                is StateDomain.Success -> {
                    println("✅Успешно");
                    return ResultMenu.Complete(stateCreate.domain, NavigationIntent.Exit)
                }
            }
        }
    }
    private fun startEditingStorageMenu(storage: Storage): ResultMenu<Storage>{
        var newName: String? = null
        var newNote: String? = null
        var newType: TypeStorage? = null
        var newColor: ExistColor? = null
        var newStatistics: Boolean? = null
        var newArchive: Boolean? = null
        while (true){
            ViewService.printHeadersForMenu("Editing storage menu", "|${storage.id}|${storage.name}|${storage.currency.name}|${storage.typeStorage.name}|${storage.note}|${storage.color.hexCode}|Саттистика = ${storage.isStatistics}| архив = ${storage.isArchive}")
            ViewService.printActionsForMenu("-1. Выйти","1. Изменить название","2. Изменить тип","3. Редактировать заметку", "4. Изменить цвет", "5. Убрать/включить в статистику", "6. Убрать/вернуть из архива","7. Save")
            println("Не сохраненные изменения: |${if (newName!=null)"$newName" else ""}|${if (newNote != null) "$newNote" else ""}|${if (newType != null) "$newType" else ""}|${if (newColor != null) "$newColor" else ""}|${if (newStatistics != null) "$newStatistics" else ""}|${if (newArchive != null) "$newArchive" else ""}")
            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            when(inp){
                -1 -> return ResultMenu.NavigationOnly(NavigationIntent.Exit)
                1 -> {
                    println("newName: ")
                    newName = readln()
                    if (!BaseStorage.isNameValid(newName)) {println("❌name invalid");continue}
                }
                2 -> {
                    when(val stateType = TypeStorage.selectTypeStorage()){
                        is ResultMenu.Exception -> {
                            println(stateType.message)
                            continue
                        }
                        is ResultMenu.Complete -> newType= stateType.item
                        is ResultMenu.NavigationOnly -> continue
                    }
                }
                3 -> {
                    println("newNote: ")
                    newNote = readln()
                    if (!BaseStorage.isNoteValid(newNote)) {println("❌note invalid");continue}
                }
                4 -> {
                    when(val stateColor = colorView.startColorSelectionMenu()){
                        is StateDomain.Error -> {
                            println(stateColor.message)
                            continue
                        }
                        is StateDomain.Success -> newColor = stateColor.domain
                    }
                }
                5 -> {
                    newStatistics = storage.isStatistics
                }
                6 -> {
                    newArchive = storage.isArchive
                }
                7 -> {
                    return when(val changingState = storageViewModel.update(storage, newName,newType,newNote,newColor,newStatistics,newArchive)){
                        is StateDomain.Error -> ResultMenu.Exception(changingState.message)
                        is StateDomain.Success -> {
                            println("✅Успешно")
                            ResultMenu.Complete(changingState.domain, NavigationIntent.Exit)
                        }
                    }
                }
            }
        }
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