package org.example.model.repository

import org.example.model.AuthUser
import org.example.model.GuestUser
import org.example.model.User
import org.example.model.service.SettingService


class CurrentUserRepositoryInMem(private val service: SettingService): ICurrentUserRepository {
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

