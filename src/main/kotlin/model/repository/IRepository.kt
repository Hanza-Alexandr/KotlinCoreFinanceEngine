package org.example.model.repository

import org.example.model.domain.ExistColor
import org.example.model.domain.Category
import org.example.model.domain.NewCategory
import org.example.model.domain.NewColor
import org.example.model.domain.NewOperation
import org.example.model.domain.NewStorage
import org.example.model.domain.Operation
import org.example.model.domain.Setting
import org.example.model.domain.Storage
import org.example.model.domain.User
import org.example.model.domain.UserColor

interface IStorageRepository{
    fun getAll(): List<Storage>
    fun getById(id: Long): Storage?
    fun save(storage: Storage): Storage?
    fun save(storage: NewStorage): Storage?
    fun delete(storage: Storage): Storage?
}
interface IOperationRepository{
    fun getAll(): List<Operation>
    fun getOperationsByStorage(storageId: Long): List<Operation>
    fun getById(id: Long): Operation?
    fun save(newOperation: NewOperation): Operation?
    fun save(operation: Operation): Operation?
    fun delete(operation: Operation): Operation?
}
interface IColorRepository{
    fun getByHex(hex: String): ExistColor?
    fun getById(id: Long): ExistColor?
    fun getAll(): List<ExistColor>
    fun save(color: UserColor): UserColor?
    fun save(color: NewColor): UserColor?
    fun delete(color: UserColor): UserColor?

    fun hasRelation(colorId: Long): Boolean
    fun replaceColorEverywhere(color: ExistColor, newColor: ExistColor): ExistColor?
}
interface ICategoryRepository{
    fun getBaseCategories(): List<Category>
    fun getChildrenByParent(parentCategoryId: Long?): List<Category>
    fun getById(id: Long): Category?
    fun save(category: Category): Category?
    fun save(category: NewCategory): Category?
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
