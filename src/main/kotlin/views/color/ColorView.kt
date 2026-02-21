package org.example.views.color

import org.example.model.domain.Color
import org.example.viewmodels.ColorViewModel
import org.example.viewmodels.StateColor
import org.example.viewmodels.StateListColor
import org.example.NavigationIntent

class ColorView(private val colorViewModel: ColorViewModel) {
    fun start(){
        while (true){
            val state = startMainMenu()
            when(state){
                NavigationIntent.Back -> continue
                NavigationIntent.BackHome -> continue
                NavigationIntent.Exit -> return
            }
        }
    }
    private fun startMainMenu(): NavigationIntent {
        while (true){
            println("====================================")
            println("       Меню цветов")
            println("====================================")
            val stateListColor = colorViewModel.getColors()
            when(stateListColor){
                is StateListColor.Empty -> {
                    println("⚠️ПРЕДУПРЕЖДЕНИЕ: Нет ни одного цвета")
                }
                is StateListColor.Success ->{
                    val colorList = stateListColor.colors
                    displayColor(colorList)
                    println("<номер цвета>. Выбор цвета")

                }
            }
            println("0. Создать цвет")
            println("====================================")
            println("-1. Выйти")
            val inp = readln()
            if (inp.toIntOrNull()==null) {
                println("Нужен номер!!!")
                continue
            }
            else
            {
                val numberActionOrId = inp.toInt()

                when(numberActionOrId){
                    0 -> {
                        val navIntent = startColorCreationMenu()
                        when(navIntent){
                            NavigationIntent.Back -> continue
                            NavigationIntent.Exit -> return NavigationIntent.Exit
                            NavigationIntent.BackHome -> continue
                        }
                    }
                    -1 -> {
                        return NavigationIntent.Exit
                    }
                    else -> {
                        try {
                           val navIntent = startColorMenu(numberActionOrId)

                            when(navIntent){
                                NavigationIntent.Back -> continue
                                NavigationIntent.Exit -> return NavigationIntent.Exit
                                NavigationIntent.BackHome -> continue
                            }
                        }
                        catch (e: IllegalArgumentException){
                            println("❌ОШИБКА: Некорректный номер действия")
                            continue
                        }

                    }
                }


            }
        }

    }
    private fun startColorMenu(colorId: Int): NavigationIntent {
        while (true){
            println("====================================")
            println("       Меню цвета")
            println("====================================")
            when(val stateCurrentColor: StateColor = colorViewModel.getColor(colorId)){
                is StateColor.Error -> {
                    println(stateCurrentColor.message)
                    return NavigationIntent.BackHome
                }
                is StateColor.Success -> {
                    val currentColor = stateCurrentColor.color
                    if(currentColor.isSystem){
                        println("⚠️ПРЕДУПРЕЖДЕНИЕ: Системные цвета нельзя редактировать")
                        return NavigationIntent.BackHome
                    }
                    println("ЦВЕТ: ${currentColor.hexCode}")
                    println("1. Редактировать")
                    println("2. УДАЛИТЬ")
                    println("====================================")
                    println("-1. Назад")
                    println("-2. Выйти из меню цветов")

                    val inp = readln()
                    if (inp.toIntOrNull()==null) {
                        println("⚠️ПРЕДУПРЕЖДЕНИЕ: Нужен номер!!!")
                        continue
                    }
                    else {
                        val numberActionOrId = inp.toInt()
                        when (numberActionOrId) {
                            1 -> {
                                val navIntent = startColorEditingMenu(currentColor)
                                return when(navIntent){
                                    NavigationIntent.Back -> continue
                                    NavigationIntent.Exit -> NavigationIntent.Exit
                                    NavigationIntent.BackHome -> NavigationIntent.BackHome
                                }
                            }

                            2 -> {
                                val navIntent = startDeleteMenu(currentColor)
                                return when(navIntent){
                                    NavigationIntent.Back -> continue
                                    NavigationIntent.Exit -> NavigationIntent.Exit
                                    NavigationIntent.BackHome -> NavigationIntent.BackHome
                                }
                            }

                            -1 -> {
                                return NavigationIntent.Back
                            }

                            -2 -> {
                                return NavigationIntent.Exit
                            }

                            else -> {
                                try {
                                    startColorMenu(numberActionOrId)
                                } catch (e: IllegalArgumentException) {
                                    println("❌ОШИБКА: Некорректный номер действия")
                                    continue
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private fun startColorEditingMenu(currentColor: Color): NavigationIntent {
        while (true){
            println("====================================")
            println("       Меню редактирования цвета")
            println("====================================")
            println("-1. Назад")
            println("-2. Вернуться на главную")
            println("-3. Выйти из меню цветов")
            println("====================================")
            println("           Новый Hex_code")
            print("Hex_cod: #")
            val inp = readln()
            if (inp.toIntOrNull()==null){
                when(val stateEditing = colorViewModel.updateColor(oldColor = currentColor, newHexCode= inp)){
                    is StateColor.Error -> {
                        println(stateEditing.message)
                        return NavigationIntent.BackHome
                    }
                    is StateColor.Success -> {
                        println("✅Успешно")
                        println("Измененный цвет |${stateEditing.color.id}| ${stateEditing.color.hexCode}")
                        return NavigationIntent.BackHome
                    }
                }
            }
            else{
                return when(inp.toInt()){
                    -1 -> NavigationIntent.Back
                    -2 -> NavigationIntent.BackHome
                    -3 -> NavigationIntent.Exit
                    else -> {
                        println("❌ОШИБКА: Некорректный номер действия")
                        continue
                    }
                }
            }
        }
    }
    private fun startDeleteMenu(color: Color): NavigationIntent {
        while(true){
            val hasRelatedItems: Boolean = colorViewModel.hasRelations(color)
            if (hasRelatedItems){
                println("Этот цвет используется в записях!!!")
                println("====================================")
                println("1. Заменить в записях на стандартный")
                println("2. Заменить на другой")
                println("====================================")
                println("-1. Назад")
                println("-2. Вернуться на главную")
                println("-3. Выйти из меню цветов")
                println("====================================")
                val inp = readln()
                if (inp.toIntOrNull() == null) {
                    println("❌ОШИБКА: Некорректный ввод")
                    continue
                }
                else{
                    when(inp.toInt()){
                        1 -> {
                            val stateDelete = colorViewModel.deleteColor(color, null)
                            when(stateDelete){
                                is StateColor.Error -> println(stateDelete.message)
                                is StateColor.Success -> {
                                    println("✅Успешно")
                                    return NavigationIntent.BackHome
                                }
                            }
                        }
                        2 -> {
                            val newColor: Color
                            val stateNewColor = startColorSelectionMenu(color)
                            when(stateNewColor){
                                is StateColor.Error -> {
                                    println(stateNewColor.message)
                                    continue
                                }
                                is StateColor.Success -> {
                                    newColor = stateNewColor.color
                                }
                            }

                            val stateDelete = colorViewModel.deleteColor(color, newColor)
                            when(stateDelete){
                                is StateColor.Error -> {
                                    println(stateDelete.message)
                                    return NavigationIntent.BackHome
                                }
                                is StateColor.Success -> {
                                    println("✅Успешно")
                                    return NavigationIntent.BackHome
                                }
                            }
                        }
                        -1 -> return NavigationIntent.Back
                        -2 -> return NavigationIntent.BackHome
                        -3 -> return NavigationIntent.Exit
                        else -> {
                            println("❌ОШИБКА: Некорректный номер действия")
                        }
                    }
                }
            }
            else{
                val stateDelete = colorViewModel.deleteColor(color)
                when(stateDelete){
                    is StateColor.Error -> {
                        println(stateDelete.message)
                        return NavigationIntent.BackHome
                    }
                    is StateColor.Success -> {
                        println("✅Успешно")
                        return NavigationIntent.Back
                    }
                }
            }
        }
    }
    private fun startColorCreationMenu(): NavigationIntent {
        while (true){
            println("====================================")
            println("       Меню создания цвета")
            println("====================================")
            println("-1. Назад")
            println("-2. Вернуться на главную")
            println("-3. Выйти из меню цветов")
            println("====================================")
            println("           Новый цвет")
            print("Действие или Hex_cod: #")
            val inp = readln()
            if (inp.toIntOrNull()==null){  //Проверка если это не цифра значит хекс код

                val stateCreation = colorViewModel.createColor(hexCode = inp)
                when(stateCreation){
                    is StateColor.Error -> {
                        println(stateCreation.message)
                        return NavigationIntent.BackHome
                    }
                    is StateColor.Success -> {
                        println("✅Успешно")
                        println("Новый цвет: |${stateCreation.color.id}|${stateCreation.color.hexCode}|")
                        continue
                    }
                }
            }
            else{
                return when(inp.toInt()){
                    -1 -> NavigationIntent.Back
                    -2 -> NavigationIntent.BackHome
                    -3 -> NavigationIntent.Exit
                    else -> {
                        println("❌ОШИБКА: Некорректный номер действия")
                        continue
                    }
                }
            }
        }
    }
    fun startColorSelectionMenu(excludeColor: Color? =null): StateColor {
        while (true){
            println("====================================")
            println("       Меню выбора цвета")
            println("====================================")
            val stateListColor = colorViewModel.getColors()
            when(stateListColor){
                is StateListColor.Empty -> {
                    println("⚠️ПРЕДУПРЕЖДЕНИЕ: Нет ни одного цвета")
                }
                is StateListColor.Success -> {
                    val list = stateListColor.colors.toMutableList()
                    if (excludeColor!=null) {
                        list.remove(excludeColor)
                    }
                    displayColor(list)

                }
            }
            println("<номер цвета>. Выбрать")
            println("0. Создать новый")
            println("-1. Выйти")

            val inp = readln()
            if (inp.toIntOrNull()==null){
                println("❌ОШИБКА: Некорректный ввод")
                continue
            }else{
                when(val actionOrId = inp.toInt()){
                    0 -> {
                        startColorCreationMenu()
                        continue
                    }
                    -1 -> {
                        return StateColor.Error(message = "выход из меню")
                    }
                    else -> {
                        return colorViewModel.getColor(actionOrId)
                    }
                }
            }
        }
    }
    private fun displayColor(list: List<Color>){
        list.forEach { println("${it.id}|${if (it.isSystem)"🖥️" else "🙎‍♂️"} |${it.hexCode}") }
    }

}