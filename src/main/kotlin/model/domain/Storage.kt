package org.example.model.domain


data class Storage(
    val id: Long?,
    val name: String,
    val userId: Long,
    val currency: Currency,
    val typeStorage: TypeStorage,
    val note: String?,
    val color: Color.PersistedColor,
    val isStatistics: Boolean = true,
    val isArchive: Boolean = false
){
    fun rename(newName: String): Storage {
        require(newName.isNotBlank())
        return copy(name = newName)
    }

    fun changeUser(newUserId: Long): Storage =
        copy(userId = newUserId)

    fun changeCurrency(newCurrency: Currency): Storage {
        return copy(currency = newCurrency)
    }
    fun changeType(newType: TypeStorage): Storage =
        copy(typeStorage = newType)

    fun changeNote(newNote: String?): Storage =
        copy(note = newNote)

    fun changeColor(newColor: Color.PersistedColor): Storage =
        copy(color = newColor)

    fun enableStatistics(): Storage =
        copy(isStatistics = true)

    fun disableStatistics(): Storage =
        copy(isStatistics = false)

    fun archive(): Storage =
        copy(isArchive = true)

    fun unarchive(): Storage =
        copy(isArchive = false)

}


