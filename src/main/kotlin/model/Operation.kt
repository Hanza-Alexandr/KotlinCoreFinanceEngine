package org.example.model

import java.math.BigDecimal
import java.sql.Time
import java.time.LocalDateTime
import java.util.Date
import javax.xml.crypto.Data

enum class TypeOperation{
    DEBIT,
    CREDIT
}
abstract class Operation {
    abstract val id: Long?
    abstract val amount: BigDecimal
    abstract val date: Date
    abstract val time: Time
    abstract val status: String

    abstract fun changeAmount(newAmount: BigDecimal): Operation
    abstract fun changeDate(newDate: Date): Operation
    abstract fun changeTime(newTime: Time): Operation
    abstract fun changeStatus(newStatus: String): Operation
}

abstract class GeneralTransaction: Operation(){
   abstract val storage: Storage
   abstract val category: Category
   abstract val typeOperation: TypeOperation

   abstract fun changeStorage(newStorage: Storage): GeneralTransaction
   abstract fun changeCategory(newCategory: Category): GeneralTransaction
   abstract fun changeType(newType: TypeOperation): GeneralTransaction

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
    override fun changeStorage(newStorage: Storage): GeneralTransaction {
        TODO("Not yet implemented")
    }

    override fun changeCategory(newCategory: Category): GeneralTransaction {
        TODO("Not yet implemented")
    }

    override fun changeType(newType: TypeOperation): GeneralTransaction {
        TODO("Not yet implemented")
    }

    override fun changeAmount(newAmount: BigDecimal): Operation {
        TODO("Not yet implemented")
    }

    override fun changeDate(newDate: Date): Operation {
        TODO("Not yet implemented")
    }

    override fun changeTime(newTime: Time): Operation {
        TODO("Not yet implemented")
    }

    override fun changeStatus(newStatus: String): Operation {
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
    override fun changeStorage(newStorage: Storage): GeneralTransaction {
        TODO("Not yet implemented")
    }

    override fun changeCategory(newCategory: Category): GeneralTransaction {
        TODO("Not yet implemented")
    }

    override fun changeType(newType: TypeOperation): GeneralTransaction {
        TODO("Not yet implemented")
    }

    override fun changeAmount(newAmount: BigDecimal): Operation {
        TODO("Not yet implemented")
    }

    override fun changeDate(newDate: Date): Operation {
        TODO("Not yet implemented")
    }

    override fun changeTime(newTime: Time): Operation {
        TODO("Not yet implemented")
    }

    override fun changeStatus(newStatus: String): Operation {
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
    fun changeFromStorage(newFrom: Storage): TransferTransaction {
        require(newFrom != toStorage)
        return copy(fromStorage = newFrom)
    }

    fun changeToStorage(newTo: Storage): TransferTransaction {
        require(newTo != fromStorage)
        return copy(toStorage = newTo)
    }

    override fun changeAmount(newAmount: BigDecimal): Operation {
        TODO("Not yet implemented")
    }

    override fun changeDate(newDate: Date): Operation {
        TODO("Not yet implemented")
    }

    override fun changeTime(newTime: Time): Operation {
        TODO("Not yet implemented")
    }

    override fun changeStatus(newStatus: String): Operation {
        TODO("Not yet implemented")
    }
}