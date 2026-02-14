package org.example.views.category


sealed class CategoryListState{
    object Empty: CategoryListState()
    data class Success(val category: List<CategoryUi>) : CategoryListState()
}
sealed class CategoryState{
    object Success: CategoryState()
    data class Error(val message: String): CategoryState()

}
