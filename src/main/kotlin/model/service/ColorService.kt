package org.example.model.service

import org.example.STANDARD_COLOR_HEX
import org.example.model.domain.Color
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.repository.IColorRepository
import java.lang.NullPointerException

class ColorService(private val repo: IColorRepository, private val userService: CurrentUserService) {
    fun createColor(hexCode: String): StateDomain<Color.UserColor> {
        if (hexCode.toCharArray().size != 6) return StateDomain.Error("Неправильная длина hex кода. <Должно быть 6 символов>")
        if(repo.getByHex(hexCode)!=null) return StateDomain.Error("Не может быть двух одинаковых цветов")
        try {
            val newColor =  repo.save(
                Color.NewColor(
                    userId = userService.userId,
                    hexCode = hexCode,
                )
            )
            return StateDomain.Success(newColor)
        }
        catch (e: Exception){
            return StateDomain.Error("Ошибка создания нового цвета ${e.message}")
        }
    }
    fun getColors(): StateDomainList<Color.PersistedColor> {
        val listColor = repo.getAll()
        return if (listColor.isEmpty()) StateDomainList.Empty

        else StateDomainList.Success(listColor)
    }
    fun getColor(colorId: Int): StateDomain<Color.PersistedColor>{
        try {
            val color = repo.getById(colorId.toLong())
            return StateDomain.Success(color)
        }
        catch (e: Exception){
            return StateDomain.Error("Ошибка получения цвета ${e.message}")
        }
    }
    fun updateColor(oldColor: Color.UserColor, newHexCode: String): StateDomain<Color.UserColor>{
        try {
            val newColor = Color.UserColor(
                id = oldColor.id,
                userId = oldColor.userId,
                hexCode = newHexCode,
            )
            return StateDomain.Success(repo.save(newColor))
        }
        catch (e: Exception){
            return StateDomain.Error("Ошибка обновления цвета ${e.message}")
        }

    }
    /**
     * 3 варианта:
     * Если связей нет - просто удалить из таблицы. Метод с id
     * Если связи есть:
     *      Заменить на стоковый цвет. Метод с Id, но newColor пустой
     *      Заменить на выбранный цвет. Метод с Id, но newColor заполнен
     */
    /**
     * Простое удаление если связанных данных нет
     */
    fun deleteColor(color: Color.UserColor): StateDomain<Color.UserColor>{
        try {
            val delete = repo.delete(color)
            return StateDomain.Success(delete)
        }
        catch (e: Exception){
            return StateDomain.Error( "Ошибка простого удаления ${e.message}")
        }
    }
    /**
     * Сложное удаление с сохранением данных. newColor это замена во всех записях цвета который будет удален
     */
    fun deleteColor(color: Color.UserColor, newColor: Color.PersistedColor): StateDomain<Color.UserColor>{
        try {
            repo.replaceColorEverywhere(color,newColor)
            val delete = repo.delete(color)
            return StateDomain.Success(delete)
        }
        catch (e: Exception){
            return StateDomain.Error("Ошибка при удалении с заменой на выбранный цвет ${e.message}")
        }

    }
    fun hasRelations(color: Color.PersistedColor): Boolean{
        return repo.hasRelation(color.id)
    }
}