package org.example.views.category

import org.example.model.domain.Category
import org.example.viewmodels.CategoryViewModel

// ----------------------------
// Навигационные состояния
// ----------------------------
sealed class InputState {
    data class Error(val message: String) : InputState()
    data class Success(val navigationState: NavigationState?) : InputState()
}

sealed class NavigationState {
    object Exit : NavigationState()      // Полный выход из CategoryView
    object Back : NavigationState()      // Вернуться на уровень выше
    object BackHome : NavigationState()  // Вернуться в корень категорий
}

// ----------------------------
// CategoryView
// ----------------------------
class CategoryView(private val categoryViewModel: CategoryViewModel) {

    /**
     * Точка входа из StorageView.
     * Здесь всегда показываются базовые категории.
     */
    fun startCategoryMainMenu() {
        while (true) {
            val nav = startMenu(currentCategory = null)

            when (nav) {
                NavigationState.Exit -> return          // Выход в StorageView
                NavigationState.Back -> continue        // Просто перерисовать корень
                NavigationState.BackHome -> continue    // То же самое
            }
        }
    }

    /**
     * Универсальное меню для любой категории.
     * Работает одинаково для корня и дочерних уровней.
     */
    fun startMenu(currentCategory: Category?): NavigationState {
        while (true) {

            val categories = categoryViewModel.getCategoryByParent(currentCategory?.id)

            displayCategory(categories, currentCategory)
            displayActions()

            when (val input = processInput(readln(), currentCategory)) {

                is InputState.Error -> {
                    println(input.message)
                }

                is InputState.Success -> {
                    when (input.navigationState) {
                        null -> continue                      // Остаёмся в текущем меню
                        NavigationState.Back -> return NavigationState.Back
                        NavigationState.BackHome -> return NavigationState.BackHome
                        NavigationState.Exit -> return NavigationState.Exit
                    }
                }
            }
        }
    }

    /**
     * Печать списка категорий.
     */
    fun displayCategory(list: CategoryListState, currentCategory: Category?) {
        val title = currentCategory?.name ?: "СПИСОК"
        println("              $title:")
        println("====================================")

        when (list) {
            CategoryListState.Empty -> println("Категорий нет")
            is CategoryListState.Success -> list.category.forEach {
                println("${it.id}|${it.name}")
            }
        }

        println("====================================")
    }

    /**
     * Печать доступных действий.
     */
    fun displayActions() {
        println("<номер категории>. Выбор категории")
        println("0. Создать категорию")
        println("====================================")
        println("-1. Назад")
        println("-2. В глав. меню категорий")
        println("-3. Выйти из категорий")
        println("-4. УДАЛИТЬ")

    }

    /**
     * Обработка ввода пользователя.
     * Возвращает InputState, который затем интерпретируется в startMenu().
     */
    fun processInput(input: String, currentCategory: Category?): InputState {

        val number = input.toIntOrNull()
            ?: return InputState.Error("Нужно ввести цифру")

        return when (number) {

            // Создание категории
            0 -> {
               val state = startMenuCreation(currentCategory)
                when(state){
                    is CategoryState.Success -> {println("✅Все категория создана")}
                    is CategoryState.Error -> {println(state.message)}
                }
                InputState.Success(null)
            }

            // Навигация
            -1 -> InputState.Success(NavigationState.Back)
            -2 -> InputState.Success(NavigationState.BackHome)
            -3 -> InputState.Success(NavigationState.Exit)
            -4 -> {
                TODO()
                InputState.Success(NavigationState.Back)
            }

            // Переход в дочернюю категорию
            else -> {
                try {
                    val newCategory = categoryViewModel.getCategory(number.toLong())
                    val nav = startMenu(newCategory)

                    // Обработка результата вложенного меню
                    when (nav) {
                        NavigationState.Back -> InputState.Success(null) // Вернуться на текущий уровень
                        NavigationState.BackHome -> InputState.Success(nav)
                        NavigationState.Exit -> InputState.Success(nav)
                    }

                } catch (e: IllegalArgumentException) {
                    InputState.Error("Нет такого действия/категории")
                }
            }
        }
    }

    /**
     * Меню создания категории.
     */
    private fun startMenuCreation(currentCategory: Category?): CategoryState {
        try {
            println("====================================")
            println("      Меню создания категории")
            println("====================================")
            print("Название: ")

            val name = readln()
            categoryViewModel.createCategory(name, currentCategory)
            return CategoryState.Success
        }
        catch (e: Exception){
            return CategoryState.Error("❌Ошибка при создании")
        }
    }

    private fun startMenuDeleting(currentCategory: Category): CategoryState{
        try {

            TODO()

        }catch (e: Exception){
            return CategoryState.Error("❌Ошибка при удалении")
        }
    }

}
