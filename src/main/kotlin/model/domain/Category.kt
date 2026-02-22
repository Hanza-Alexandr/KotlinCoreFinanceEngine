package org.example.model.domain


data class Category(
    val id: Long?,
    val userId: Long?,
    val name: String,
    val color: Color.PersistedColor,
    val pathIcon: String,
    val parentCategoryId: Long?,
    val need: NeedCategory,
    val isHide: Boolean,
    val isSystem: Boolean
){
    fun rename(newName: String): Category {
        require(newName.isNotBlank())
        return copy(name = newName)
    }

    fun changeIcon(newPathIcon: String): Category =
        copy(pathIcon = newPathIcon)

    fun changeUser(newUserId: Long?): Category =
        copy(userId = newUserId)

    fun changeColor(newColor: Color.PersistedColor): Category =
        copy(color = newColor)

    fun changeParent(newParentId: Long): Category {
        require(newParentId != id)
        return copy(parentCategoryId = newParentId)
    }

    fun changeNeed(newNeed: NeedCategory): Category =
        copy(need = newNeed)

    fun hide(): Category =
        copy(isHide = true)

    fun show(): Category =
        copy(isHide = false)
}