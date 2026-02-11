package org.example.views

import org.example.SETTING_USER_ID
import org.example.viewmodels.AuthenticationViewModel
import kotlin.math.log

class MainView(
    private val storageView: StorageView,
    private val authenticationView: AuthenticationView,
    private val authenticationViewModel: AuthenticationViewModel
){
    fun start(){
        while (true){
            authenticationView.start()
            startMainMenu()
        }

    }

    private fun startMainMenu(){
        //TODO() Нет проверок на корректность веденных данных
        var input: String
        while (true){
            showMainMenu()
            input = readln()
            useActions(input.toInt())
            when(input.toInt()){
                -1 ->break
            }
        }
    }

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
    private fun useActions(num: Int){
        when (num) {
            1 -> {storageView.start()}
            2 -> {TODO("НЕСДЕЛАЛ {MaineView useAction 2}")}
            3 -> {TODO("НЕСДЕЛАЛ {MaineView useAction 3}")}
            -1 -> {authenticationViewModel.logOut()}
            else -> return
        }
    }
}