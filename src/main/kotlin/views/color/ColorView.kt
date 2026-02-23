package org.example.views.color

import org.example.model.domain.Color
import org.example.viewmodels.ColorViewModel
import org.example.NavigationIntent
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList

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
                is StateDomainList.Empty -> {
                    println("⚠️ПРЕДУПРЕЖДЕНИЕ: Нет ни одного цвета")
                }
                is StateDomainList.Success ->{
                    val colorList = stateListColor.domainList
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
            when(val stateCurrentColor = colorViewModel.getColor(colorId)){
                is StateDomain.Error -> {
                    println(stateCurrentColor.message)
                    return NavigationIntent.BackHome
                }
                is StateDomain.Success -> {
                    val currentColor = when(stateCurrentColor.domain){
                        is Color.UserColor -> {stateCurrentColor.domain}
                        is Color.SystemColor -> {
                            println("⚠️ПРЕДУПРЕЖДЕНИЕ: Системные цвета нельзя редактировать")
                            return NavigationIntent.BackHome
                        }
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
                        when (val numberActionOrId = inp.toInt()) {
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
    private fun startColorEditingMenu(currentColor: Color.UserColor): NavigationIntent {
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
                    is StateDomain.Error -> {
                        println(stateEditing.message)
                        return NavigationIntent.BackHome
                    }
                    is StateDomain.Success -> {
                        println("✅Успешно")
                        println("Измененный цвет |${stateEditing.domain.id}| ${stateEditing.domain.hexCode}")
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
    private fun startDeleteMenu(color: Color.UserColor): NavigationIntent {
        while(true){
            val hasRelatedItems: Boolean = colorViewModel.hasRelations(color)
            if (hasRelatedItems){
                println("Этот цвет используется в записях!!!")
                println("====================================")
                println("1. Заменить на другой")
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
                            val newColor: Color.ExistingColor
                            val stateNewColor = startColorSelectionMenu(color)
                            when(stateNewColor){
                                is StateDomain.Error -> {
                                    println(stateNewColor.message)
                                    continue
                                }
                                is StateDomain.Success -> {
                                    newColor = stateNewColor.domain
                                }
                            }

                            val stateDelete = colorViewModel.deleteColor(color, newColor)
                            when(stateDelete){
                                is StateDomain.Error -> {
                                    println(stateDelete.message)
                                    return NavigationIntent.BackHome
                                }
                                is StateDomain.Success -> {
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
                    is StateDomain.Error -> {
                        println(stateDelete.message)
                        return NavigationIntent.BackHome
                    }
                    is StateDomain.Success -> {
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
                    is StateDomain.Error -> {
                        println(stateCreation.message)
                        return NavigationIntent.BackHome
                    }
                    is StateDomain.Success -> {
                        println("✅Успешно")
                        println("Новый цвет: |${stateCreation.domain.id}|${stateCreation.domain.hexCode}|")
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
    fun startColorSelectionMenu(excludeColor: Color.ExistingColor? =null): StateDomain<Color.ExistingColor> {
        while (true){
            println("====================================")
            println("       Меню выбора цвета")
            println("====================================")
            val stateListColor = colorViewModel.getColors()
            when(stateListColor){
                is StateDomainList.Empty -> {
                    println("⚠️ПРЕДУПРЕЖДЕНИЕ: Нет ни одного цвета")
                }
                is StateDomainList.Success -> {
                    val list = stateListColor.domainList.toMutableList()
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
                        return StateDomain.Error(message = "выход из меню")
                    }
                    else -> {
                        return colorViewModel.getColor(actionOrId)
                    }
                }
            }
        }
    }
    private fun displayColor(list: List<Color.ExistingColor>){
        list.forEach {
            when(it){
                is Color.UserColor -> {
                    println("${it.id}|${"🙎‍♂️"} |${it.hexCode}")
                }
                is Color.SystemColor -> {
                    println("${it.id}|${"🖥️"} |${it.hexCode}")
                }
            }
        }
    }
}