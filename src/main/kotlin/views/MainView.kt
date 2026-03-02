package org.example.views

import org.example.viewmodels.AccountViewModel
import org.example.views.authentication.AuthenticationView
import org.example.views.StorageView

class MainView(
    private val storageView: StorageView,
    private val authenticationView: AuthenticationView,
    private val categoryView: CategoryView,
    private val colorView: ColorView,
    private val accountViewModel: AccountViewModel
){
    private fun showMainMenu() {
        println("====================================")
        println("            FINANCE APP")
        println("------------------------------------")
        println("1. Storage")
        println("2. Categories")
        println("3. Colors")
        println("------------------------------------")
        println("-1. LogOut")
        print("Choose option: ")
        println("====================================")
    }

    fun start(){
        while (true){
            authenticationView.start()
            startMainMenu()
        }
    }

    private fun startMainMenu(){
        showMainMenu()
        val input = readln()
        useActions(input.toInt())
    }

    private fun useActions(num: Int){
        when (num) {
            1 -> TODO()
            2 -> categoryView.startMainMenu()
            3 -> colorView.start()
            -1 -> accountViewModel.logOut()
            else -> return
        }
    }
}