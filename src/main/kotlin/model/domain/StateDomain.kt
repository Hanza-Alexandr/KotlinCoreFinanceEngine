package org.example.model.domain

sealed class StateDomainList<out T> {
    data class Success<T>(val domainList: List<T>): StateDomainList<T>()
    object Empty: StateDomainList<Nothing>()
}
sealed class StateDomain<T> {
    data class Success<T>(val domain: T) : StateDomain<T>()
    data class Error<T>(val message: String) : StateDomain<T>()
}
