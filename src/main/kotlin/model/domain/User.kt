package org.example.model.domain
//Абстракция для разделения гостя и авторизованного пользователя
abstract class User{
    abstract val id: Long?
    abstract val isGuest: Boolean
}
data class AuthUser(
    override val id: Long,
    override val isGuest: Boolean = false
): User()
data class GuestUser(
    override val id: Long = -1L,
    override val isGuest: Boolean = true
): User()
