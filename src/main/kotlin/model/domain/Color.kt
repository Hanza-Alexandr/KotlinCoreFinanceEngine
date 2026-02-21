package org.example.model.domain

data class Color (
    val id: Long?,
    val userId: Long?,
    val hexCode: String,
    val isSystem: Boolean
){
    fun changeUser(newUserId: Long?): Color =
        copy(userId = newUserId)

    fun changeHex(newHex: String): Color {
        require(newHex.matches(Regex("^#?[0-9A-Fa-f]{6}$")))
        return copy(hexCode = newHex)
    }
}

/*
Можно потом как нить сделать так. Т.е проверка на выполнения треований к атрибутам сущности конкретно тут при создании. Это вроде как раз ответственность самого класса
data class Color private constructor(
    val id: Long?,
    val userId: Long?,
    val hexCode: String,
    val isSystem: Boolean
){
    companion object{
        fun create(id: Long?,userId: Long?, hexCode: String, isSystem: Boolean): InputState<Color>{
            ....
        }
    }
 */