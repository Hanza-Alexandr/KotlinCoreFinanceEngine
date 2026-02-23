package org.example.model.domain


sealed class Color{
    sealed class ExistingColor(open val id: Long, open val hexCode: String): Color()

    data class SystemColor(
        override val id: Long,
        override val hexCode: String,
    ): ExistingColor(id,hexCode)
    data class UserColor(
        override val id: Long,
        val userId: Long,
       override val hexCode: String
    ): ExistingColor(id, hexCode)

    data class NewColor(
        val userId: Long,
        val hexCode: String
    ): Color()
}