package org.example.views

import org.example.InputState
import org.example.NavigationIntent
import org.example.ViewService
import org.example.model.domain.ExistColor
import org.example.model.domain.Category
import org.example.model.domain.CategoryStructure
import org.example.model.domain.CategoryOwner
import org.example.model.domain.ResultMenu
import org.example.model.domain.NeedCategory
import org.example.model.domain.Operation
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.viewmodels.CategoryViewModel

class CategoryView(private val categoryViewModel: CategoryViewModel, private val colorView: ColorView) {

    fun startMainMenu() {
        while (true) {
            val state = startCategoryMenu()
            when (state) {
                NavigationIntent.Exit -> return
                else -> continue
            }
        }
    }
    private fun startCategoryMenu(): NavigationIntent {
        while (true) {
            val categoriesState = categoryViewModel.getBaseCategories()
            ViewService.printHeadersForMenu("Меню категорий", "Главная")
            displayCategory(categoriesState)
            ViewService.printActionsForMenu("0. Создать категорию","-1. Выйти" )
            ViewService.printBottom()
            ViewService.printHeaderChoose()
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
            ViewService.printHeadersForMenu("Меню категорий",currentCategory.name)
            displayCategory(categoriesState)
            ViewService.printActionsForMenu("0. Создать категорию","-1. Редактировать","-2. Удалить","-3. Назад","-4. Выйти")
            ViewService.printBottom()
            ViewService.printHeaderChoose()
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
            ViewService.printHeadersForMenu("Create category menu")
            ViewService.printActionsForMenu("Name")
            val name = readln()
            ViewService.printActionsForMenu("Icon(Emoji):")
            val iconPath = readln()
            ViewService.printActionsForMenu("Color:")
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
            ViewService.printActionsForMenu("Need")
            val need = NeedCategory.selectNeed() ?: continue //Если вернулся null это зачит что в меню выбора необходимости было выбрано действие "назад"

            when(val createState = categoryViewModel.createCategory(name= name, parentCategoryId= currentCategoryId,iconPath= iconPath, color= color, need= need)){
                is StateDomain.Error -> {
                    println(createState.message)
                    ViewService.printBottom()
                    continue
                }
                is StateDomain.Success -> {
                    println("✅Успешно")
                    ViewService.printBottom()
                    return NavigationIntent.Back
                }
            }
        }
    }
    private fun startCategoryEditingMenu(currentCategoryId: Int): NavigationIntent {
        var newName: String? = null
        var newIcon: String? = null
        var newColor: ExistColor? = null
        var newNeed: NeedCategory? = null
        var newIsHide: Boolean? = null
        var newParent: CategoryStructure? = null
        while (true){
            val currentCategory: Category = when(val stateCurrentCategory = categoryViewModel.getCategory(currentCategoryId)) {
                is StateDomain.Error -> {
                    println(stateCurrentCategory.message)
                    return NavigationIntent.Back
                }
                is StateDomain.Success -> {
                     stateCurrentCategory.domain
                }
            }
            ViewService.printHeadersForMenu("Editing category menu", "     |${currentCategory.name}|${currentCategory.icon}|${currentCategory.color}|${currentCategory.need}|${if(currentCategory.structure is CategoryStructure.Child) categoryViewModel.getCategory(currentCategory.structure.parentId.toInt()) else null}|${currentCategory.isHidden}")
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
                    ViewService.printActionsForMenu("1. Изменить название","2. Изменить иконку","3. Изменить цвет","4. Изменить необходимость","5. Изменить родительскую категорию","6. Скрыть|Показать","-1. Сохранить","-2. Назад")
                    ViewService.printBottom()
                    ViewService.printHeaderChoose()
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
                            val resMenuSelection = startParentCategorySelectionMenu()
                            newParent = when(resMenuSelection){
                                is ResultMenu.Complete -> {
                                    if (resMenuSelection.item==null) CategoryStructure.Root else CategoryStructure.Child(resMenuSelection.item.id)
                                }
                                is ResultMenu.Exception -> {
                                    println(resMenuSelection.message)
                                    continue
                                }
                                is ResultMenu.NavigationOnly -> {
                                    continue
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
        TODO("Не реализованно. И не будет пока не будут сделаны Operation")
        fun startActionsMenuOnRecords(): NavigationIntent{
            while (true){
                lateinit var recordList: StateDomainList<Operation> //TODO сделать получение операций с текущей категорией
                ViewService.printHeadersForMenu("Действия над записиями с данной категорией")
                ViewService.printListDomain(recordList){
                    TODO("Реализовать после реализации операций")
                }
                ViewService.printActionsForMenu("-1. Выйти", "-2. Заменить у всех записей категорию", "-3 Удалить все записи")
            }
        }
        fun startActionMenuOverChildrenCategories(): NavigationIntent{
            while (true){
                val childrenList = categoryViewModel.getCategoriesByParent(currentCategoryId)
                ViewService.printHeadersForMenu("Действия над дочерними категориями")
                ViewService.printListDomain(childrenList){
                    println("|${it.id}|${it.name}")
                }
                ViewService.printActionsForMenu("-1. Выйти", "-2. Заменить родителя у всех дочерних категорий", "-3. Удалить все дочерние категории с со всеми данными")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    -1 -> {
                        return NavigationIntent.Exit
                    }
                    -2 -> {
                        TODO()
                    }
                    -3 -> {
                        TODO()
                    }
                    else -> {
                        TODO()
                    }

                }
            }
        }

        val hasRelatedItems: Boolean = false//TODO(сделать получение из сервиса)
        val hasChildren: Boolean = false //TODO(сделать получение из сервиса)

        if (hasRelatedItems){
            when(startActionsMenuOnRecords()){
                is NavigationIntent.Back -> {}
                is NavigationIntent.BackHome -> {throw IllegalArgumentException("BackHome не должен возвращаться")}
                is NavigationIntent.Exit -> {return NavigationIntent.Back}
            }
        }
        if (hasChildren){
            when(startActionMenuOverChildrenCategories()){
                is NavigationIntent.Back -> {}
                is NavigationIntent.BackHome -> {throw IllegalArgumentException("BackHome не должен возвращаться")}
                is NavigationIntent.Exit -> {return NavigationIntent.Back}
            }
        }
        when(val deleteState = categoryViewModel.deleteCategory(currentCategoryId)){
            is StateDomain.Error ->{
                println(deleteState.message)
                return NavigationIntent.Back
            }
            is StateDomain.Success -> {
                return NavigationIntent.Back
            }
        }
    }

    private fun startCategorySelectionMenu(): ResultMenu<Category> {
        while(true){
            while (true) {
                val categoriesState = categoryViewModel.getBaseCategories()
                ViewService.printHeadersForMenu("Меню категорий", "Выбор")
                displayCategory(categoriesState)
                ViewService.printActionsForMenu("0. Создать категорию", "-1. Назад")
                ViewService.printBottom()
                ViewService.printHeaderChoose()
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    0 ->{
                        startCategoryCreationMenu(null)
                        continue
                    }
                    -1 -> {
                        return ResultMenu.NavigationOnly(NavigationIntent.Exit)
                    }
                    else -> {
                        when(val menuIntent = startCategorySelectionMenu(inp)){
                            is ResultMenu.Exception -> {
                                return ResultMenu.Exception(menuIntent.message)
                            }
                            is ResultMenu.NavigationOnly -> {
                                continue
                            }
                            is ResultMenu.Complete -> {
                                return menuIntent
                            }
                        }
                    }
                }
            }
        }
    }
    private fun startCategorySelectionMenu(parentCategory: Int): ResultMenu<Category>{
        while(true){
            while (true) {
                val currentCategory: Category
                when(val stateCurrentCategory = categoryViewModel.getCategory(parentCategory)) {
                    is StateDomain.Error -> {
                        return ResultMenu.Exception(stateCurrentCategory.message)
                    }
                    is StateDomain.Success -> {
                        currentCategory = stateCurrentCategory.domain
                    }
                }
                ViewService.printHeadersForMenu("Меню выбора дочерних категорий", currentCategory.name)
                displayCategory(categoryViewModel.getCategoriesByParent(parentCategory))
                ViewService.printActionsForMenu("0. Создать категорию","-1. Выбрать","-2. Редактировать","-3. Удалить","-4. Назад")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    0 ->{
                        startCategoryCreationMenu(parentCategory)
                        continue
                    }
                    -1 -> {
                        return when(val catState = categoryViewModel.getCategory(parentCategory)){
                            is StateDomain.Error -> {
                                ResultMenu.Exception(catState.message)
                            }
                            is StateDomain.Success -> {
                                ResultMenu.Complete(catState.domain, NavigationIntent.Exit)
                            }
                        }
                    }
                    -2 -> {
                        startCategoryEditingMenu(parentCategory)
                        continue
                    }
                    -3 ->{
                        startCategoryDeletingMenu(parentCategory)
                        continue
                    }
                    -4 -> {
                        return ResultMenu.NavigationOnly(NavigationIntent.Back)
                    }
                    else -> {
                        return when(val menuIntent = startCategorySelectionMenu(inp)) {
                            is ResultMenu.Exception -> {
                                ResultMenu.Exception(menuIntent.message)
                            }

                            is ResultMenu.NavigationOnly -> {
                                continue
                            }

                            is ResultMenu.Complete -> {
                                menuIntent
                            }
                        }
                    }
                }
            }
        }
    }
    private fun startParentCategorySelectionMenu(): ResultMenu<Category?>{
        while(true){
            while (true) {
                val categoriesState = categoryViewModel.getBaseCategories()
                ViewService.printHeadersForMenu("Меню категорий", "Выбор")
                displayCategory(categoriesState)
                ViewService.printActionsForMenu("0. Создать категорию", "-1. Выбрать", "-2. Выйти")
                ViewService.printBottom()
                ViewService.printHeaderChoose()
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    0 ->{
                        startCategoryCreationMenu(null)
                        continue
                    }
                    -1 -> {
                        return ResultMenu.Complete(null, NavigationIntent.Exit)
                    }
                    -2 -> {
                        return ResultMenu.NavigationOnly(NavigationIntent.Exit)
                    }
                    else -> {
                        when(val menuIntent = startCategorySelectionMenu(inp)){
                            is ResultMenu.Exception -> {
                                return ResultMenu.Exception(menuIntent.message)
                            }
                            is ResultMenu.NavigationOnly -> {
                                continue
                            }
                            is ResultMenu.Complete -> {
                                return menuIntent
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * Additional methods:
     */
    private fun displayCategory(state: StateDomainList<Category>) {
        ViewService.printListDomain(state){
            println("|${it.id}|${it.name}|${if (it.owner is CategoryOwner.System) "🖥️" else "🙎‍♂️"}")
        }
    }
}