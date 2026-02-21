package org.example.views.category

import org.example.InputState
import org.example.NavigationIntent
import org.example.NeedCategory
import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.viewmodels.CategoryListState
import org.example.viewmodels.CategoryState
import org.example.viewmodels.CategoryViewModel
import org.example.viewmodels.StateColor
import org.example.views.color.ColorView


// ----------------------------
// CategoryView
// ----------------------------
class CategoryView(private val categoryViewModel: CategoryViewModel, private val colorView: ColorView) {

    fun startMainMenu() {
        while (true) {
            val state = startCategoryMenu()
            when (state) {
                NavigationIntent.Back -> continue        // Просто перерисовать корень
                NavigationIntent.BackHome -> continue    // То же самое
                NavigationIntent.Exit -> return          // Выход в StorageView
            }
        }
    }
    private fun startCategoryMenu(): NavigationIntent {
        while (true) {
            val categoriesState = categoryViewModel.getBaseCategories()

            println("====================================")
            println("         Меню категорий")
            println("            Главная")
            println("====================================")
            displayCategory(categoriesState)
            println("0. Создать категорию")
            println("====================================")
            println("-1. Выйти")

            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            when(inp){
                0 ->{
                    val navIntent = startCategoryCreationMenu(null)
                    when(navIntent){
                        is NavigationIntent.Back -> continue
                        is NavigationIntent.BackHome -> continue
                        is NavigationIntent.Exit -> return NavigationIntent.Exit
                    }
                }
                -1 ->{
                    return NavigationIntent.Exit
                }
                else -> {
                    val navIntent = startCategoryMenu(inp)
                    when(navIntent){
                        is NavigationIntent.Back -> continue
                        is NavigationIntent.BackHome -> continue
                        is NavigationIntent.Exit -> return NavigationIntent.Exit
                    }
                }
            }
        }
    }
    private fun startCategoryMenu(currentCategoryId: Int): NavigationIntent {
        while (true) {
            val currentCategory: Category
            when(val stateCurrentCategory = categoryViewModel.getCategory(currentCategoryId)) {
                is CategoryState.Error -> {
                    println(stateCurrentCategory.message)
                    return NavigationIntent.Back
                }
                is CategoryState.Success -> {
                    currentCategory = stateCurrentCategory.category
                }
            }
            val categoriesState = categoryViewModel.getCategoriesByParent(currentCategoryId)
            println("====================================")
            println("         Меню категорий")
            println("            ${currentCategory.name}")
            println("====================================")
            displayCategory(categoriesState)
            println("0. Создать категорию")
            println("====================================")
            println("-1. Редактировать")
            println("-2. Удалить")
            println("-3. Назад")
            println("-4. Выйти")

            val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
            when(inp){
                0 ->{
                    val navIntent = startCategoryCreationMenu(currentCategoryId)
                    return when(navIntent){
                        is NavigationIntent.Back -> continue
                        is NavigationIntent.BackHome -> NavigationIntent.BackHome
                        is NavigationIntent.Exit -> NavigationIntent.Exit
                    }
                }
                -1 -> {
                    val navState = startCategoryEditingMenu(currentCategoryId)
                    return when(navState){
                        is NavigationIntent.Back -> continue
                        is NavigationIntent.BackHome -> NavigationIntent.BackHome
                        is NavigationIntent.Exit -> NavigationIntent.Exit
                    }
                }
                -2 -> {
                    val navState = startCategoryDeletingMenu(currentCategoryId)
                    return when(navState){
                        is NavigationIntent.Back -> continue
                        is NavigationIntent.BackHome -> NavigationIntent.BackHome
                        is NavigationIntent.Exit -> NavigationIntent.Exit
                    }
                }
                -3 ->{
                    return NavigationIntent.Back
                }
                -4 ->{
                    return NavigationIntent.Exit
                }
                else -> {
                    val navState = startCategoryMenu(inp.toInt())
                    return when(navState){
                        is NavigationIntent.Back -> continue
                        is NavigationIntent.BackHome -> NavigationIntent.BackHome
                        is NavigationIntent.Exit -> NavigationIntent.Exit
                    }
                }
            }
        }
    }
    private fun startCategoryCreationMenu(currentCategoryId: Int?): NavigationIntent {
        while (true) {
            println("====================================")
            println("      Меню создания категории")
            println("====================================")
            print("Название: ")
            val name = readln()
            print("Икнонка(Emoji):")
            val iconPath = readln()
            val color =
                when(val colorState =colorView.startColorSelectionMenu()){
                    is StateColor.Error ->{
                        println(colorState.message)
                        continue
                    }
                    is StateColor.Success -> {
                        colorState.color
                    }
                }
            val need: NeedCategory
            try {
                need = NeedCategory.valueOf(readln())
            } catch (e: IllegalArgumentException) {
                println("Неверное название необходимости")
                continue
            }
            val createState = categoryViewModel.createCategory(name= name, parentCategoryId= currentCategoryId,iconPath= iconPath, color= color)
            when(createState){
                is CategoryState.Error -> {
                    println(createState.message)
                    continue
                }
                is CategoryState.Success -> {
                    println("✅Успешно")
                    return NavigationIntent.Back
                }
            }
        }
    }
    private fun startCategoryEditingMenu(currentCategoryId: Int): NavigationIntent{
        while (true){
            val currentCategory: Category
            when(val stateCurrentCategory = categoryViewModel.getCategory(currentCategoryId)) {
                is CategoryState.Error -> {
                    println(stateCurrentCategory.message)
                    return NavigationIntent.Back
                }
                is CategoryState.Success -> {
                    currentCategory = stateCurrentCategory.category
                }
            }
            println("====================================")
            println("   Меню редактирвоания категории")
            println("     ${currentCategory.name}")
            println("====================================")
            val inp = readln()
            if (currentCategory.isSystem){
                println("1. Скрыть")
                println("====================================")
                println("-1. Назад")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    1 ->{
                        TODO("Сделать скрытие категории")
                    }
                    -1 -> {
                       return NavigationIntent.Back
                    }
                    else -> {
                        println("❌ОШИБКА: нет такого действия")
                        continue
                    }
                }
            }
            else{
                println("1. Изменить название")
                println("2. Изменить иконку")
                println("3. Изменить цвет")
                println("4. Изменить необходимость")
                println("5. Изменить родительскую категорию")
                println("6. Скрыть|Показать")
                println("====================================")
                println("-1. Сохранить")
                println("-2. Назад")
                var newName: String? = null
                var newIcon: String? = null
                var newColor: Color? = null
                var newNeed: NeedCategory? = null
                var newParent: Category? = null
                var isHide: Boolean = currentCategory.isHide

                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    1 ->{
                        print("Новое название:")
                        newName = readln()
                        continue
                    }
                    2 ->{
                        print("Новая иконка:")
                        newIcon = readln()
                        continue
                    }
                    3 ->{
                        print("Новый цвет:")
                        val state =colorView.startColorSelectionMenu(currentCategory.color)
                        when(state){
                            is StateColor.Error -> {println(state.message); continue}
                            is StateColor.Success -> {
                                newColor = state.color
                            }
                        }
                    }
                    4 ->{
                        print("Новая необходимость:")
                        val inpState = NeedCategory.getObj(readln())
                        when(inpState){
                            is InputState.Error -> {println(inpState.message); continue}
                            is InputState.Success<NeedCategory> -> {
                                newNeed = inpState.obj
                            }
                        }

                    }
                    5 ->{
                        print("Новая родительская категория")
                        TODO("Продумать как не дать изменить родительскую категорию на свою же дочернюю")
                    }
                    6 ->{
                        if (isHide){
                            print("Показать?")
                        }
                        else{
                            print("Скрыть?")
                        }
                        isHide = readln().toBoolean()
                    }
                    -1 -> {
                        if (newName==null || newIcon ==null || newColor ==null || newNeed == null || newParent ==null){
                            return NavigationIntent.Back
                        }
                        else(

                                TODO("тупо передавать newName newIcon и тд дальше в viewModel она в service и там уже разибратся что обновлять. " +
                                        "И возврашть состояние обновления." +
                                        "Так же потом тут сделать меню сохранения <Были изменены .. перечисление...сохранить? > ")

                                )
                    }
                    -2->{
                        TODO("тупо передавать newName newIcon и тд дальше в viewModel она в service и там уже разибратся что обновлять. " +
                                "И возврашть состояние обновления.")
                    }

                    else -> {
                        println("❌ОШИБКА: нет такого действия")
                        continue
                    }
                }

            }
        }
    }
    private fun startCategoryDeletingMenu(currentCategoryId: Int): NavigationIntent{
        while (true){
            val hasRelatedItems: Boolean = false//TODO(сделать получение из сервиса)
            val hasChildren: Boolean = false //TODO(сдеаоть получение из сервиса)
            if (!hasRelatedItems || !hasChildren){
                TODO("Простое удаление")
            }
            else{
                println("1. Полное удалени.(Удаялтся все записи и дочерние категори)")
                println("2. Удалить с сохранением данных")
                println("3. Назад")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    1 ->{
                        TODO("Полное удаоение. Думю это уже дело на стороне сервиса. Так что нужно вызывать метод удаления с параметрами либло отдельныый метод полного удаления")
                        return NavigationIntent.Back
                    }
                    2 ->{
                        TODO("Тут интререснее. Надо узнать есть ли записи в данной категори и куда их прееместить. И что делать с дочернимим категориями. Точнее как их прекрипить к другому родителю")
                        return NavigationIntent.Back
                    }
                    3 ->{
                        return NavigationIntent.Back
                    }
                    else -> {
                        println("Нет такого действия")
                        continue
                    }
                }
            }
        }
    }

    fun startCategorySelectionMenu(excludeCategory: Category?=null): Category{
        while(true){
            while (true) {
                val categoriesState = categoryViewModel.getBaseCategories()

                println("====================================")
                println("         Меню категорий")
                println("            Главная")
                println("====================================")
                displayCategory(categoriesState,excludeCategory)
                println("0. Создать категорию")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    0 ->{
                        val navIntent = startCategoryCreationMenu(null)
                        when(navIntent){
                            is NavigationIntent.Back -> continue
                            is NavigationIntent.BackHome -> continue
                            is NavigationIntent.Exit -> continue
                        }
                    }
                    else -> {
                        val category = startCategorySelectionMenu(inp.toInt())
                        if (  category==null){
                            continue
                        }
                        else{
                            return category
                        }
                    }
                }
            }


        }
    }
    private fun startCategorySelectionMenu(parentCategory: Int,excludeCategory: Category?=null): Category?{
        while(true){
            while (true) {
                val currentCategory: Category
                when(val stateCurrentCategory = categoryViewModel.getCategory(parentCategory)) {
                    is CategoryState.Error -> {
                        println(stateCurrentCategory.message)
                        return null
                    }
                    is CategoryState.Success -> {
                        currentCategory = stateCurrentCategory.category
                    }
                }
                val categoriesState = categoryViewModel.getCategoriesByParent(parentCategory)

                println("====================================")
                println("  Меню выбора дочерних категорий")
                println("     ${currentCategory.name}")
                println("====================================")

                displayCategory(categoriesState,excludeCategory)

                println("0. Создать категорию")
                println("====================================")
                println("-1. Выбрать")
                println("-2. Редактировать")
                println("-3. Удалить")
                println("-4. Выйти")

                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    0 ->{
                        val navIntent = startCategoryCreationMenu(parentCategory)
                        return when(navIntent){
                            is NavigationIntent.Back -> continue
                            is NavigationIntent.BackHome -> null
                            is NavigationIntent.Exit -> null
                        }
                    }
                    -1 -> {
                        val catState = categoryViewModel.getCategory(parentCategory)
                        when(catState){
                            is CategoryState.Error -> {
                                println(catState.message)
                                return null
                            }
                            is CategoryState.Success -> {return catState.category}
                        }

                    }
                    -2 -> {
                        val navState = startCategoryEditingMenu(parentCategory)
                        return when(navState){
                            is NavigationIntent.Back -> continue
                            is NavigationIntent.BackHome -> null
                            is NavigationIntent.Exit -> null
                        }
                    }
                    -3 ->{
                        val navState = startCategoryDeletingMenu(parentCategory)
                        return when(navState){
                            is NavigationIntent.Back -> continue
                            is NavigationIntent.BackHome -> null
                            is NavigationIntent.Exit -> null
                        }
                    }
                    -4 -> {
                        return null
                    }
                    else -> {
                        return startCategorySelectionMenu(inp.toInt())
                    }
                }
            }
        }
    }

    private fun displayCategory(state: CategoryListState, excludeCategory: Category?=null) {
        when(state){
            is CategoryListState.Empty -> {
                println("⚠️ПРЕДУПРЕЖДЕНИЕ: Нет ни одной категории")
            }
            is CategoryListState.Success -> {
                val list = state.categories.toMutableList()
                if (excludeCategory!=null){
                    list.remove(excludeCategory)
                }
               list.forEach { println("|id - ${it.id}| name - ${it.name}| ${it.color.hexCode} ${if(it.isSystem)"🖥️" else "🙎‍♂️"}") }
                println("<номер категории>. Выбор категории")
            }
        }

    }
    private fun displayCategory(list: List<Category>) {
        list.forEach { println("|id - ${it.id}| name - ${it.name}| ${it.color.hexCode} ${if(it.isSystem)"🖥️" else "🙎‍♂️"}") }
    }


}
