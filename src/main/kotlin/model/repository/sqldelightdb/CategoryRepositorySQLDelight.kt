package org.example.model.repository.sqldelightdb

import com.example.CategoryEntity
import com.example.CategoryQueries
import com.example.ColorEntity
import org.example.model.domain.Category
import org.example.model.domain.CategoryOwner
import org.example.model.domain.CategoryHierarchy
import org.example.model.domain.NewCategory
import org.example.model.domain.toDomain
import org.example.model.repository.ICategoryRepository

class CategoryRepositorySQLDelight(private val queries: CategoryQueries): ICategoryRepository {

    override fun getBaseCategories(): List<Category> {
        return queries.selectBaseCategories().executeAsList().map {
            CategoryEntity(
                id = it.category_id,
                path_icon = it.category_path_icon,
                user_id = it.category_user_id,
                color_id = it.color_id,
                name = it.category_name,
                parent_category_id =it.parent_category_id,
                need = it.category_need,
                is_hide = it.category_isHide
            ).toDomain(
                ColorEntity(
                    it.color_id,
                    it.color_user_id,
                    it.color_hex_code?:throw NullPointerException(),
                ).toDomain()
            )
        }
    }

    override fun getChildrenByParent(parentCategoryId: Long?): List<Category> {
        return queries.selectChildrenCategories(parentCategoryId).executeAsList().map {
            CategoryEntity(
                id = it.category_id,
                path_icon = it.category_path_icon,
                user_id = it.category_user_id,
                color_id = it.color_id,
                name = it.category_name,
                parent_category_id =it.parent_category_id,
                need = it.category_need,
                is_hide = it.category_isHide
            ).toDomain(
                ColorEntity(
                    it.color_id,
                    it.color_user_id,
                    it.color_hex_code?:throw NullPointerException()
                    ).toDomain()
            )
        }
    }

    override fun getById(id: Long): Category? {
        return try {
            queries.categorySelectById(id).executeAsOne().toDomain()
        }
        catch (e: NullPointerException){
            return null
        }
    }

    override fun save(category: NewCategory): Category? {
        val newId = queries.insertNewCategory(
            id = null,
            path_icon = category.icon,
            user_id = when(val owner = category.owner){
                CategoryOwner.System -> {null}
                is CategoryOwner.User -> {owner.userId}
            },
            color_id = category.color.id,
            name = category.name,
            parent_category_id = when(val structure = category.structure){
                CategoryHierarchy.Root ->{null}
                is CategoryHierarchy.Child -> {structure.parentId}
            },
            need = category.need.toString(),
            is_hide = if (category.isHidden)1L else 0
        ).executeAsOne().id
        return getById(newId)
    }
    override fun save(category: Category): Category? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Category? {
        TODO("Not yet implemented")
    }

}