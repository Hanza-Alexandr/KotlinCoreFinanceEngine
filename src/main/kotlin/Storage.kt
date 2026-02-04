package org.example

import java.math.BigDecimal

abstract class Storage{
    abstract val id: Long?
    abstract val title: String
}

data class General(
    override val id: Long?,
    override val title: String,
): Storage()


