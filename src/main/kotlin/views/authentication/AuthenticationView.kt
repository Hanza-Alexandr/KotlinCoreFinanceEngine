package org.example.views.authentication

import org.example.viewmodels.AccountViewModel
import org.example.views.authentication.CreateAccountView
import org.example.views.authentication.LogInView

class AuthenticationView(
    private val accountViewModel: AccountViewModel,
    private val logInView: LogInView,
    private val createAccountView: CreateAccountView,
    private val recoveryView: AccountRecoveryView
    ) {
    // Класс сборник вью которые относятся к аутентификации
    // Пока тут просто реализация автоматического входа если пользователь уже авторизован
    // Но может и должен быть вью входа(LoginView)

    private fun showMenu(){
        println("====================================")
        println("              LogIn")
        println("====================================")
        println("1. Вход")
        println("2. Создать акк")
        println("3. Гость")
        println("4. Восстановление аккаунта")

    }
    fun start(){
        if (accountViewModel.isAuthentication()) return
        showMenu()
        useAction(readln().toInt())
    }
    private fun useAction(num: Int){
        when(num){
            1 -> {
                TODO("Вход еще не сделан")
            }
            2 -> {
                TODO("Создание еще не сделано")
            }
            3 -> {
                accountViewModel.loginAsGuest()
            }
            4 -> {
                TODO("Восстановление еще не сделано")
            }
            else -> println("нет такого варианта")
        }
    }

}