package org.example.model.service

import org.example.NeedCategory
import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.repository.ICategoryRepository
import java.lang.NullPointerException

class CategoryService(private val repo: ICategoryRepository, private val currentUserService: CurrentUserService) {
    fun getBaseCategory(): List<Category>{
        return repo.getBaseCategories(currentUserService.userId ?: throw NullPointerException("the <ID> returned is null"))
    }
    fun getCategoryByParent(id: Long?): List<Category>{
        return repo.getChildrenByParent(id, currentUserService.userId?: throw NullPointerException("the <ID> returned is null"))
    }
    fun createCategory(name: String, currentCategory: Category?, need: NeedCategory = NeedCategory.OPTIONAL, isHide: Boolean = false){
        repo.save(Category(
                id = null,
                pathIcon = "NONOONONasdasd",//пока так
                userId = currentUserService.userId,
                color = Color(
                    id = 2L,//пока так
                    userId = null,//пока так
                    hexCode = "#FF0000"//ПОка так
                ),
                name = name,
                parentCategoryId = currentCategory?.id,
                need = need,
                isHide = isHide
            )
        )
    }
    fun getCategory(categoryId: Long): Category{
        return repo.getById(categoryId) ?: throw NullPointerException("the <ID> returned is null")
    }
    fun getCategoryOrNull(categoryId: Long): Category?{
        return repo.getById(categoryId)
    }

}