package org.example.model.domain

import org.example.InputState

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