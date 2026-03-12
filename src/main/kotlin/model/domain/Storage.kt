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
    fun rename(newName: String): Storage {
        require(newName.isNotBlank())
        return copy(name = newName)
    }

    fun changeType(newType: TypeStorage): Storage =
        copy(typeStorage = newType) //TODO проверка на корректность

    fun changeNote(newNote: String?): Storage =
        copy(note = newNote)//TODO проверка на корректность

    fun changeColor(newColor: ExistColor): Storage =
        copy(color = newColor)//TODO проверка на корректность

    fun enableStatistics(): Storage =
        copy(isStatistics = true)//TODO проверка на корректность

    fun disableStatistics(): Storage =
        copy(isStatistics = false)//TODO проверка на корректность

    fun archive(): Storage =
        copy(isArchive = true)//TODO проверка на корректность

    fun unarchive(): Storage =
        copy(isArchive = false)//TODO проверка на корректность

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
