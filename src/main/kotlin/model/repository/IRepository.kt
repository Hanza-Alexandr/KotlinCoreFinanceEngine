package org.example.model.repository

import org.example.model.Category
import org.example.model.Color
import org.example.model.Operation
import org.example.model.Setting
import org.example.model.Storage
import org.example.model.User

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
    fun getById(id: Long): Color
    fun getAll(): List<Color>
    fun save(color: Color)
    fun delete(id: Long)
}

interface ICategoryRepository{
    fun getAllByUser(userId: Long): List<Category>
    fun getChildrenByParent(parentCategoryId: Long): List<Category>
    fun getById(id: Long): Category
    fun getAll(): List<Category>
    fun save(category: Category)
    fun delete(id: Long)
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
