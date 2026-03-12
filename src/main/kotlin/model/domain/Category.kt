package org.example.model.domain

import com.example.CategoryEntity
import com.example.CategorySelectById
import com.example.ColorEntity
import org.example.model.repository.sqldelightdb.toDomain


sealed class Owner {
    object System : Owner()
    data class User(val userId: Long) : Owner()
}

sealed class CategoryStructure {
    object Root : CategoryStructure()
    data class Child(val parentId: Long) : CategoryStructure()
}
data class Category(
    val id: Long,
    val name: String,
    val color: ExistColor,
    val icon: String,
    val need: NeedCategory,
    val isHidden: Boolean,
    val owner: Owner,
    val structure: CategoryStructure
)
data class NewCategory(
    val name: String,
    val color: ExistColor,
    val icon: String,
    val need: NeedCategory,
    val isHidden: Boolean,
    val owner: Owner.User,
    val structure: CategoryStructure
)


fun CategoryEntity.toDomain(color: ExistColor): Category{
    val owner = if(user_id == null) Owner.System else Owner.User(user_id)
    val structure = if (parent_category_id == null) CategoryStructure.Root else CategoryStructure.Child(parent_category_id)
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
    val owner = if(category_user_id == null) Owner.System else Owner.User(category_user_id)
    val structure = if (parent_category_id == null) CategoryStructure.Root else CategoryStructure.Child(parent_category_id)
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
