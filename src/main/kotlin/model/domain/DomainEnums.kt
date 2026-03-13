package org.example.model.domain

import org.example.InputState
import org.example.NavigationIntent
import org.example.ViewService

enum class TypeOperation{
    DEBIT,
    CREDIT;
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
        fun selectTypeStorage(): ResultMenu<TypeStorage>{
            while (true){
                ViewService.printHeadersForMenu("Selection Type Storage  menu ")
                ViewService.printActionsForMenu("1.GENERAL", "2.BANK_ACCOUNT", "3. CASH", "4. CARD", "-1. Exit")
                ViewService.printBottom()
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }
                return when (inp){
                    1 -> ResultMenu.Complete(TypeStorage.GENERAL, NavigationIntent.Back)
                    2 -> ResultMenu.Complete(TypeStorage.BANK_ACCOUNT, NavigationIntent.Back)
                    3 -> ResultMenu.Complete(TypeStorage.CASH, NavigationIntent.Back)
                    4 -> ResultMenu.Complete(TypeStorage.CARD, NavigationIntent.Back)
                    -1 -> ResultMenu.NavigationOnly(NavigationIntent.Back)
                    else -> ResultMenu.Exception("Некорректный ввод")
                }
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
        fun selectCurrency(): ResultMenu<Currency>{
            while (true){
                ViewService.printHeadersForMenu("Selection Currency menu ")
                ViewService.printActionsForMenu("1.RUB", "2.USD", "-1. Назад")
                ViewService.printBottom()
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }

                return when (inp){
                    1 -> ResultMenu.Complete(Currency.RUB, NavigationIntent.Back)
                    2 -> ResultMenu.Complete(Currency.USD, NavigationIntent.Back)
                    -1 -> ResultMenu.NavigationOnly(NavigationIntent.Back)
                    else -> ResultMenu.Exception("Некорректный ввод")
                }
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
        fun selectNeed(): NeedCategory?{
            while (true){
                println("====================================")
                println("      Меню выбора необходимости")
                println("====================================")
                println("1. Необходимо")
                println("2. Опционально")
                println("====================================")
                println("-1. Назад")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }

                when (inp){
                    1 -> {
                        return NeedCategory.MUST_HAVE
                    }
                    2 -> {
                        return NeedCategory.OPTIONAL
                    }
                    -1 -> {
                        return null
                    }
                }

            }
        }
    }
}
enum class StatusOperation{
    CONFIRMED,
    NOT_CONFIRMED;
    companion object {
        fun selectStatus(): StatusOperation?{
            while (true){
                println("====================================")
                println("      Меню выбора статуса")
                println("====================================")
                println("1. Подтвержден")
                println("2. Не подтвержден")
                println("====================================")
                println("-1. Назад")
                val inp = readln().toIntOrNull() ?: run { println("❌ОШИБКА: нужно число"); continue }

                when (inp){
                    1 -> {
                        return StatusOperation.CONFIRMED
                    }
                    2 -> {
                        return StatusOperation.NOT_CONFIRMED
                    }
                    -1 -> {
                        return null
                    }
                }
            }
        }
    }
}