package org.example.model.domain

import org.example.NavigationIntent

sealed class StateDomainList<out T> {
    data class Success<T>(val domainList: List<T>): StateDomainList<T>()
    object Empty: StateDomainList<Nothing>()
}
sealed class StateDomain<T> {
    data class Success<T>(val domain: T) : StateDomain<T>()
    data class Error<T>(val message: String) : StateDomain<T>()
}

sealed class ResultSelectionMenu<out T> {
    data class Selected<T>(
        val item:T,
        val navigation: NavigationIntent
    ) : ResultSelectionMenu<T>()

    data class NavigationOnly(
        val navigation: NavigationIntent
    ) : ResultSelectionMenu<Nothing>()
    
    data class Exception(val message: String): ResultSelectionMenu<Nothing>()
}
