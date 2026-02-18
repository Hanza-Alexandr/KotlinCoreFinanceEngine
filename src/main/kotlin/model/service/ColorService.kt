package org.example.model.service

import org.example.STANDARD_COLOR_HEX
import org.example.model.domain.Color
import org.example.model.repository.IColorRepository
import org.example.viewmodels.StateColor
import org.example.viewmodels.StateListColor
import java.lang.NullPointerException

class ColorService(private val repo: IColorRepository, private val userService: CurrentUserService) {
    fun createColor(hexCode: String): StateColor {
        if (hexCode.toCharArray().size != 6) return StateColor.Error("Неправильная длина hex кода. <Должно быть 6 символов>")
        if(repo.getByHex(hexCode)!=null) return StateColor.Error("Не может быть двух одинаковых цветов")
        try {
            val newColor =  repo.save(
                Color(
                    id = null,
                    userId = userService.userId,
                    hexCode = hexCode,
                    isSystem = false
                )
            )
            return StateColor.Success(newColor)
        }
        catch (e: Exception){
            return StateColor.Error("Ошибка создания нового цвета ${e.message}")
        }
    }
    fun getColors(): StateListColor {
        val listColor = repo.getAll()
        return if (listColor.isEmpty()) StateListColor.Empty
        else StateListColor.Success(listColor)
    }
    fun getColor(colorId: Int): StateColor{
        try {
            val color = repo.getById(colorId.toLong())
            return StateColor.Success(color)
        }
        catch (e: Exception){
            return StateColor.Error("Ошибка получения цвета ${e.message}")
        }
    }
    fun updateColor(oldColor: Color, newHexCode: String): StateColor{
        try {
            val newColor = Color(
                id = oldColor.id,
                userId = oldColor.userId,
                hexCode = newHexCode,
                isSystem = oldColor.isSystem
            )
            return StateColor.Success(repo.save(newColor))
        }
        catch (e: Exception){
            return StateColor.Error("Ошибка обновления цвета ${e.message}")
        }

    }
    /**
     * 3 варианта:
     * Если связей нет - просто удалить из таблицы. Метод с id
     * Если связи есть:
     *      Заменить на стоковый цвет. Метод с Id, но newColor пустой
     *      Заменить на выбранный цвет. Метод с Id, но newColor заполнен
     */
    fun deleteColor(color: Color): StateColor{
        try {
            val id = color.id ?: return StateColor.Error("Отсутствует ID")
            repo.delete(id)
            return StateColor.Success(color)
        }
        catch (e: Exception){
            return StateColor.Error( "Ошибка простого удаления ${e.message}")
        }
    }
    fun deleteColor(color: Color, newColor: Color?): StateColor{
        if (newColor==null){
            try {
                val oldId = color.id ?: return StateColor.Error("У изменяемого цвета отсутствует ID")
                val standardColor = repo.getByHex(STANDARD_COLOR_HEX) ?: return StateColor.Error("Не удалось найти стандартынй цвет")
                val newId = standardColor.id ?: return StateColor.Error("У стандартного цвета отсутствует Id")
                repo.replaceColorEverywhere(oldId,newId)
                repo.delete(oldId)
                return StateColor.Success(standardColor)
            }
            catch (e: Exception){
                return StateColor.Error("Ошибка при удалении с заменой на стандартный цвет ${e.message}")
            }
        }
        else{
            try {
                val oldId = color.id ?: return StateColor.Error("У изменяемого цвета отсутствует ID")
                val newId = newColor.id ?: return StateColor.Error("У выбранного цвета отсутствует Id")
                repo.replaceColorEverywhere(oldId,newId)
                repo.delete(oldId)
                return StateColor.Success(newColor)
            }
            catch (e: Exception){
                return StateColor.Error("Ошибка при удалении с заменой на выбранный цвет ${e.message}")
            }
        }
    }
    fun hasRelations(color: Color): Boolean{
        val id = color.id ?: throw NullPointerException("Ошибка при запросе на наличие связанных элементов. У выбранного цвета отсутствует ID ")
        return repo.hasRelation(id)
    }
}