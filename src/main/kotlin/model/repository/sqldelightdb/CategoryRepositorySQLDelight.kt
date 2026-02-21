package org.example.model.repository.sqldelightdb

import com.example.CategoryEntity
import com.example.CategoryQueries
import com.example.SelectById
import org.example.InputState
import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.domain.NeedCategory
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
                Color(
                    it.color_id,
                    it.color_user_id,
                    it.color_hex_code?:throw NullPointerException(),
                    it.color_user_id ==null
                )
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
                Color(
                    it.color_id,
                    it.color_user_id,
                    it.color_hex_code?:throw NullPointerException(),
                    it.color_user_id ==null
                )
            )
        }
    }

    override fun getById(id: Long): Category? {
        return try {
            queries.selectById(id).executeAsOne().toDomain()
        }
        catch (e: NullPointerException){
            return null
        }
    }

    override fun save(category: Category): Category? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Category? {
        TODO("Not yet implemented")
    }

    private fun CategoryEntity.toDomain(color: Color): Category =
        Category(
            id = id,
            userId = user_id,
            name = name,
            color = color,
            pathIcon = path_icon,
            parentCategoryId = parent_category_id,
            need = NeedCategory.valueOf(need),
            isHide = is_hide==1L,
            isSystem = user_id==null
        )

    private fun SelectById.toDomain(): Category =
        Category(
            id = category_id,
            userId = category_user_id,
            name = category_name,
            color =
                Color(
                    id = color_id,
                    userId = color_user_id,
                    hexCode = color_hex_code?:throw NullPointerException(),
                    isSystem = color_user_id ==null
                ),
            pathIcon = category_path_icon,
            parentCategoryId = parent_category_id,
            need = NeedCategory.valueOf(category_need),
            isHide = category_is_hide==1L,
            isSystem = category_user_id==null
        )

}