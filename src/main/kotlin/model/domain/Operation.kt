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

sealed interface GeneralTransaction: Operation{
    val id: Long
    val storage: Storage
    val category: Category
    val typeOperation: TypeOperation

    fun changeStorage(newStorage: Storage): GeneralTransaction
    fun changeCategory(newCategory: Category): GeneralTransaction
    fun changeAmount(newAmount: BigDecimal): GeneralTransaction
    fun changeDate(newDate: LocalDate): GeneralTransaction
    fun changeTime(newTime: LocalTime): GeneralTransaction
    fun changeStatus(newStatusOperation: StatusOperation): GeneralTransaction


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
    override fun changeStorage(newStorage: Storage): CreditTransaction =
        copy(storage= newStorage)

    override fun changeCategory(newCategory: Category): CreditTransaction =
        copy(category = newCategory)

    override fun changeAmount(newAmount: BigDecimal): CreditTransaction =
        copy(amount = newAmount)//TODO(Отсутствует проверка на корректность данных)

    override fun changeDate(newDate: LocalDate): CreditTransaction=
        copy(date = newDate)//TODO(Отсутствует проверка на корректность данных. Не может быть больше текущей)

    override fun changeTime(newTime: LocalTime): CreditTransaction=
        copy(time = newTime)//TODO(Отсутствует проверка на корректность данных. Не может быть больше текущей)

    override fun changeStatus(newStatusOperation: StatusOperation): CreditTransaction =
        copy(status = newStatusOperation)

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
    override fun changeStorage(newStorage: Storage): DebitTransaction =
        copy(storage= newStorage)

    override fun changeCategory(newCategory: Category): DebitTransaction =
        copy(category = newCategory)

    override fun changeAmount(newAmount: BigDecimal): DebitTransaction =
        copy(amount = newAmount) //TODO(Отсутствует проверка на корректность данных)

    override fun changeDate(newDate: LocalDate): DebitTransaction=
        copy(date= newDate) //TODO(Отсутствует проверка на корректность данных. Не может быть больше текущей)

    override fun changeTime(newTime: LocalTime): DebitTransaction=
        copy(time=newTime) //TODO(Отсутствует проверка на корректность данных. Не может быть больше текущей)

    override fun changeStatus(newStatusOperation: StatusOperation): DebitTransaction =
        copy(status = newStatusOperation)
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
    fun changeFromStorage(newStorage: Storage): TransferTransaction =
        copy(fromStorage= newStorage)

    fun changeToStorage(newStorage: Storage): TransferTransaction =
        copy(toStorage= newStorage)

    fun changeAmount(newAmount: BigDecimal): TransferTransaction =
        copy(amount = newAmount) //TODO(Отсутствует проверка на корректность данных)

    fun changeDate(newDate: LocalDate): TransferTransaction=
        copy(date= newDate) //TODO(Отсутствует проверка на корректность данных. Не может быть больше текущей)

    fun changeTime(newTime: LocalTime): TransferTransaction=
        copy(time=newTime) //TODO(Отсутствует проверка на корректность данных. Не может быть больше текущей)

    fun changeStatus(newStatusOperation: StatusOperation): TransferTransaction =
        copy(status = newStatusOperation)

    init {
        if(!isValidAmount(amount)) throw IllegalArgumentException()
    }
}