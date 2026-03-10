package org.example.viewmodels

import org.example.model.domain.ExistColor
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.domain.UserColor
import org.example.model.service.ColorService





class ColorViewModel(private val colorService: ColorService) {

    /**
     * CRUD
     **/
    fun createColor(hexCode: String): StateDomain<UserColor>{
        return colorService.createColor(hexCode)
    }
    fun getColors(): StateDomainList<ExistColor>{
        return colorService.getColors()
    }
    fun getColor(colorId: Int): StateDomain<ExistColor>{
        return colorService.getColor(colorId)
    }
    fun updateColor(oldColor: UserColor, newHexCode: String): StateDomain<UserColor>{
        return colorService.updateColor(oldColor,newHexCode)
    }
    /**
     * 3 варианта:
     * Если связей нет - просто удалить из таблицы. Метод с id
     * Если связи есть:
     *      Заменить на стоковый цвет. Метод с Id, но newColor пустой
     *      Заменить на выбранный цвет. Метод с Id, но newColor заполнен
     */
    fun deleteColor(color: UserColor): StateDomain<UserColor>{
        return colorService.deleteColor(color)
    }
    fun deleteColor(color: UserColor, newColor: ExistColor): StateDomain<UserColor>{
        return colorService.deleteColor(color,newColor)
    }
    /**
     * Other
     */
    fun hasRelations(color: UserColor): Boolean{
        return colorService.hasRelations(color)
    }

}