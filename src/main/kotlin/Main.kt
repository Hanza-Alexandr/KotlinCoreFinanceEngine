package org.example

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.Database
import java.io.File
import java.math.BigDecimal


fun main() {
    val driver: SqlDriver =  JdbcSqliteDriver( "jdbc:sqlite:src/main/resources/CoreDatabase.db")
    // Create a Database
    if (!File("src/main/resources/CoreDatabase.db").exists()) {
        Database.Schema.create(driver)
    }
    // Get a reference to the queries
    //val usersQueries: UsersQueries = Database(driver).usersQueries


    val opVM = OperationViewModel()
    //val stVM = StorageViewModel()
    //val view = consoleView(opVM,stVM)
}
class StorageView(private val storageViewModel: StorageViewModel){
    fun showMenu(){
        println("1. Список Всех")
        println("2. Выбрать счет")
    }
    fun printList(){
        for (i in storageViewModel.listStorage()) println(i)
    }
    fun add(title: String, startBalance: BigDecimal){
        storageViewModel.addGeneralStorage(General(title,startBalance))
    }
    fun setNewTitle(storage: Storage,newTitle: String){
        storageViewModel.replaceGeneralStorage(storage, General(newTitle,storage.startBalance))
    }
    fun setNewStartBalance(storage: Storage,newStartBalance: BigDecimal){
        storageViewModel.replaceGeneralStorage(storage, General(storage.title,newStartBalance))
    }
}
class consoleView(
    private val operationViewModel: OperationViewModel,
    private val storageViewModel: StorageViewModel
)







