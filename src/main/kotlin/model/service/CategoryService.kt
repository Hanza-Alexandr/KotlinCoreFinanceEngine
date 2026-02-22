package org.example.model.service

import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.domain.NeedCategory
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.repository.ICategoryRepository

class CategoryService(private val repo: ICategoryRepository, private val currentUserService: CurrentUserService) {
    fun getBaseCategories(): StateDomainList<Category>{
        val list = repo.getBaseCategories()
        return if (list.isEmpty()) StateDomainList.Empty
        else{
            StateDomainList.Success(list)
        }
    }
    fun getCategoriesByParent(parentCategoryId: Int): StateDomainList<Category>{
        val list = repo.getChildrenByParent(parentCategoryId.toLong())
        return if (list.isEmpty()) StateDomainList.Empty
        else{
            StateDomainList.Success(list)
        }
    }
    fun getCategory(categoryId: Int): StateDomain<Category>{
        val category = repo.getById(categoryId.toLong()) ?: return StateDomain.Error("❌ошибка получении категории")
        return StateDomain.Success(category)
    }

    fun createCategory(name: String, parentCategoryId: Int?,  color: Color.PersistedColor, iconPath: String, need: NeedCategory): StateDomain<Category>{
        val category = repo.save(Category(
            id = null,
            userId = currentUserService.userId,
            name = name,
            color = color,
            pathIcon = iconPath,
            parentCategoryId = parentCategoryId?.toLong(),
            need = need,
            isHide = false,
            isSystem = false
        )) ?: return StateDomain.Error("❌Ошибка при создание категории ")
        return StateDomain.Success(category)
    }
}