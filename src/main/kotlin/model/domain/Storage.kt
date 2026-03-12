package org.example.model.domain

abstract class BaseStorage{
    abstract val name: String
    abstract val userId: Long
    abstract val currency: Currency
    abstract val typeStorage: TypeStorage
    abstract val note: String?
    abstract val color: ExistColor
    abstract val isStatistics: Boolean
    abstract val isArchive: Boolean

    companion object{
        fun isNameValid(name: String): Boolean{
            return name.isNotBlank() && !name.startsWith(" ")
        }
        fun isNoteValid(note: String?): Boolean{
            if (note == null) return true
            return note.isNotBlank() && !note.startsWith(" ")
        }
    }
}

data class Storage(
    val id: Long,
    override val name: String,
    override val userId: Long,
    override val currency: Currency,
    override val typeStorage: TypeStorage,
    override val note: String?,
    override val color: ExistColor,
    override val isStatistics: Boolean,
    override val isArchive: Boolean
):BaseStorage(){
    companion object{
        fun create(id: Long, name: String, userId: Long, currency: Currency, typeStorage: TypeStorage, note: String?, color: ExistColor, isStatistics: Boolean, isArchive: Boolean): Storage{
            /** Объект класса создается из данных из бд. Поэтому если там ошибка лучше увидеть ее сразу*/
            if(!isNameValid(name)) throw IllegalArgumentException()
            if(!isNoteValid(note)) throw IllegalArgumentException()
            return Storage(id, name, userId, currency, typeStorage, note, color, isStatistics, isArchive)
        }
    }
    fun changeName(newName: String): Storage =
        copy(name = newName) //TODO()отсутсвует проверка на корректность. Т.к я хз как сделать так что бы было удобно использовать функцию и что бы я был уверен что имя соответсвует логикик и огранияениям

    fun changeType(newType: TypeStorage): Storage =
        copy(typeStorage = newType)

    fun changeNote(newNote: String?): Storage=
        copy(note = newNote) //TODO()отсутсвует проверка на корректность. Т.к я хз как сделать так что бы было удобно использовать функцию и что бы я был уверен что имя соответсвует логикик и огранияениям

    fun changeColor(newColor: ExistColor): Storage =
        copy(color = newColor)

    fun switchStatistic(bool: Boolean): Storage{
        return when(bool){
            true -> copy(isStatistics = false)
            false -> copy(isStatistics = true)
        }
    }
    fun switchArchive(bool: Boolean): Storage{
        return when(bool){
            true -> copy(isArchive = false)
            false -> copy(isArchive = true)
        }
    }
}

class NewStorage private constructor(
    override val name: String,
    override val userId: Long,
    override val currency: Currency,
    override val typeStorage: TypeStorage,
    override val note: String?,
    override val color: ExistColor,
    override val isStatistics: Boolean = true,
    override val isArchive: Boolean = false
): BaseStorage(){
    companion object{
        fun create(name: String, userId: Long, currency: Currency, typeStorage: TypeStorage, note: String?, color: ExistColor): StateDomain<NewStorage>{
            if(!isNameValid(name)) {
                return StateDomain.Error("❌Некорректное имя")
            }
            if(!isNoteValid(note)) return StateDomain.Error("❌Некорректная заметка")
            return StateDomain.Success(NewStorage(name, userId, currency, typeStorage, note, color, isStatistics = true, isArchive = false))
        }
    }
}
