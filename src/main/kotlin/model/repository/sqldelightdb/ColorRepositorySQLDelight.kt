package org.example.model.repository.sqldelightdb

import com.example.ColorEntity
import com.example.ColorQueries
import org.example.model.domain.ExistColor
import org.example.model.domain.Owner
import org.example.model.domain.NewColor
import org.example.model.domain.SystemColor
import org.example.model.domain.UserColor
import org.example.model.repository.IColorRepository

class ColorRepositorySQLDelight(private val queries: ColorQueries): IColorRepository {
    override fun getByHex(hex: String): ExistColor? = queries.selectByHex(hex).executeAsOneOrNull()?.toDomain()
    override fun getById(id: Long): ExistColor? = queries.selectById(id).executeAsOneOrNull()?.toDomain()
    override fun getAll(): List<ExistColor> = queries.selectAll().executeAsList().map { it.toDomain() }
    override fun save(color: UserColor): UserColor? = queries.updateColor(color.owner.userId,color.hexCode, color.id).executeAsOneOrNull()?.toDomain() as? UserColor
    override fun save(color: NewColor): UserColor? = queries.insertColor(color.owner.userId, color.hexCode).executeAsOneOrNull()?.toDomain() as? UserColor
    override fun delete(color: UserColor): UserColor? = queries.deleteColor(color.id).executeAsOneOrNull()?.toDomain() as? UserColor

    override fun hasRelation(colorId: Long): Boolean {
        return if(queries.countStoragesByColor(colorId).executeAsOne() != 0L) true
        else if(queries.countCategoriesByColor(colorId).executeAsOne() != 0L) true
        else false
    }
    override fun replaceColorEverywhere(color: ExistColor, newColor: ExistColor): ExistColor? {
        try {
            queries.updateStorageColor(newColor.id, color.id)
            queries.updateCategoryColor(newColor.id, color.id)
            return newColor
        }
        catch (e: Exception){
            return null
        }
    }
}
fun ColorEntity.toDomain(): ExistColor{
    return if (user_id==null){
        SystemColor.create(
            id,
            hex_code,
        )
    } else{
        UserColor.create(
            id,
            hex_code,
            Owner.User(user_id)
        )
    }
}