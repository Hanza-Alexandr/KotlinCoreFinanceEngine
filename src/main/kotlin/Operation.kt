package org.example

import java.math.BigDecimal
import java.time.LocalDateTime

enum class TypeOperation{
    DEBIT,
    CREDIT
}
interface displayOperation{
    fun printOperation()
}
abstract class Operation: displayOperation {
    abstract val id: Long?
    abstract val occurredAt: LocalDateTime
    abstract val amount: BigDecimal
    abstract val typeOperation: TypeOperation
}


abstract class GeneralTransaction: Operation(){
   abstract val category: Category
   abstract val storage: Storage

}

data class CreditTransaction(
    override val id: Long?,
    override val storage: Storage,
    override val occurredAt: LocalDateTime,
    override val category: Category,
    override val amount: BigDecimal
): GeneralTransaction(){
    override val typeOperation: TypeOperation = TypeOperation.CREDIT
    override fun printOperation() {
        println("-$amount Категория - ${category.title} ДатаВремя - ${occurredAt}")
    }
}

data class DebitTransaction(
    override val id: Long?,
    override val storage: Storage,
    override val occurredAt: LocalDateTime,
    override val category: Category,
    override val amount: BigDecimal
): GeneralTransaction(){
    override val typeOperation: TypeOperation = TypeOperation.DEBIT
    override fun printOperation() {
        println("+$amount Категория - ${category.title} ДатаВремя - ${occurredAt}")
    }
}

data class TransferTransaction(
    override val id: Long?,
    val fromStorage: Storage,
    val toStorage: Storage,
    override val typeOperation: TypeOperation,
    override val amount: BigDecimal,
    override val occurredAt: LocalDateTime

): Operation() {
    override fun printOperation() {
        val info = if (typeOperation == TypeOperation.CREDIT) {
            "-$amount перевод на ${toStorage.title} из ${fromStorage.title}"
        }
        else {
            "+$amount пополнение с ${fromStorage.title} на ${toStorage.title}"
        }
        println("$info ДатаВремя - $occurredAt")
    }
}
