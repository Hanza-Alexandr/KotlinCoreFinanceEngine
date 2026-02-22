package org.example.model.service

import org.example.model.domain.AuthUser
import org.example.model.domain.GuestUser
import org.example.model.repository.ICurrentUserRepository
import java.lang.NullPointerException

class CurrentUserService(private val repo: ICurrentUserRepository){
    val isAuthentication: Boolean
        get() = repo.getCurrentUser() != null
    val isGuest: Boolean?
        get() = repo.getCurrentUser()?.isGuest
    val userId: Long
        get() = repo.getCurrentUser()?.id ?: throw NullPointerException("Пользователь не авторизован")
    fun setGuestUser(){
        repo.setCurrentUser(GuestUser())
    }
    fun setAuthUser(user: AuthUser){
        TODO()
    }

}