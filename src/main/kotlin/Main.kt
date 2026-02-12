package org.example

import org.example.model.service.AccountService
import org.example.model.database.DatabaseProvider
import org.example.model.repository.sqldelightdb.CurrentUserRepositorySQLDelight
import org.example.model.repository.infile.SettingsRepositoryInFile
import org.example.model.repository.sqldelightdb.OperationRepositorySQLDelight
import org.example.model.repository.sqldelightdb.StorageRepositorySQLDelight
import org.example.model.service.AccountSettingService
import org.example.model.service.CurrentUserService
import org.example.model.service.OperationService
import org.example.model.service.StorageService
import org.example.model.service.SettingService
import org.example.viewmodels.AuthenticationViewModel
import org.example.viewmodels.OperationViewModel
import org.example.viewmodels.StorageViewModel
import org.example.views.AccountRecoveryView
import org.example.views.AuthenticationView
import org.example.views.CreateAccountView
import org.example.views.LogInView
import org.example.views.MainView
import org.example.views.OperationView
import org.example.views.StorageView
import java.io.File
import java.time.format.DateTimeFormatter
const val SETTING_USER_ID = "user_id"
object Formater {
    val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
}

enum class AppThem{
    DARK,
    LIGHT,
    SYSTEM
}
enum class TypeStorage{
    GENERAL,
    BANKACCOUNT,
    CASH
}
enum class Currency{
    RUB,
    USD
}

fun main() {
    //DatabaseProvider.reset()


    val databaseProvider = DatabaseProvider()

    //val logInRep = LogInRepositoryImp(databaseProvider.userQueries)
        //val logInVM = LogInViewModel(logInRep)
    //val logInV = LogInView(logInVM)
    val settingRep = SettingsRepositoryInFile(File("src/main/resources/settings.json"))
    val appSetting = SettingService(settingRep)
    val currentUserRepository = CurrentUserRepositorySQLDelight(appSetting)
    val currentUserSer = CurrentUserService(currentUserRepository)

    val opRep = OperationRepositorySQLDelight(databaseProvider.operationQueries, databaseProvider.transferQueries)
    val stRep = StorageRepositorySQLDelight(databaseProvider.storageQueries, currentUserSer)



    val opSer = OperationService(opRep)
    val stSer = StorageService(stRep, currentUserSer)
    val accountSettingSer= AccountSettingService(appSetting)
    val accSer = AccountService(currentUserSer,accountSettingSer)

    val opVM = OperationViewModel(opSer)
    val stVM = StorageViewModel(stSer)
    val autVM = AuthenticationViewModel(accSer)

    val opV = OperationView(opVM)
    val stV = StorageView(stVM, opV)
    val logView = LogInView(autVM)
    val createAccountView = CreateAccountView(autVM)
    val recoveryView = AccountRecoveryView(autVM)

    val authV = AuthenticationView(autVM,logView, createAccountView, recoveryView)


    val app = MainView(stV, authV, autVM)

    app.start()


}






