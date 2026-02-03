package org.example

import java.math.BigDecimal

abstract class Storage{
    abstract val id: Long?
    abstract val title: String
    abstract val startBalance: BigDecimal
}

data class General(
    override val id: Long?,
    override val title: String,
    override val startBalance: BigDecimal,
): Storage()


