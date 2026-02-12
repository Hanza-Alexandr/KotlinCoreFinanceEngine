package org.example.model.domain

data class Category(
    val id: Long?,
    val pathIcon: String,
    val userId: Long?,
    val color: Color,
    val name: String,
    val parentCategoryId: Long,
    val need: String,
    val isHide: Boolean
){
    fun rename(newName: String): Category {
        require(newName.isNotBlank())
        return copy(name = newName)
    }

    fun changeIcon(newPathIcon: String): Category =
        copy(pathIcon = newPathIcon)

    fun changeUser(newUserId: Long?): Category =
        copy(userId = newUserId)

    fun changeColor(newColor: Color): Category =
        copy(color = newColor)

    fun changeParent(newParentId: Long): Category {
        require(newParentId != id)
        return copy(parentCategoryId = newParentId)
    }

    fun changeNeed(newNeed: String): Category =
        copy(need = newNeed)

    fun hide(): Category =
        copy(isHide = true)

    fun show(): Category =
        copy(isHide = false)
}