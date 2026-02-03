package org.example

import java.math.BigDecimal
import java.time.LocalDateTime

enum class TypeOperation{
    PROFIT,
    LOSS,
    TRANSFER,
}

abstract class Operation {
    abstract val id: Long?
    abstract val fromStorage: Storage
    abstract val dateTime: LocalDateTime
    abstract val amount: BigDecimal
    abstract val typeOperation: TypeOperation

    abstract fun printOperation()
}
abstract class GeneralTransaction: Operation(){
   abstract val category: Category

}

data class LossTransaction(
    override val id: Long?,
    override val fromStorage: Storage,
    override val dateTime: LocalDateTime,
    override val category: Category,
    override val amount: BigDecimal
): GeneralTransaction(){
    override val typeOperation: TypeOperation = TypeOperation.LOSS
    override fun printOperation() {
        println("Рсход - $amount Категория - ${category.title} ДатаВремя - ${dateTime}")
    }
}

data class ProfitTransaction(
    override val id: Long?,
    override val fromStorage: Storage,
    override val dateTime: LocalDateTime,
    override val category: Category,
    override val amount: BigDecimal
): GeneralTransaction(){
    override val typeOperation: TypeOperation = TypeOperation.PROFIT
    override fun printOperation() {
        println("Приход - $amount Категория - ${category.title} ДатаВремя - ${dateTime}")
    }
}

data class TransferTransaction(
    override val id: Long?,
    override val fromStorage: Storage,
    val toStorage: Storage,
    override val dateTime: LocalDateTime,
    override val amount: BigDecimal

): Operation(){
    override val typeOperation: TypeOperation = TypeOperation.TRANSFER
    override fun printOperation() {
        println("Перевод - $amount ОтКуда - ${fromStorage.title} Куда - ${toStorage.title} ДатаВремя - ${dateTime}")
    }
}
