package org.example.views

import org.example.viewmodels.AuthenticationViewModel

class AuthenticationView(
    private val authenticationViewModel: AuthenticationViewModel,
    private val logInView: LogInView,
    private val createAccountView: CreateAccountView,
    private val recoveryView: AccountRecoveryView
    ) {
    // Класс сборник вью которые относятся к аутентификации
    // Пока тут просто реализация автоматического входа если пользователь уже авторизован
    // Но может и должен быть вью входа(LoginView)
    fun start(){
        if (authenticationViewModel.isAuthentication()) return
        showMenu()
        val input = readln()
        useAction(input.toInt())
    }
    private fun showMenu(){
        println("1. Вход")
        println("2. Создать акк")
        println("3. Гость")
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
                authenticationViewModel.loginAsGuest()
            }
            else -> println("нет такого варианта")
        }
    }

}