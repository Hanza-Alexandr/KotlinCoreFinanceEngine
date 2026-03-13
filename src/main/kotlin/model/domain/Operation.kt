package org.example.model.domain

import java.math.BigDecimal
import java.sql.Time
import java.util.Date
/** В разных классах я пробовал создавать объекты по разному. Где то через закрытый конструктор и
 * возвращалось состояние объекта. Где то просто если данные не проходили условия и бизнес логику выбрасывалось исключение.
 * Тут попробуем выкидывать исключение для объектов цепляемых из бд(т.е объекты классов с ID) а при создании объектов классов для новых объектов(с приставкой New и без id)
 * использовать обертки*/

sealed interface Operation{
    val amount: BigDecimal
    val date: Date
    val time: Time
    val status: String
}

sealed interface GeneralTransaction: Operation{
    val storage: Storage
    val category: Category
    val typeOperation: TypeOperation

}

@ConsistentCopyVisibility
data class CreditTransaction (
    val id: Long,
    override val storage: Storage,
    override val category: Category,
    override val amount: BigDecimal,
    override val time: Time,
    override val date: Date,
    override val status: String
): GeneralTransaction{
    override val typeOperation: TypeOperation = TypeOperation.CREDIT
}

data class DebitTransaction(
    val id: Long,
    override val storage: Storage,
    override val category: Category,
    override val amount: BigDecimal,
    override val time: Time,
    override val date: Date,
    override val status: String
): GeneralTransaction{
    override val typeOperation: TypeOperation = TypeOperation.DEBIT
}

data class NewGeneralOperation(
    override val storage: Storage,
    override val category: Category,
    override val amount: BigDecimal,
    override val time: Time,
    override val date: Date,
    override val status: String,
    override val typeOperation: TypeOperation
): GeneralTransaction



data class NewTransferTransaction(
    val fromStorage: Storage,
    val toStorage: Storage,
    override val amount: BigDecimal,
    override val date: Date,
    override val time: Time,
    override val status: String

): Operation

data class TransferTransaction(
    val id: Long,
    val fromStorage: Storage,
    val toStorage: Storage,
    override val amount: BigDecimal,
    override val date: Date,
    override val time: Time,
    override val status: String
): Operation