package org.example.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.Database
import com.example.OperationsQueries
import com.example.StorageQueries
import com.example.TransferQueries
import java.io.File

//Временное и не практичное решение. где все в одном месте. Дальше можно бы ка нить выелить драйвер и подключение
class DatabaseProvider {

    private val dbPath = "src/main/resources/CoreDatabase.db"


    private val driver: JdbcSqliteDriver by lazy {
        val isNew = !File(dbPath).exists()

        val driver = JdbcSqliteDriver("jdbc:sqlite:$dbPath?foreign_keys=on")

        if (isNew) {
            Database.Schema.create(driver)
        }

        driver
    }

    val database: Database by lazy {
        Database.Companion(driver)
    }

    val storageQueries: StorageQueries by lazy {
        database.storageQueries
    }

    val operationQueries: OperationsQueries by lazy {
        database.operationsQueries
    }

    val transferQueries: TransferQueries by lazy {
        database.transferQueries
    }

}