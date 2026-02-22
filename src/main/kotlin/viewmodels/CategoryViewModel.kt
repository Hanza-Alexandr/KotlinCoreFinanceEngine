package org.example.viewmodels

import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.domain.NeedCategory
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.service.CategoryService

class CategoryViewModel(private val service: CategoryService) {

    fun getBaseCategories(): StateDomainList<Category>{
        return service.getBaseCategories()
    }
    fun getCategoriesByParent(parentId: Int): StateDomainList<Category>{
        return service.getCategoriesByParent(parentId)
    }
    fun getCategory(categoryId: Int): StateDomain<Category>{
        return service.getCategory(categoryId)
    }
    fun createCategory(name: String, parentCategoryId: Int?, color: Color.PersistedColor, iconPath: String,  need: NeedCategory): StateDomain<Category>{
        return service.createCategory(name,parentCategoryId,color,iconPath,need)
    }
}