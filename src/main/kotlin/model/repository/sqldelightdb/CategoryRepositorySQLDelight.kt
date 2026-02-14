package org.example.model.repository.sqldelightdb

import com.example.CategoryQueries
import org.example.NeedCategory
import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.repository.ICategoryRepository

class CategoryRepositorySQLDelight(private val queries: CategoryQueries): ICategoryRepository {

    override fun getBaseCategories(userId: Long): List<Category> {
        try {
           return queries.selectBaseCategories(userId){
                    category_id,
                    path_icon,
                    category_user_id,
                    color_id,
                    name,
                    parent_category,
                    need,
                    is_hide,
                    color_user_id,
                    hex_code ->
                Category(
                    id = category_id,
                    pathIcon = path_icon,
                    userId = category_user_id,
                    color = Color(
                        id = color_id,
                        userId = color_user_id,
                        hexCode = hex_code!!
                    ),
                    name = name,
                    parentCategoryId = parent_category,
                    need = NeedCategory.valueOf(need),
                    isHide = is_hide==1L
                )
            }.executeAsList()
        }
        catch (e: NullPointerException){
            println("Где то что то вернуло Нулл. Хотя не должно")
            return listOf()

        }
    }

    override fun getChildrenByParent(parentCategoryId: Long?, userId: Long): List<Category> {
        try {
            return queries.selectChildrenCategories(parentCategoryId,userId){
                category_id,
                category_path_icon,
                category_user_id,
                color_id,
                color_hex_code,
                parent_category_id,
                category_name,
                category_need,
                category_isHide ->
                Category(
                    id = category_id,
                    pathIcon = category_path_icon,
                    userId = category_user_id,
                    color = Color(
                        id = color_id,
                        userId = category_user_id,
                        hexCode = color_hex_code!!
                    ),
                    name = category_name,
                    parentCategoryId = parent_category_id,
                    need = NeedCategory.valueOf(category_need),
                    isHide = category_isHide==1L)
            }.executeAsList()
        }
        catch (e: NullPointerException){
            println("Где то что то вернуло Нулл. Хотя не должно")
            return listOf()

        }
    }

    override fun getById(id: Long): Category? {
        try {
            return queries.selectCategoryById(id){
                    category_id,
                    category_path_icon,
                    category_user_id,
                    color_id,
                    color_hex_code,
                    parent_category_id,
                    category_name,
                    category_need,
                    category_isHide ->
                Category(
                    id = category_id,
                    pathIcon = category_path_icon,
                    userId = category_user_id,
                    color = Color(
                        id = color_id,
                        userId = category_user_id,
                        hexCode = color_hex_code!!
                    ),
                    name = category_name,
                    parentCategoryId = parent_category_id,
                    need = NeedCategory.valueOf(category_need),
                    isHide = category_isHide==1L
                )
            }.executeAsOneOrNull()
        } catch (e: NullPointerException){
            println("Где то что то вернуло Нулл. Хотя не должно")
            return null
        }

    }

    override fun save(category: Category) {
        when(category.id){
            null -> queries.insertNewCategory(
                id = null,
                path_icon = category.pathIcon,
                user_id = category.userId,
                color_id = category.color.id ?: throw NullPointerException("asd"),
                name = category.name,
                parent_category_id =category.parentCategoryId,
                need = category.need.toString(),
                is_hide = if(category.isHide)1L else 0
            )
            else -> {
                TODO()
            }
        }
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }


}