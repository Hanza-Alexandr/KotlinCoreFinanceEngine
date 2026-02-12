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
import org.example.viewmodels.AccountViewModel
import org.example.viewmodels.OperationViewModel
import org.example.viewmodels.StorageViewModel
import org.example.views.authentication.AccountRecoveryView
import org.example.views.authentication.AuthenticationView
import org.example.views.authentication.CreateAccountView
import org.example.views.authentication.LogInView
import org.example.views.MainView
import org.example.views.OperationView
import org.example.views.StorageView
import java.io.File

enum class AppThem{
    DARK,
    LIGHT,
    SYSTEM
}
enum class TypeStorage{
    GENERAL,
    BANK_ACCOUNT,
    CASH
}
enum class Currency{
    RUB,
    USD
}

fun main() {
    val databaseProvider = DatabaseProvider()
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
    val autVM = AccountViewModel(accSer)

    val opV = OperationView(opVM)
    val stV = StorageView(stVM, opV)
    val logView = LogInView(autVM)
    val createAccountView = CreateAccountView(autVM)
    val recoveryView = AccountRecoveryView(autVM)

    val authV = AuthenticationView(autVM,logView, createAccountView, recoveryView)

    val app = MainView(stV, authV, autVM)

    app.start()
}