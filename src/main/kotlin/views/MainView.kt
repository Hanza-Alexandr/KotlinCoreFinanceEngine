package org.example.views

import org.example.ViewService
import org.example.viewmodels.AccountViewModel
import org.example.views.authentication.AuthenticationView
import org.example.views.StorageView
import javax.swing.text.View

class MainView(
    private val storageView: StorageView,
    private val authenticationView: AuthenticationView,
    private val categoryView: CategoryView,
    private val colorView: ColorView,
    private val accountViewModel: AccountViewModel
){
    fun start(){
        while (true){
            authenticationView.start()
            startMainMenu()
        }
    }

    private fun startMainMenu(){
        ViewService.printHeadersForMenu("FINANCE APP")
        ViewService.printActionsForMenu("1. Storage", "2. Categories", "3. Colors", "-1. LogOut")
        ViewService.printBottom()
        ViewService.printHeaderChoose()
        val input = readln()
        useActions(input.toInt())
    }

    private fun useActions(num: Int){
        when (num) {
            1 -> TODO("Счета не сделаны")
            2 -> categoryView.startMainMenu()
            3 -> colorView.start()
            -1 -> accountViewModel.logOut()
            else -> return
        }
    }
}