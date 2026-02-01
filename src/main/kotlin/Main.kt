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








