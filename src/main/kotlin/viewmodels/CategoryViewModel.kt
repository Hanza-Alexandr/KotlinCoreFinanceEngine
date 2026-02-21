package org.example.viewmodels

import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.service.CategoryService
import org.example.views.category.CategoryUi
import org.example.views.color.ColorView
import org.example.views.storage.StorageListState
import java.lang.Exception

sealed class CategoryListState{
    object Empty: CategoryListState()
    data class Success(val categories: List<Category>) : CategoryListState()
}
sealed class CategoryState{
    data class Success(val category: Category): CategoryState()
    data class Error(val message: String): CategoryState()

}
class CategoryViewModel(private val service: CategoryService) {

    fun getBaseCategories(): CategoryListState{
        return service.getBaseCategories()
    }

    fun getCategoriesByParent(parentId: Int): CategoryListState{
        return service.getCategoriesByParent(parentId)
    }
    fun getCategory(categoryId: Int): CategoryState{
        return service.getCategory(categoryId)
    }
    fun createCategory(name: String, parentCategoryId: Int?, iconPath: String, color: Color ): CategoryState{
        TODO()
    }

}