package org.example.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.CategoriesQueries
import com.example.Database
import com.example.OperationsQueries
import com.example.StorageQueries
import java.io.File

//Временное и не практичное решение. где все в одном месте. Дальше можно бы ка нить выелить драйвер и подключение
class DatabaseProvider {

    private val dbPath = "src/main/resources/CoreDatabase.db"


    private val driver: SqlDriver by lazy {
        JdbcSqliteDriver(url = "jdbc:sqlite:$dbPath").also {
            it.execute(null, "PRAGMA foreign_keys = ON", 0)
            if (!File(dbPath).exists()) {
                Database.Companion.Schema.create(it)
            }
        }
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

    val categoryQueries: CategoriesQueries by lazy {
        database.categoriesQueries
    }
}