package org.example.model.service

import org.example.NeedCategory
import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.repository.ICategoryRepository
import org.example.viewmodels.CategoryListState
import org.example.viewmodels.CategoryState
import org.example.viewmodels.StateListColor
import java.lang.NullPointerException

class CategoryService(private val repo: ICategoryRepository, private val currentUserService: CurrentUserService) {
    fun getBaseCategories(): CategoryListState{
        val list = repo.getBaseCategories()
        return if (list.isEmpty()) CategoryListState.Empty
        else{
            CategoryListState.Success(list)
        }
    }
    fun getCategoriesByParent(parentCategoryId: Int): CategoryListState{
        val list = repo.getChildrenByParent(parentCategoryId.toLong())
        return if (list.isEmpty()) CategoryListState.Empty
        else{
            CategoryListState.Success(list)
        }
    }
    fun getCategory(categoryId: Int): CategoryState{
        val category = repo.getById(categoryId.toLong()) ?: return CategoryState.Error("❌ошибка получении категории")
        return CategoryState.Success(category)
    }


}