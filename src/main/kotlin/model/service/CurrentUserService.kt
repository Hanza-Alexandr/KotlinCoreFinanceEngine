package org.example.model.service

import org.example.model.AuthUser
import org.example.model.GuestUser
import org.example.model.repository.ICurrentUserRepository

class CurrentUserService(private val repo: ICurrentUserRepository){
    val isAuthentication: Boolean
        get() = repo.getCurrentUser() != null
    val isGuest: Boolean?
        get() = repo.getCurrentUser()?.isGuest
    val userId: Long?
        get() = repo.getCurrentUser()?.id
    fun setGuestUser(){
        repo.setCurrentUser(GuestUser())
    }
    fun setAuthUser(user: AuthUser){
        TODO()
    }

}