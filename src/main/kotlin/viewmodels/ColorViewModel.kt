package org.example.viewmodels

import org.example.model.domain.Color
import org.example.model.domain.StateDomain
import org.example.model.domain.StateDomainList
import org.example.model.service.ColorService





class ColorViewModel(private val colorService: ColorService) {

    /**
     * CRUD
     **/
    fun createColor(hexCode: String): StateDomain<Color.UserColor>{
        return colorService.createColor(hexCode)
    }
    fun getColors(): StateDomainList<Color.ExistingColor>{
        return colorService.getColors()
    }
    fun getColor(colorId: Int): StateDomain<Color.ExistingColor>{
        return colorService.getColor(colorId)
    }
    fun updateColor(oldColor: Color.UserColor, newHexCode: String): StateDomain<Color.UserColor>{
        return colorService.updateColor(oldColor,newHexCode)
    }
    /**
     * 3 варианта:
     * Если связей нет - просто удалить из таблицы. Метод с id
     * Если связи есть:
     *      Заменить на стоковый цвет. Метод с Id, но newColor пустой
     *      Заменить на выбранный цвет. Метод с Id, но newColor заполнен
     */
    fun deleteColor(color: Color.UserColor): StateDomain<Color.UserColor>{
        return colorService.deleteColor(color)
    }
    fun deleteColor(color: Color.UserColor, newColor: Color.ExistingColor): StateDomain<Color.UserColor>{
        return colorService.deleteColor(color,newColor)
    }
    /**
     * Other
     */
    fun hasRelations(color: Color.ExistingColor): Boolean{
        return colorService.hasRelations(color)
    }

}