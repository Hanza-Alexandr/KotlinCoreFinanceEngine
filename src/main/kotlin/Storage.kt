package org.example

import java.math.BigDecimal

abstract class Storage(){
    abstract val startBalance: BigDecimal
    abstract val transactionList: MutableList<Transaction>
    val balance: BigDecimal
    get() = startBalance + run { ->
        var count: BigDecimal = BigDecimal("0")
        if (transactionList.isNotEmpty()){
            for (i in transactionList){
                count += i.countMoney
            }
        }
        count
    }
}

data class General(
    override val startBalance: BigDecimal,
    override val transactionList: MutableList<Transaction>
): Storage()

data class Cash(
    override val startBalance: BigDecimal,
    override val transactionList: MutableList<Transaction>
): Storage(){

}
abstract class BancAccount(): Storage(){
    abstract override val startBalance: BigDecimal
    abstract override val transactionList: MutableList<Transaction>
    abstract val cards: List<BancCard>?
}

data class BancCard(val title: String)

data class DebitBancAccount(
    override val startBalance: BigDecimal,
    override val transactionList: MutableList<Transaction>,
    override val cards: List<BancCard>?
): BancAccount()

data class CreditBancAccount(
    override val startBalance: BigDecimal,
    override val transactionList: MutableList<Transaction>,
    override val cards: List<BancCard>?
): BancAccount()