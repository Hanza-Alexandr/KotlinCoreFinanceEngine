package org.example

sealed class NavigationIntent {
    object Exit : NavigationIntent()      // Полный выход из CategoryView
    object Back : NavigationIntent()      // Вернуться на уровень выше
    object BackHome : NavigationIntent()  // Вернуться в корень категорий
}