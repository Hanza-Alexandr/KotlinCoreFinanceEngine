package org.example.views

class MainView(
    private val storageView: StorageView
){
    fun start(){
        var input: String
        while (true){
            showMainMenu()
            input = readln()
            useActions(input.toInt())
            if (input.toInt() == 0){
                break
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
        println("0. Exit")
        print("Choose option: ")
    }
    private fun useActions(num: Int){
        when (num) {
            1 -> {storageView.start()}
            2 -> {TODO("НЕСДЕЛАЛ {MaineView useAction 2}")}
            3 -> {TODO("НЕСДЕЛАЛ {MaineView useAction 3}")}
            else -> return
        }
    }
}