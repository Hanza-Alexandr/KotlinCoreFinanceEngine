package org.example.views

import org.example.InputState
import org.example.NavigationIntent
import org.example.ViewService
import org.example.model.domain.Category
import org.example.model.domain.CategoryHierarchy
import org.example.model.domain.CategoryOwner
import org.example.model.domain.Color
import org.example.model.domain.GeneralTransaction
import org.example.model.domain.NeedCategory
import org.example.model.domain.NewCategory
import org.example.model.domain.Operation
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.viewmodels.CategoryViewModel
import org.example.views.ColorView
import java.text.Bidi
import javax.swing.text.View
import javax.swing.text.html.BlockView

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
        var newColor: Color.ExistingColor? = null
        var newNeed: NeedCategory? = null
        var newIsHide: Boolean? = null
        var newParent: CategoryHierarchy? = null
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
            ViewService.printHeadersForMenu("Editing category menu", "     |${currentCategory.name}|${currentCategory.icon}|${currentCategory.color}|${currentCategory.need}|${if(currentCategory.structure is CategoryHierarchy.Child) categoryViewModel.getCategory(currentCategory.structure.parentId.toInt()) else null}|${currentCategory.isHidden}")
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
                            val newParentCategory = startCategorySelectionMenu()
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
    private fun startCategorySelectionMenu(): Category {
        while(true){
            while (true) {
                val categoriesState = categoryViewModel.getBaseCategories()
                ViewService.printHeadersForMenu("Меню категорий", "Выбор")
                displayCategory(categoriesState)
                ViewService.printActionsForMenu("0. Создать категорию")
                ViewService.printBottom()
                ViewService.printHeaderChoose()
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    0 ->{
                        startCategoryCreationMenu(null)
                        continue
                    }
                    else -> {

                        when(val state = startCategorySelectionMenu(inp)){
                            is StateDomain.Error -> {
                                println(state.message)
                                continue
                            }
                            is StateDomain.Success -> {
                                return state.domain
                            }
                            null -> continue
                        }
                    }
                }
            }
        }
    }
    private fun startCategorySelectionMenu(parentCategory: Int): StateDomain<Category>?{
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
                ViewService.printHeadersForMenu("Меню выбора дочерних категорий", currentCategory.name)
                displayCategory(categoryViewModel.getCategoriesByParent(parentCategory))
                ViewService.printActionsForMenu("0. Создать категорию","-1. Выбрать","-2. Редактировать","-3. Удалить","-4. Выйти")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                when(inp){
                    0 ->{
                        return if (startCategoryCreationMenu(parentCategory) is NavigationIntent.Back) continue else null
                    }
                    -1 -> {
                        when(val catState = categoryViewModel.getCategory(parentCategory)){
                            is StateDomain.Error -> {
                                println(catState.message)
                                return null
                            }
                            is StateDomain.Success -> {return catState}
                        }
                    }
                    -2 -> {
                        return if (startCategoryEditingMenu(parentCategory) is NavigationIntent.Back) continue else null
                    }
                    -3 ->{
                        return if (startCategoryDeletingMenu(parentCategory) is NavigationIntent.Back) continue else null
                    }
                    -4 -> {
                        return null
                    }
                    else -> {
                        return startCategorySelectionMenu(inp)
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