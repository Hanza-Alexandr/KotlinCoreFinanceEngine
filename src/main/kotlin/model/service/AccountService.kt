package org.example.model.service

//Пока только работа с текущим пользователем. В дальнейшем можно добавить другие модули саязанные с аккаунтом
class AccountService(private val currentUserService: CurrentUserService, private val accountSettingService: AccountSettingService) {
    fun checkAuthentication(): Boolean{
        return currentUserService.isAuthentication
    }
    fun loginAsGuest(){
        currentUserService.setGuestUser()
    }
    fun logOut(){
        accountSettingService.deleteLocalAccount()
    }
}