package org.example.model.domain

import org.example.model.domain.Operation.Companion.isValidAmount
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

/** В разных классах я пробовал создавать объекты по разному. Где то через закрытый конструктор и
 * возвращалось состояние объекта. Где то просто если данные не проходили условия и бизнес логику выбрасывалось исключение.
 * Тут попробуем выкидывать исключение для объектов цепляемых из бд(т.е объекты классов с ID) а при создании объектов классов для новых объектов(с приставкой New и без id)
 * использовать обертки*/

sealed interface Operation{
    val amount: BigDecimal
    val date: LocalDate
    val time: LocalTime
    val status: StatusOperation

    companion object{
        fun isValidAmount(amount: BigDecimal): Boolean{
            return amount > BigDecimal.ZERO
        }
    }
}
sealed interface NewOperation : Operation {
    // интерфейс маркер чисто для обозначения новых операций
}

sealed interface GeneralTransaction: Operation, NewOperation{
    val id: Long
    val storage: Storage
    val category: Category
    val typeOperation: TypeOperation

}

data class CreditTransaction (
    override val id: Long,
    override val storage: Storage,
    override val category: Category,
    override val amount: BigDecimal,
    override val time: LocalTime,
    override val date: LocalDate,
    override val status: StatusOperation
): GeneralTransaction{
    override val typeOperation: TypeOperation = TypeOperation.CREDIT
    init {
        if(!isValidAmount(amount)) throw IllegalArgumentException()
    }
}

data class DebitTransaction(
    override val id: Long,
    override val storage: Storage,
    override val category: Category,
    override val amount: BigDecimal,
    override val time: LocalTime,
    override val date: LocalDate,
    override val status: StatusOperation
): GeneralTransaction{
    override val typeOperation: TypeOperation = TypeOperation.DEBIT
    init {
        if(!isValidAmount(amount)) throw IllegalArgumentException()
    }
}

@ConsistentCopyVisibility
data class NewGeneralOperation private constructor(
    val storage: Storage,
    val category: Category,
    override val amount: BigDecimal,
    override val time: LocalTime,
    override val date: LocalDate,
    override val status: StatusOperation,
    val typeOperation: TypeOperation
): Operation, NewOperation{
    companion object{
        fun create(storage: Storage, category: Category, amount: BigDecimal, time: LocalTime, date: LocalDate, status: StatusOperation, typeOperation: TypeOperation): StateDomain<NewGeneralOperation>{
            try {
                if(!isValidAmount(amount)) throw IllegalArgumentException()
                return StateDomain.Success(NewGeneralOperation(storage, category,amount, time, date, status, typeOperation))
            }
            catch (e: IllegalArgumentException){
                return StateDomain.Error("❌Ошибка при создании new сущности")
            }
        }
    }
}



@ConsistentCopyVisibility
data class NewTransferTransaction private constructor(
    val fromStorage: Storage,
    val toStorage: Storage,
    override val amount: BigDecimal,
    override val time: LocalTime,
    override val date: LocalDate,
    override val status: StatusOperation
): Operation, NewOperation{
    companion object{
        fun create(fromStorage:Storage, toStorage: Storage, amount: BigDecimal, time: LocalTime, date: LocalDate, status: StatusOperation): StateDomain<NewTransferTransaction>{
            try {
                if(!isValidAmount(amount)) throw IllegalArgumentException()
                return StateDomain.Success(NewTransferTransaction(fromStorage, toStorage,amount, time, date, status))
            }
            catch (e: IllegalArgumentException){
                return StateDomain.Error("❌Ошибка при создании new сущности")
            }
        }
    }
}

data class TransferTransaction(
    val id: Long,
    val fromStorage: Storage,
    val toStorage: Storage,
    override val amount: BigDecimal,
    override val date: LocalDate,
    override val time: LocalTime,
    override val status: StatusOperation
): Operation{
    init {
        if(!isValidAmount(amount)) throw IllegalArgumentException()
    }
}