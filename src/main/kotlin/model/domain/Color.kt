package org.example.model.domain

/**
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
*/
abstract class AppColor{
    abstract val hexCode: String
    abstract val owner: Owner
    companion object{
        fun isValidHex(hexCode: String): Boolean{
            val code = hexCode.lowercase()
            return if (code.length != 6) false
            else true
        }
    }
}


abstract class ExistColor: AppColor() {
    abstract val id: Long
}

class UserColor private constructor(
    override val id: Long,
    override val hexCode: String,
    user: Owner.User
): ExistColor(){
    override val owner = user
    companion object{
        fun create(id: Long, hexCode: String, user: Owner.User): UserColor{
            if (id<=0) throw IllegalArgumentException("❌Ошибка при создании объекта. Некорректный ID")
            if (!isValidHex(hexCode)) throw IllegalArgumentException("❌Ошибка при создании объекта. Некорректный Hex")
            return UserColor(id,hexCode, user)
        }
    }

    fun hexChange(newHex: String): StateDomain<UserColor>{
        return if (!isValidHex(newHex)) StateDomain.Error("❌Некорректный HexCode")
        else StateDomain.Success(UserColor.create(id,newHex,owner))
    }
}

class SystemColor private constructor(
    override val id: Long,
    override val hexCode: String
): ExistColor(){
    override val owner: Owner = Owner.System

    companion object{
        fun create(id: Long, hexCode: String): SystemColor{
            if (id<=0) throw IllegalArgumentException("❌Ошибка при создании объекта. Некорректный ID")
            if (!isValidHex(hexCode)) throw IllegalArgumentException("❌Ошибка при создании объекта. Некорректный Hex")
            return SystemColor(id,hexCode)
        }
    }
}

class NewColor private constructor(
    override val hexCode: String,
    override val owner: Owner.User

): AppColor(){
    companion object{
        fun create(hexCode: String, structure: Owner.User): StateDomain<NewColor>{
            if (!isValidHex(hexCode)) return StateDomain.Error("❌Ошибка при создании объекта. Некорректный Hex")
            return StateDomain.Success(NewColor(hexCode,structure))
        }
    }
}

