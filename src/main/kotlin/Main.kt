package org.example

import org.example.database.DatabaseProvider
import org.example.repository.OperationRepository
import org.example.repository.StorageRepository
import org.example.viewmodels.OperationViewModel
import org.example.viewmodels.StorageViewModel
import org.example.views.MainView
import org.example.views.OperationView
import org.example.views.StorageView
import java.time.format.DateTimeFormatter

object Formater {
    val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
}

fun main() {
    //DatabaseProvider.reset()

    val databaseProvider = DatabaseProvider()

    val opRep = OperationRepository(databaseProvider.operationQueries)
    val stRep = StorageRepository(databaseProvider.storageQueries)

    val opVM = OperationViewModel(opRep)
    val opV = OperationView(opVM)

    val stVM = StorageViewModel(stRep)
    val stV = StorageView(stVM, opV)

    val app = MainView(stV)

    app.start()


}

fun <T>testPrintList(list: List<T>){
    for (i in list){
        println(i)
    }
}







