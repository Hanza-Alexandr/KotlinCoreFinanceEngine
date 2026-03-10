package org.example.model.service

import org.example.model.domain.ExistColor
import org.example.model.domain.ColorOwner
import org.example.model.domain.NewColor
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.UserColor
import org.example.model.repository.IColorRepository

class ColorService(private val repo: IColorRepository, private val userService: CurrentUserService) {
    fun createColor(hexCode: String): StateDomain<UserColor> {
        if (repo.getByHex(hexCode)!=null) return StateDomain.Error("Не может быть двух одинаковых цветов")
        val newColor = NewColor.create(hexCode, ColorOwner.User(userService.userId))
        when (newColor){
            is StateDomain.Error-> return StateDomain.Error(newColor.message)
            is StateDomain.Success-> {
                val create =  repo.save(newColor.domain)
                return if (create==null) StateDomain.Error("❌Ошибка при создании")
                else StateDomain.Success(create)
            }
        }
    }
    fun getColors(): StateDomainList<ExistColor> {
        val colors = repo.getAll()
        return if (colors.isEmpty()) StateDomainList.Empty
        else StateDomainList.Success(colors)
    }
    fun getColor(colorId: Int): StateDomain<ExistColor>{
        val color = repo.getById(colorId.toLong())
        return if(color==null) StateDomain.Error("❌Ошибка при получении цвета")
        else StateDomain.Success(color)
    }
    fun updateColor(oldColor: UserColor, newHexCode: String): StateDomain<UserColor>{
        when(val upColor = oldColor.hexChange(newHexCode)){
            is StateDomain.Error-> return StateDomain.Error(upColor.message)
            is StateDomain.Success -> {
                val update = repo.save(upColor.domain)
                return if(update == null) StateDomain.Error("❌Ошибка при обновлении цвета")
                else StateDomain.Success(update)
            }
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
    fun deleteColor(color: UserColor): StateDomain<UserColor>{
        val delete = repo.delete(color)
        return if (delete == null) StateDomain.Error("❌Ошибка при удалении")
        else StateDomain.Success(delete)
    }
    /**
     * Сложное удаление с сохранением данных. newColor это замена во всех записях цвета который будет удален
     */
    fun deleteColor(color: UserColor, newColor: ExistColor): StateDomain<UserColor>{
        repo.replaceColorEverywhere(color, newColor) ?: return StateDomain.Error("❌Ошибка замены цвета")
        val delete = repo.delete(color) ?: return StateDomain.Error("❌Ошибка удаления цвета")
        return StateDomain.Success(delete)
    }
    fun hasRelations(color: UserColor): Boolean{
        return repo.hasRelation(color.id)
    }
}