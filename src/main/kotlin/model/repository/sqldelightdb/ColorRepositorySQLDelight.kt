package org.example.model.repository.sqldelightdb

import com.example.ColorEntity
import com.example.ColorQueries
import org.example.model.domain.Color
import org.example.model.domain.User
import org.example.model.repository.IColorRepository

class ColorRepositorySQLDelight(private val queries: ColorQueries): IColorRepository {
    override fun getByHex(hex: String): Color.PersistedColor? = queries.selectByHex(hex).executeAsOneOrNull()?.toDomain()
    override fun getById(id: Long): Color.PersistedColor = queries.selectById(id).executeAsOne().toDomain()
    override fun getAll(): List<Color.PersistedColor> = queries.selectAll().executeAsList().map { it.toDomain() }


    override fun save(color: Color.UserColor): Color.UserColor {
        val update = queries.updateColor(
            user_id = color.userId,
            hex_code = color.hexCode,
            id = color.id
        ).executeAsOne()
        return Color.UserColor(
            id = update.id,
            userId = requireNotNull(update.user_id) { "user_id must not be null for UserColor" },
            hexCode = update.hex_code
        )
    }

    override fun save(color: Color.NewColor): Color.UserColor {
        val insert = queries.insertColor(
            user_id = color.userId,
            hex_code = color.hexCode
        ).executeAsOne()
        return Color.UserColor(
            id = insert.id,
            userId = requireNotNull(insert.user_id) { "user_id must not be null for UserColor" },
            hexCode = insert.hex_code
        )
    }

    override fun delete(color: Color.UserColor): Color.UserColor {
        val delete =  queries.deleteColor(color.id).executeAsOne()
        return Color.UserColor(
            id = delete.id,
            userId = requireNotNull(delete.user_id) { "user_id must not be null for UserColor" },
            hexCode = delete.hex_code
        )
    }

    override fun hasRelation(colorId: Long): Boolean {
        if(queries.countStoragesByColor(colorId).executeAsOne() != 0L) return true
        else if(queries.countCategoriesByColor(colorId).executeAsOne() != 0L) return true
        else return false
    }

    override fun replaceColorEverywhere(color: Color.UserColor, newColor: Color.PersistedColor) {
        queries.updateStorageColor(newColor.id, color.id)
        queries.updateCategoryColor(newColor.id, color.id)
    }

}
fun ColorEntity.toDomain(): Color.PersistedColor{
    return if (user_id==null){
        Color.SystemColor(
            id = id,
            hexCode = hex_code
        )
    } else{
        Color.UserColor(
            id = id,
            userId = user_id,
            hexCode = hex_code
        )
    }
}