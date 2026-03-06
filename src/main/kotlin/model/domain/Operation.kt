package org.example.model.domain

import java.math.BigDecimal
import java.sql.Time
import java.util.Date


abstract class Operation {
    abstract val id: Long?
    abstract val amount: BigDecimal
    abstract val date: Date
    abstract val time: Time
    abstract val status: String

    abstract fun changeAmount(newAmount: BigDecimal): StateDomain<Operation>
    abstract fun changeDate(newDate: Date): StateDomain<Operation>
    abstract fun changeTime(newTime: Time): StateDomain<Operation>
    abstract fun changeStatus(newStatus: String): StateDomain<Operation>
}

abstract class GeneralTransaction: Operation(){
   abstract val storage: Storage
   abstract val category: Category
   abstract val typeOperation: TypeOperation

   abstract fun changeStorage(newStorage: Storage): StateDomain<GeneralTransaction>
   abstract fun changeCategory(newCategory: Category): StateDomain<GeneralTransaction>
   abstract fun changeType(newType: TypeOperation): StateDomain<GeneralTransaction>

}

data class CreditTransaction(
    override val id: Long?,
    override val storage: Storage,
    override val category: Category,
    override val amount: BigDecimal,
    override val time: Time,
    override val date: Date,
    override val status: String
): GeneralTransaction(){
    override val typeOperation: TypeOperation = TypeOperation.CREDIT
    override fun changeStorage(newStorage: Storage): StateDomain<GeneralTransaction> {
        TODO("Not yet implemented")
    }

    override fun changeCategory(newCategory: Category): StateDomain<GeneralTransaction> {
        TODO("Not yet implemented")
    }

    override fun changeType(newType: TypeOperation): StateDomain<GeneralTransaction> {
        TODO("Not yet implemented")
    }

    override fun changeAmount(newAmount: BigDecimal): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeDate(newDate: Date): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeTime(newTime: Time): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeStatus(newStatus: String): StateDomain<Operation> {
        TODO("Not yet implemented")
    }
}

data class DebitTransaction(
    override val id: Long?,
    override val storage: Storage,
    override val category: Category,
    override val amount: BigDecimal,
    override val time: Time,
    override val date: Date,
    override val status: String
): GeneralTransaction(){
    override val typeOperation: TypeOperation = TypeOperation.DEBIT
    override fun changeStorage(newStorage: Storage): StateDomain<GeneralTransaction> {
        TODO("Not yet implemented")
    }

    override fun changeCategory(newCategory: Category): StateDomain<GeneralTransaction> {
        TODO("Not yet implemented")
    }

    override fun changeType(newType: TypeOperation): StateDomain<GeneralTransaction> {
        TODO("Not yet implemented")
    }

    override fun changeAmount(newAmount: BigDecimal): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeDate(newDate: Date): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeTime(newTime: Time): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeStatus(newStatus: String): StateDomain<Operation> {
        TODO("Not yet implemented")
    }
}

data class TransferTransaction(
    override val id: Long?,
    val fromStorage: Storage,
    val toStorage: Storage,
    override val amount: BigDecimal,
    override val date: Date,
    override val time: Time,
    override val status: String

): Operation() {
    fun changeFromStorage(newFrom: Storage): StateDomain<TransferTransaction>  {
        TODO("Not yet implemented")
    }

    fun changeToStorage(newTo: Storage): StateDomain<TransferTransaction> {
        TODO("Not yet implemented")
    }

    override fun changeAmount(newAmount: BigDecimal): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeDate(newDate: Date): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeTime(newTime: Time): StateDomain<Operation> {
        TODO("Not yet implemented")
    }

    override fun changeStatus(newStatus: String): StateDomain<Operation> {
        TODO("Not yet implemented")
    }
}