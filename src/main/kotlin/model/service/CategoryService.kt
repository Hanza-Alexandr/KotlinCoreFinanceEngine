package org.example.model.service

import org.example.model.domain.Category
import org.example.model.domain.CategoryOwner
import org.example.model.domain.CategoryHierarchy
import org.example.model.domain.Color
import org.example.model.domain.NeedCategory
import org.example.model.domain.NewCategory
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
    fun createCategory(name: String, parentCategoryId: Int?, color: Color.ExistingColor, iconPath: String, need: NeedCategory): StateDomain<Category>{
        val structure =  if (parentCategoryId==null) CategoryHierarchy.Root else CategoryHierarchy.Child(parentCategoryId.toLong())
        val category = repo.save(NewCategory(
            name = name,
            color = color,
            owner = CategoryOwner.User(currentUserService.userId),
            icon = iconPath,
            structure = structure,
            need = need,
            isHidden = false,
        )) ?: return StateDomain.Error("❌Ошибка при создание категории ")
        return StateDomain.Success(category)
    }
    fun changeCategory(category: Category, newName: String?, newIcon: String?, newColor: Color.ExistingColor?, newNeed: NeedCategory?, newIsHide: Boolean?, newParent: CategoryHierarchy?): StateDomain<Category>{
        when(val returned = repo.save(Category(
            id = category.id,
            name = newName?: category.name,
            color = newColor?: category.color,
            icon = newIcon?: category.icon,
            need = newNeed?: category.need,
            isHidden = newIsHide ?: category.isHidden,
            owner = when(category.owner){
                is CategoryOwner.System -> {return StateDomain.Error("❌Ошибка. Нельзя менять системные категории")}
                is CategoryOwner.User -> {category.owner}
            },
            structure = when(newParent){
                /** Проверка не совпадает ли новая родительская категория своей же дочерней категорией */
                is CategoryHierarchy.Root -> {newParent}
                is CategoryHierarchy.Child -> {
                    if (getAllDescendants(category.id).contains(newParent.parentId)) {
                        return StateDomain.Error("❌Ошибка. Новая родительская категория это дочерняя категория данной категории")
                    }
                    else newParent
                }
                null -> category.structure
            }
        ))){
            null -> {
                return StateDomain.Error("❌Ошибка при изменении в БД")
            }
            is Category -> {
                return StateDomain.Success(returned)
            }
        }
    }
    fun changeSystemCategory(category: Category, newIsHide: Boolean): StateDomain<Category>{
       when(val returned =  repo.save(Category(
            id = category.id,
            name = category.name,
            color = category.color,
            icon = category.icon,
            need = category.need,
            isHidden = newIsHide,
            owner = category.owner,
            structure = category.structure
        ))){
           null -> {
               return StateDomain.Error("❌Ошибка при изменении isHide у системной категории в БД")
           }
           is Category -> {
               return StateDomain.Success(returned)
           }
       }
    }

    /** Auxiliary methods */

    /** Метод возвращающий id всей ветки дочерних категорий по id родителя*/
    private fun getAllDescendants(rootId: Long, visited: MutableSet<Long> = mutableSetOf()): List<Long> {

        // защита от циклов
        if (!visited.add(rootId)) return emptyList()

        val directChildren = repo.getChildrenByParent(rootId)
        val result = mutableListOf<Long>()

        for (child in directChildren) {
            result += child.id
            result += getAllDescendants( child.id, visited)
        }
        return result
    }
}