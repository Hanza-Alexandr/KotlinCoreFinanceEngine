package org.example.model.repository.sqldelightdb

import com.example.ColorEntity
import com.example.ColorQueries
import org.example.model.domain.Color
import org.example.model.repository.IColorRepository

class ColorRepositorySQLDelight(private val queries: ColorQueries): IColorRepository {
    override fun getByHex(hex: String): Color? = queries.selectByHex(hex).executeAsOneOrNull()?.toDomain()
    override fun getById(id: Long): Color = queries.selectById(id).executeAsOne().toDomain()
    override fun getAll(): List<Color> = queries.selectAll().executeAsList().map { it.toDomain() }


    override fun save(color: Color): Color {

        if(color.id ==null){
            val insert = queries.insertColor(
                user_id = color.userId,
                hex_code = color.hexCode
            ).executeAsOne()
            return insert.toDomain()
        }
        else{
            val update = queries.updateColor(
                user_id = color.userId,
                hex_code = color.hexCode,
                id = color.id
            ).executeAsOne()
            return update.toDomain()
        }
    }

    override fun delete(id: Long) {
        queries.deleteColor(id)
    }

    override fun hasRelation(colorId: Long): Boolean {
        if(queries.countStoragesByColor(colorId).executeAsOne() != 0L) return true
        else if(queries.countCategoriesByColor(colorId).executeAsOne() != 0L) return true
        else return false
    }

    override fun replaceColorEverywhere(oldColorId: Long, newColorId: Long) {
        queries.updateStorageColor(newColorId, oldColorId)
        queries.updateCategoryColor(newColorId, oldColorId)
    }
    private fun ColorEntity.toDomain(): Color =
        Color(
            id = id,
            userId = user_id,
            hexCode = hex_code,
            isSystem = user_id == null // системный цвет = user_id IS NULL
        )

}