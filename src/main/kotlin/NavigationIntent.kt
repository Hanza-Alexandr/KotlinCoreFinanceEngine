package org.example

sealed class NavigationIntent {
    object Exit : NavigationIntent()      // Полный выход из CategoryView
    object Back : NavigationIntent()      // Вернуться на уровень выше
    object BackHome : NavigationIntent()  // Вернуться в корень категорий
}

/*
В теории можно сдлеать так если из какого то экрана нужно назад еще вернуть объект
sealed class NavigationIntent2<T> {
    data class Exit<T>(val obj: T?) : NavigationIntent2<T>()      // Полный выход из CategoryView
    data class Back<T>(val obj: T?) : NavigationIntent2<T>()      // Вернуться на уровень выше
    data class BackHome<T>(val obj: T?) : NavigationIntent2<T>()  // Вернуться в корень категорий
}

 */