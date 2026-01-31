package org.example

import java.math.BigDecimal
import java.time.LocalDateTime

abstract class Transaction(){
    abstract val storage: Storage
    abstract val dateTime: LocalDateTime
    abstract val title: String
    abstract val countMoney: BigDecimal

}

interface MoneyMovement{
    //TODO()
}


data class Income(
    override val storage: Storage,
    override val dateTime: LocalDateTime,
    override val title: String,
    override val countMoney: BigDecimal,
    val category: Category
): MoneyMovement, Transaction()

data class Expense(
    override val storage: Storage,
    override val dateTime: LocalDateTime,
    override val title: String,
    override val countMoney: BigDecimal,
    val category: Category
): MoneyMovement, Transaction()

data class Transfer(
    override val storage: Storage,
    val toStorageId: Long,
    override val dateTime: LocalDateTime,
    override val title: String,
    override val countMoney: BigDecimal
): Transaction()