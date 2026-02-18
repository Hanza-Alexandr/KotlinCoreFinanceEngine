package org.example.views.category

import org.example.model.domain.Category
import org.example.viewmodels.CategoryListState
import org.example.viewmodels.CategoryState
import org.example.viewmodels.CategoryViewModel

// ----------------------------
// Навигационные состояния
// ----------------------------
sealed class InputState {
    data class Error(val message: String) : InputState()
    data class Success(val navigationIntent: NavigationIntent?) : InputState()
}

sealed class NavigationIntent {
    object Exit : NavigationIntent()      // Полный выход из CategoryView
    object Back : NavigationIntent()      // Вернуться на уровень выше
    object BackHome : NavigationIntent()  // Вернуться в корень категорий
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
                NavigationIntent.Exit -> return          // Выход в StorageView
                NavigationIntent.Back -> continue        // Просто перерисовать корень
                NavigationIntent.BackHome -> continue    // То же самое
            }
        }
    }

    /**
     * Универсальное меню для любой категории.
     * Работает одинаково для корня и дочерних уровней.
     */
    fun startMenu(currentCategory: Category?): NavigationIntent {
        while (true) {

            val categories = categoryViewModel.getCategoryByParent(currentCategory?.id)

            displayCategory(categories, currentCategory)
            displayActions()

            when (val input = processInput(readln(), currentCategory)) {

                is InputState.Error -> {
                    println(input.message)
                }

                is InputState.Success -> {
                    when (input.navigationIntent) {
                        null -> continue                      // Остаёмся в текущем меню
                        NavigationIntent.Back -> return NavigationIntent.Back
                        NavigationIntent.BackHome -> return NavigationIntent.BackHome
                        NavigationIntent.Exit -> return NavigationIntent.Exit
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
            -1 -> InputState.Success(NavigationIntent.Back)
            -2 -> InputState.Success(NavigationIntent.BackHome)
            -3 -> InputState.Success(NavigationIntent.Exit)
            -4 -> {
                TODO()
                InputState.Success(NavigationIntent.Back)
            }

            // Переход в дочернюю категорию
            else -> {
                try {
                    val newCategory = categoryViewModel.getCategory(number.toLong())
                    val nav = startMenu(newCategory)

                    // Обработка результата вложенного меню
                    when (nav) {
                        NavigationIntent.Back -> InputState.Success(null) // Вернуться на текущий уровень
                        NavigationIntent.BackHome -> InputState.Success(nav)
                        NavigationIntent.Exit -> InputState.Success(nav)
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
