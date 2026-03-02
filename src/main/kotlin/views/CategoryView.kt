package org.example.views

import org.example.InputState
import org.example.NavigationIntent
import org.example.model.domain.Category
import org.example.model.domain.CategoryHierarchy
import org.example.model.domain.CategoryOwner
import org.example.model.domain.Color
import org.example.model.domain.NeedCategory
import org.example.model.domain.NewCategory
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.viewmodels.CategoryViewModel
import org.example.views.ColorView

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

            println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            println("         Меню категорий")
            println("            Главная")
            println("====================================")
            displayCategory(categoriesState)
            println("0. Создать категорию")
            println("====================================")
            println("-1. Выйти")
            println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")

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
                is StateDomain.Error -> {
                    println(stateCurrentCategory.message)
                    return NavigationIntent.Back
                }
                is StateDomain.Success -> {
                    currentCategory = stateCurrentCategory.domain
                }
            }
            val categoriesState = categoryViewModel.getCategoriesByParent(currentCategoryId)
            println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
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
            println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")

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
            println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            println("      Меню создания категории")
            println("====================================")
            print("Название: ")
            val name = readln()
            print("Икнонка(Emoji):")
            val iconPath = readln()
            val color =
                when(val colorState =colorView.startColorSelectionMenu()){
                    is StateDomain.Error ->{
                        println(colorState.message)
                        continue
                    }
                    is StateDomain.Success -> {
                        colorState.domain
                    }
                }
            println("Необходимость")
            val need = NeedCategory.Companion.selectNeed() ?: continue //Если вернулся null это зачит что в меню выбора необходимости было выбрано действие "назад"
            when(val createState = categoryViewModel.createCategory(name= name, parentCategoryId= currentCategoryId,iconPath= iconPath, color= color, need= need)){
                is StateDomain.Error -> {
                    println(createState.message)
                    continue
                }
                is StateDomain.Success -> {
                    println("✅Успешно")
                    return NavigationIntent.Back
                }
            }
        }
    }
    private fun startCategoryEditingMenu(currentCategoryId: Int): NavigationIntent {
        var newName: String? = null
        var newIcon: String? = null
        var newColor: Color.ExistingColor? = null
        var newNeed: NeedCategory? = null
        var newIsHide: Boolean? = null
        var newParent: CategoryHierarchy? = null

        while (true){
            /** Получение текущей категории из бд + обработчик ошибок */
            val currentCategory: Category = when(val stateCurrentCategory = categoryViewModel.getCategory(currentCategoryId)) {
                is StateDomain.Error -> {
                    println(stateCurrentCategory.message)
                    return NavigationIntent.Back
                }
                is StateDomain.Success -> {
                     stateCurrentCategory.domain
                }
            }
            /**  Показ шапки меню */
            println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            println("   Меню редактирования категории")
            println("     |${currentCategory.name}|${currentCategory.icon}|${currentCategory.color}|${currentCategory.need}|${if(currentCategory.structure is CategoryHierarchy.Child) categoryViewModel.getCategory(currentCategory.structure.parentId.toInt()) else null}|${currentCategory.isHidden}")
            println("====================================")
            /** Развитие событий в зависимости от принадлежности категории(системная или пользовательская) */
            when(currentCategory.owner){
                is CategoryOwner.System -> {
                    if (currentCategory.isHidden){
                        print("Показать?")
                        newIsHide = !readln().toBoolean()
                    }
                    else{
                        print("Скрыть?")
                        newIsHide = readln().toBoolean()
                    }
                    when(val state = categoryViewModel.changeCategorySystem(currentCategory, newIsHide)){
                        is StateDomain.Error -> {
                            println(state.message)
                            return NavigationIntent.Back
                        }
                        is StateDomain.Success -> {
                            println("✅Успешно")
                            return NavigationIntent.Back
                        }
                    }
                }
                is CategoryOwner.User -> {
                    println("1. Изменить название")
                    println("2. Изменить иконку")
                    println("3. Изменить цвет")
                    println("4. Изменить необходимость")
                    println("5. Изменить родительскую категорию")
                    println("6. Скрыть|Показать")
                    println("====================================")
                    println("-1. Сохранить")
                    println("-2. Назад")
                    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")

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
                            when(val state =colorView.startColorSelectionMenu(currentCategory.color)){
                                is StateDomain.Error -> println(state.message)
                                is StateDomain.Success -> newColor = state.domain
                            }
                            continue
                        }
                        4 ->{
                            print("Новая необходимость:")
                            val inpState = NeedCategory.getObj(readln())
                            when(inpState){
                                is InputState.Error -> println(inpState.message)
                                is InputState.Success<NeedCategory> -> newNeed = inpState.obj
                            }
                            continue
                        }
                        5 ->{
                            print("Новая родительская категория")
                            val newParentCategory = startParentCategorySelectionMenu(excludeCategoryId = if(currentCategory.structure is CategoryHierarchy.Child) currentCategory.structure.parentId.toInt() else null)
                            newParent = when(newParentCategory){
                                null -> {
                                    CategoryHierarchy.Root
                                }

                                is Category ->{
                                    CategoryHierarchy.Child(newParentCategory.id)
                                }
                            }
                        }
                        6 ->{
                            // is hide = true - значит скрыт
                            if (currentCategory.isHidden){
                                print("Показать?")
                                newIsHide = !readln().toBoolean()
                            }
                            else{
                                print("Скрыть?")
                                newIsHide = readln().toBoolean()
                            }
                            continue
                        }

                        -1 ->{
                            when(val state = categoryViewModel.changeCategory(
                                newName = newName,
                                newIcon = newIcon,
                                newColor = newColor,
                                newNeed = newNeed,
                                newIsHide = newIsHide,
                                newParent = newParent,
                                currentCategory = currentCategory
                            )){
                                is StateDomain.Error -> {
                                    println(state.message)
                                    continue
                                }
                                is StateDomain.Success -> {
                                    println("✅Успешно")
                                    return NavigationIntent.Back
                                }
                            }
                        }
                        -2->{
                            return NavigationIntent.Back
                        }
                        else -> {
                            println("❌ОШИБКА: нет такого действия")
                            continue
                        }
                    }
                }
            }
        }
    }
    private fun startCategoryDeletingMenu(currentCategoryId: Int): NavigationIntent {
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
    fun startCategorySelectionMenu(excludeCategory: Category?=null): Category {
        while(true){
            while (true) {
                val categoriesState = categoryViewModel.getBaseCategories()

                println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                println("         Меню категорий")
                println("            Главная")
                println("====================================")
                displayCategory(categoriesState,excludeCategory)
                println("0. Создать категорию")
                println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
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
    fun startParentCategorySelectionMenu(excludeCategoryId: Int?=null): Category? {
        while(true){
            val stateExcludeCategory = if (excludeCategoryId!=null) categoryViewModel.getCategory(excludeCategoryId) else null
            val excludeCategory = if(stateExcludeCategory==null) null else {
                when(stateExcludeCategory){
                    is StateDomain.Error ->{
                        println(stateExcludeCategory.message)
                        throw Exception()
                    }
                    is StateDomain.Success -> stateExcludeCategory.domain
                }
            }
            while (true) {
                val categoriesState = categoryViewModel.getBaseCategories()

                println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                println("         Меню категорий")
                println("            Главная")
                println("====================================")
                displayCategory(categoriesState,excludeCategory)
                println("-1. Сделать категорию базовой")
                println("0. Создать категорию")
                println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
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
                    -1 ->{
                        return null
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
                    is StateDomain.Error -> {
                        println(stateCurrentCategory.message)
                        return null
                    }
                    is StateDomain.Success -> {
                        currentCategory = stateCurrentCategory.domain
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
                            is StateDomain.Error -> {
                                println(catState.message)
                                return null
                            }
                            is StateDomain.Success -> {return catState.domain}
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

    private fun displayCategory(state: StateDomainList<Category>, excludeCategory: Category?=null) {
        when(state){
            is StateDomainList.Empty -> {
                println("⚠️ПРЕДУПРЕЖДЕНИЕ: Нет ни одной категории")
            }
            is StateDomainList.Success -> {
                val list = state.domainList.toMutableList()
                if (excludeCategory!=null){
                    list.remove(excludeCategory)
                }
               list.forEach { println("|id - ${it.id}| name - ${it.name}| ${it.color.hexCode} ${if(it.owner is CategoryOwner.System)"🖥️" else "🙎‍♂️"}") }
                println("<номер категории>. Выбор категории")
            }
        }
    }
    private fun displayCategory(list: List<Category>) {
        list.forEach { println("|id - ${it.id}| name - ${it.name}| ${it.color.hexCode} ${if(it.owner is CategoryOwner.System)"🖥️" else "🙎‍♂️"}") }
    }


}