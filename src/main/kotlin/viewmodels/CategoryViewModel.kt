package org.example.viewmodels

import org.example.model.domain.Category
import org.example.model.service.CategoryService
import org.example.views.category.CategoryUi
import org.example.views.storage.StorageListState
import java.lang.Exception

sealed class CategoryListState{
    object Empty: CategoryListState()
    data class Success(val category: List<CategoryUi>) : CategoryListState()
}
sealed class CategoryState{
    object Success: CategoryState()
    data class Error(val message: String): CategoryState()

}


class CategoryViewModel(private val service: CategoryService) {
    fun getCategoryByParent(parentId: Long?): CategoryListState{
        val list  = service.getCategoryByParent(parentId)
        if (list.isEmpty()) return CategoryListState.Empty
        val uiList = list.map {
            CategoryUi(it.id ?: throw NullPointerException("the <ID> returned is null"),
                it.name
            )
        }
        return CategoryListState.Success(uiList)
    }
    fun getBaseCategory(): CategoryListState{
        val list = service.getBaseCategory()
        if (list.isEmpty()) return CategoryListState.Empty

        val uiList = list.map{
            CategoryUi(
                id = it.id ?:throw NullPointerException("the <ID> returned is null"),
                name = it.name
            )
        }
        return CategoryListState.Success(uiList)
    }

    fun getCategory(categoryId: Long): Category{
        return service.getCategory(categoryId)
    }

    fun createCategory(
        name: String,
        currentCategory: Category?
    ): CategoryState{
        return try {
            service.createCategory(name, currentCategory)
            CategoryState.Success
        }
        catch (e: IllegalArgumentException){
            CategoryState.Error("Ошибка создания категории")
        }

    }

}