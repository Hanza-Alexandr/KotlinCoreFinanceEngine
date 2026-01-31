package org.example

import java.math.BigDecimal
import java.time.LocalDateTime

enum class TypeOperation{
    PROFIT,
    LOSS,
}

abstract class Operation() {
    abstract val fromStorage: Storage
    abstract val dateTime: LocalDateTime
    abstract val amount: BigDecimal
}
data class GeneralTransaction(
    override val fromStorage: Storage,
    val typeOperation: TypeOperation,
    override val dateTime: LocalDateTime,
    override val amount: BigDecimal,
    val category: Category,
): Operation()

data class TransferTransaction(
    override val fromStorage: Storage,
    val toStorage: Storage,
    override val dateTime: LocalDateTime,
    override val amount: BigDecimal

): Operation()
