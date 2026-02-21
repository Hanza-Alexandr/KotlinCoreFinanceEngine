package org.example

import org.example.model.service.AccountService
import org.example.model.database.DatabaseProvider
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
import org.example.views.color.ColorView
import org.example.views.authentication.AccountRecoveryView
import org.example.views.authentication.AuthenticationView
import org.example.views.authentication.CreateAccountView
import org.example.views.authentication.LogInView
import org.example.views.main.MainView
import org.example.views.OperationView
import org.example.views.category.CategoryView
import org.example.views.storage.StorageView
import java.io.File

const val STANDARD_COLOR_HEX = "ABABAB"

enum class AppThem{
    DARK,
    LIGHT,
    SYSTEM;
    companion object {
        fun getObj(inp: String): InputState<NeedCategory> {
            return try {
                InputState.Success(NeedCategory.valueOf(inp))

            } catch (e: IllegalArgumentException) {
                InputState.Error("Нет такой Темы")
            }
        }
    }
}
enum class TypeStorage {
    GENERAL,
    BANK_ACCOUNT,
    CASH,
    CARD;

    companion object {
        fun getObj(inp: String): InputState<NeedCategory> {
            return try {
                InputState.Success(NeedCategory.valueOf(inp))

            } catch (e: IllegalArgumentException) {
                InputState.Error("Нет такого типа")
            }

        }
    }
}
enum class Currency{
    RUB,
    USD;
    companion object {
        fun getObj(inp: String): InputState<NeedCategory> {
            return try {
                InputState.Success(NeedCategory.valueOf(inp))

            } catch (e: IllegalArgumentException) {
                InputState.Error("Нет такой Валюты")
            }

        }
    }
}
enum class NeedCategory{
    MUST_HAVE,
    OPTIONAL;
    companion object {
        fun getObj(inp: String): InputState<NeedCategory> {
            return try {
                InputState.Success(NeedCategory.valueOf(inp))

            } catch (e: IllegalArgumentException) {
                InputState.Error("Нет такой необходисости")
            }

        }
    }
}

sealed class InputState<out T> {
    data class Error(val message: String) : InputState<Nothing>()
    data class Success<T>(val obj: T) : InputState<T>()
}


fun main() {
    val databaseProvider = DatabaseProvider()
    val settingRep = SettingsRepositoryInFile(File("src/main/resources/settings.json"))
    val appSetting = SettingService(settingRep)
    val currentUserRepository = CurrentUserRepositorySQLDelight(appSetting)
    val currentUserSer = CurrentUserService(currentUserRepository)

    val opRep = OperationRepositorySQLDelight(databaseProvider.operationQueries, databaseProvider.transferQueries)
    val stRep = StorageRepositorySQLDelight(databaseProvider.storageQueries, currentUserSer)
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