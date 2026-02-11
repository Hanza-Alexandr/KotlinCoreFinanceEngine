package org.example.model.repository

import com.example.CategoryQueries
import org.example.model.Category

class CategoryRepositoryImp(private val queries: CategoryQueries): ICategoryRepository {
    override fun getAllByUser(userId: Long): List<Category> {
        TODO("Not yet implemented")
    }

    override fun getChildrenByParent(parentCategoryId: Long): List<Category> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Category {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Category> {
        TODO("Not yet implemented")
    }

    override fun save(category: Category) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}