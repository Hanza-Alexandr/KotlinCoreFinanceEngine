package org.example.model.repository

import org.example.model.domain.Category
import org.example.model.domain.Color
import org.example.model.domain.Operation
import org.example.model.domain.Setting
import org.example.model.domain.Storage
import org.example.model.domain.User

interface IStorageRepository{
    fun getById(id: Long): Storage
    fun getAll(): List<Storage>
    fun save(storage: Storage)
    fun delete(id: Long)
}
interface IOperationRepository{
    fun getAllByStorage(storageId: Long): List<Operation>
    fun getById(id: Long): Operation
    fun getAll(): List<Operation>
    fun save(operation: Operation)
    fun delete(id: Long)
}
interface IColorRepository{
    fun getByHex(hex: String): Color.PersistedColor?
    fun getById(id: Long): Color.PersistedColor
    fun getAll(): List<Color.PersistedColor>
    fun save(color: Color.UserColor): Color.UserColor
    fun save(color: Color.NewColor): Color.UserColor
    fun delete(color: Color.UserColor): Color.UserColor

    fun hasRelation(colorId: Long): Boolean
    fun replaceColorEverywhere(color: Color.UserColor, newColor: Color.PersistedColor)
}
interface ICategoryRepository{
    fun getBaseCategories(): List<Category>
    fun getChildrenByParent(parentCategoryId: Long?): List<Category>
    fun getById(id: Long): Category?
    fun save(category: Category): Category?
    fun delete(id: Long): Category?
}
interface ISettingRepository{
    fun save(setting: Setting)
    fun load(): Setting
    fun removeAll()
}
interface ICurrentUserRepository {
    fun getCurrentUser(): User?
    fun setCurrentUser(user: User)
}
