package org.example.model.repository.sqldelightdb

import org.example.model.domain.AuthUser
import org.example.model.domain.GuestUser
import org.example.model.domain.User
import org.example.model.repository.ICurrentUserRepository
import org.example.model.service.SettingService


class CurrentUserRepositorySQLDelight(private val service: SettingService): ICurrentUserRepository {
    override fun getCurrentUser(): User? {
        val userId = service.load().userId
        when(userId){
            null -> return null
            -1L -> return GuestUser()
            else -> return AuthUser(userId)
        }
    }

    override fun setCurrentUser(user: User) {
        service.update {  it.copy(userId = user.id)}
    }
}

