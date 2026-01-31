package org.example

import java.math.BigDecimal
import java.security.PrivateKey
import java.time.LocalDateTime
import java.util.Date
import kotlin.math.abs


fun main() {

    val category1 = Category("Еда",null)
    val category2 = Category("Фастфуд", category1)
    val category3 = Category("Доход",null)

    val categories: Set<Category> = setOf(category1,category2)


    val storage1 = Cash(BigDecimal("7000"),mutableListOf())
    val storage2 = DebitBancAccount(BigDecimal("7000"),mutableListOf(),null)
    val storages: Set<Storage> = setOf(storage1,storage2)

    val transaction1 = Expense(
        storage1,
        LocalDateTime.of(2024, 5, 10, 14, 30),
        "Печеньки", BigDecimal("7000"),
        category1)
    val transaction2 = Income(
        storage2,
        LocalDateTime.of(2024, 5, 10, 14, 30),
        "ЗП", BigDecimal("7000"),
        category3 )

    storage2.transactionList.add(transaction2)
    print(storage2.balance)
}





