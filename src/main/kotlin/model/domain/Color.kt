package org.example.model.domain

data class Color (
    val id: Long?,
    val userId: Long?,
    val hexCode: String
){
    fun changeUser(newUserId: Long?): Color =
        copy(userId = newUserId)

    fun changeHex(newHex: String): Color {
        require(newHex.matches(Regex("^#?[0-9A-Fa-f]{6}$")))
        return copy(hexCode = newHex)
    }
}