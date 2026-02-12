package org.example.views

import org.example.viewmodels.AccountViewModel
import org.example.views.authentication.AuthenticationView

class MainView(
    private val storageView: StorageView,
    private val authenticationView: AuthenticationView,
    private val accountViewModel: AccountViewModel
){
    private fun showMainMenu() {
        println("====================================")
        println("            FINANCE APP")
        println("====================================")
        println("1. Storages (Счета)")
        println("TODO 2. Categories (Категории)")
        println("TODO 3. Types of Operation (Типы операций)")
        println("-1. LogOut")
        print("Choose option: ")
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
            1 -> {storageView.start()}
            2 -> {TODO("НЕСДЕЛАЛ {MaineView useAction 2}")}
            3 -> {TODO("НЕСДЕЛАЛ {MaineView useAction 3}")}
            -1 -> {accountViewModel.logOut()}
            else -> return
        }
    }
}