package org.example.views

import org.example.NavigationIntent
import org.example.model.domain.ExistColor
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.SystemColor
import org.example.model.domain.UserColor
import org.example.viewmodels.ColorViewModel

class ColorView(private val colorViewModel: ColorViewModel) {
    fun start(){
        while (true){
            when(startMainMenu()){
                NavigationIntent.Back -> continue
                NavigationIntent.BackHome -> continue
                NavigationIntent.Exit -> return
            }
        }
    }

    private fun displayColorWithWarring(stateListColor: StateDomainList<ExistColor>){
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
    }

    private fun startMainMenu(): NavigationIntent {
        while (true){
            println("====================================")
            println("            Color menu")
            println("------------------------------------")
            displayColorWithWarring(colorViewModel.getColors())
            println("0. Create color")
            println("------------------------------------")
            println("-1. Exit")
            println("====================================")
            val inp = readln().toIntOrNull() ?: run { println("❌Error: need number"); continue }
            when(inp){
                0 -> {
                    when(startColorCreationMenu()){
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
                        when(startColorMenu(inp)){
                            NavigationIntent.Back -> continue
                            NavigationIntent.Exit -> return NavigationIntent.Exit
                            NavigationIntent.BackHome -> continue
                        }
                    }
                    catch (e: IllegalArgumentException){
                        println("❌Error: Некорректный номер действия")
                        continue
                    }
                }
            }
        }
    }
    private fun startColorMenu(colorId: Int): NavigationIntent {
        while (true){
            println("====================================")
            println("       Color menu")
            println("------------------------------------")
            when(val stateCurrentColor = colorViewModel.getColor(colorId)){
                is StateDomain.Error -> {
                    println(stateCurrentColor.message)
                    return NavigationIntent.BackHome
                }
                is StateDomain.Success -> {
                    val currentColor = when(stateCurrentColor.domain){
                        is UserColor -> {stateCurrentColor.domain}
                        is SystemColor -> {
                            println("⚠️Warring: Системные цвета нельзя редактировать")
                            return NavigationIntent.BackHome
                        }
                        else -> throw IllegalArgumentException()
                    }

                    println("Color: ${currentColor.hexCode}")
                    println("1. Edit")
                    println("2. Delete")
                    println("------------------------------------")
                    println("-1. Back")
                    println("-2. Get out ")
                    println("====================================")

                    val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                    when (inp) {
                        1 -> {
                            return when(startColorEditingMenu(currentColor)){
                                NavigationIntent.Back -> continue
                                NavigationIntent.Exit -> NavigationIntent.Exit
                                NavigationIntent.BackHome -> NavigationIntent.BackHome
                            }
                        }
                        2 -> {
                            return when(startDeleteMenu(currentColor)){
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
                                startColorMenu(inp)
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
    private fun startColorEditingMenu(currentColor: UserColor): NavigationIntent {
        while (true){
            println("====================================")
            println("       Меню редактирования цвета")
            println("------------------------------------")
            println("-1. Назад")
            println("-2. Вернуться на главную")
            println("-3. Выйти из меню цветов")
            println("------------------------------------")
            println("           Новый Hex_code")
            print("Hex_cod: #")
            println("====================================")

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
    private fun startDeleteMenu(color: UserColor): NavigationIntent {
        while(true){
            val hasRelatedItems: Boolean = colorViewModel.hasRelations(color)
            if (hasRelatedItems){
                println("====================================")
                println("        Delete color menu")
                println("  This color is used in entity!!!!")
                println("------------------------------------")
                println("1. Replace with another")
                println("------------------------------------")
                println("-1. Back")
                println("-2. Back to home")
                println("-3. Exit")
                println("------------------------------------")
                print("choose: ")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                println("====================================")
                when(inp){
                    1 -> {
                        val newColor: ExistColor
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
            else{
                when( val stateDelete = colorViewModel.deleteColor(color)){
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
            println("         Create color menu")
            println("------------------------------------")
            println("-1. Back")
            println("-2. Back to home")
            println("-3. Exit")
            println("------------------------------------")
            println("           New color")
            print("Action or Hex_cod: #")
            val inp = readln()
            println("====================================")

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
    fun startColorSelectionMenu(excludeColor: ExistColor? =null): StateDomain<ExistColor> {
        while (true){
            println("====================================")
            println("       Меню выбора цвета")
            println("------------------------------------")
            displayColorWithWarring(colorViewModel.getColors())
            println("0. Создать новый")
            println("-1. Выйти")
            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            println("====================================")
            return when(inp){
                0 -> {
                    startColorCreationMenu()
                    continue
                }

                -1 -> {
                    StateDomain.Error(message = "выход из меню")
                }

                else -> {
                    colorViewModel.getColor(inp)
                }
            }
        }
    }
    private fun displayColor(list: List<ExistColor>){
        list.forEach {
            when(it){
                is UserColor -> {
                    println("${it.id}|${"🙎‍♂️"} |${it.hexCode}")
                }
                is SystemColor -> {
                    println("${it.id}|${"🖥️"} |${it.hexCode}")
                }
            }
        }
    }
}