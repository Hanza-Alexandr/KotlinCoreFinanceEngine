package org.example.views.main

import org.example.viewmodels.AccountViewModel
import org.example.views.ColorView
import org.example.views.authentication.AuthenticationView
import org.example.views.category.CategoryView
import org.example.views.storage.StorageView

class MainView(
    private val storageView: StorageView,
    private val authenticationView: AuthenticationView,
    private val categoryView: CategoryView,
    private val accountViewModel: AccountViewModel
){
    private fun showMainMenu() {
        println("====================================")
        println("            FINANCE APP")
        println("====================================")
        println("1. Счета")
        println("2. Категории")
        println("3. Категории")
        println("4. Цвета")


        println("====================================")
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
            2 -> {categoryView.startCategoryMainMenu()}
            -1 -> {accountViewModel.logOut()}
            else -> return
        }
    }
}

