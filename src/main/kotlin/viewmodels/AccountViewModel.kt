package org.example.viewmodels

import org.example.model.service.AccountService

class AccountViewModel(private val service: AccountService) {

    fun isAuthentication(): Boolean{
       return service.checkAuthentication()
    }
    fun loginAsGuest(){
        service.loginAsGuest()
    }

    fun logOut(){
        service.logOut()
    }
}