package org.example

import org.example.model.service.AccountService
import org.example.model.database.DatabaseProvider
import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.domain.StateDomainList
import org.example.model.domain.Storage
import org.example.model.repository.sqldelightdb.CurrentUserRepositorySQLDelight
import org.example.model.repository.infile.SettingsRepositoryInFile
import org.example.model.repository.sqldelightdb.CategoryRepositorySQLDelight
import org.example.model.repository.sqldelightdb.ColorRepositorySQLDelight
import org.example.model.repository.sqldelightdb.OperationRepositorySQLDelight
import org.example.model.repository.sqldelightdb.StorageRepositorySQLDelight
import org.example.model.service.AccountSettingService
import org.example.model.service.CategoryService
import org.example.model.service.ColorService
import org.example.model.service.CurrentUserService
import org.example.model.service.OperationService
import org.example.model.service.StorageService
import org.example.model.service.SettingService
import org.example.viewmodels.AccountViewModel
import org.example.viewmodels.CategoryViewModel
import org.example.viewmodels.ColorViewModel
import org.example.viewmodels.OperationViewModel
import org.example.viewmodels.StorageViewModel
import org.example.views.ColorView
import org.example.views.authentication.AccountRecoveryView
import org.example.views.authentication.AuthenticationView
import org.example.views.authentication.CreateAccountView
import org.example.views.authentication.LogInView
import org.example.views.MainView
import org.example.views.OperationView
import org.example.views.CategoryView
import org.example.views.StorageView
import java.io.File
import javax.xml.catalog.Catalog

sealed class InputState<out T> {
    data class Error(val message: String) : InputState<Nothing>()
    data class Success<T>(val obj: T) : InputState<T>()
}

class ViewService{
    companion object{
        private val red = "\u001B[31m"
        private val green = "\u001B[32m"
        private val yellow = "\u001B[33m"
        private val blue = "\u001B[34m"
        private val reset = "\u001B[0m"

        fun printHeadersForMenu(title: String, vararg optionalInfo: String = emptyArray()){
            println("====================================")
            println("${blue}$title$reset")
            for (i in optionalInfo){
                println(i)
            }
            println("------------------------------------")
        }
        fun printActionsForMenu(vararg yourActions: String){
            for (i in yourActions){
                println(i)
            }
        }

        fun printBottom(){
            println("====================================")
        }
        fun printHeaderChoose(){
            print("${green}Choose option:$reset")
        }

        fun <T> printListDomain(list: StateDomainList<T>,print: (T)-> Unit){
            when(list){
                is StateDomainList.Empty -> {
                    println("⚠️Предупреждение: Список пустой")
                }
                is StateDomainList.Success -> {
                    for (i in list.domainList){
                        print(i)
                    }
                }
            }
        }
    }
}


fun main() {
    val databaseProvider = DatabaseProvider()
    val settingRep = SettingsRepositoryInFile(File("src/main/resources/settings.json"))
    val appSetting = SettingService(settingRep)
    val currentUserRepository = CurrentUserRepositorySQLDelight(appSetting)
    val currentUserSer = CurrentUserService(currentUserRepository)

    val opRep = OperationRepositorySQLDelight(databaseProvider.operationQueries, databaseProvider.transferQueries)
    val stRep = StorageRepositorySQLDelight(databaseProvider.storageQueries)
    val catRep = CategoryRepositorySQLDelight(databaseProvider.categoryQueries)
    val colRep = ColorRepositorySQLDelight(databaseProvider.colorQueries)



    val opSer = OperationService(opRep)
    val stSer = StorageService(stRep, currentUserSer)
    val accountSettingSer= AccountSettingService(appSetting)
    val accSer = AccountService(currentUserSer,accountSettingSer)
    val catSer = CategoryService(catRep,currentUserSer)
    val colSer = ColorService(colRep, currentUserSer)

    val opVM = OperationViewModel(opSer)
    val stVM = StorageViewModel(stSer)
    val accVM = AccountViewModel(accSer)
    val catVM = CategoryViewModel(catSer)
    val colVM = ColorViewModel(colSer)

    val opV = OperationView(opVM)
    val stV = StorageView(stVM, opV)
    val logView = LogInView(accVM)
    val createAccountView = CreateAccountView(accVM)
    val recoveryView = AccountRecoveryView(accVM)

    val colV = ColorView(colVM)
    val catV = CategoryView(catVM,colV)

    val authV = AuthenticationView(accVM,logView, createAccountView, recoveryView)

    val app = MainView(
        storageView = stV,
        authenticationView = authV,
        categoryView = catV,
        colorView = colV,
        accountViewModel = accVM,
    )

    app.start()
}