package org.example.model.domain

import com.example.CategoryEntity
import com.example.CategorySelectById
import com.example.ColorEntity
import org.example.model.repository.sqldelightdb.toDomain


sealed class CategoryOwner {
    object System : CategoryOwner()
    data class User(val userId: Long) : CategoryOwner()
}

sealed class CategoryHierarchy {
    object Root : CategoryHierarchy()
    data class Child(val parentId: Long) : CategoryHierarchy()
}
data class Category(
    val id: Long,
    val name: String,
    val color: Color.ExistingColor,
    val icon: String,
    val need: NeedCategory,
    val isHidden: Boolean,
    val owner: CategoryOwner,
    val structure: CategoryHierarchy
)
data class NewCategory(
    val name: String,
    val color: Color.ExistingColor,
    val icon: String,
    val need: NeedCategory,
    val isHidden: Boolean,
    val owner: CategoryOwner.User,
    val structure: CategoryHierarchy
)


fun CategoryEntity.toDomain(color: Color.ExistingColor): Category{
    val owner = if(user_id == null) CategoryOwner.System else CategoryOwner.User(user_id)
    val structure = if (parent_category_id == null) CategoryHierarchy.Root else CategoryHierarchy.Child(parent_category_id)
    return Category(
        id = id,
        name = name,
        color = color,
        icon = path_icon,
        need = NeedCategory.valueOf(need),
        isHidden = is_hide==1L,
        owner = owner,
        structure = structure
    )
}
fun CategorySelectById.toDomain(): Category{
    val owner = if(category_user_id == null) CategoryOwner.System else CategoryOwner.User(category_user_id)
    val structure = if (parent_category_id == null) CategoryHierarchy.Root else CategoryHierarchy.Child(parent_category_id)
    return Category(
        id = category_id,
        name = category_name,
        color = ColorEntity(
            id = color_id!!,
            user_id = color_user_id,
            hex_code = color_hex_code!!
        ).toDomain(),
        icon = category_path_icon,
        need = NeedCategory.valueOf(category_need),
        isHidden = category_is_hide == 1L,
        owner = owner,
        structure = structure
    )
}
