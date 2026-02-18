package org.example.viewmodels

import org.example.model.domain.Color
import org.example.model.service.ColorService
import org.example.views.category.CategoryUi

sealed class StateColor{
    data class Success(val color: Color): StateColor()
    data class Error(val message: String): StateColor()
}

sealed class StateListColor{
    data class Success(val colors: List<Color>): StateListColor()
    object Empty: StateListColor()

}


class ColorViewModel(private val colorService: ColorService) {

    /**
     * CRUD
     **/
    fun createColor(hexCode: String): StateColor{
        return colorService.createColor(hexCode)
    }
    fun getColors(): StateListColor{
        return colorService.getColors()
    }
    fun getColor(colorId: Int): StateColor{
        return colorService.getColor(colorId)
    }
    fun updateColor(oldColor: Color, newHexCode: String): StateColor{
        return colorService.updateColor(oldColor,newHexCode)
    }
    /**
     * 3 варианта:
     * Если связей нет - просто удалить из таблицы. Метод с id
     * Если связи есть:
     *      Заменить на стоковый цвет. Метод с Id, но newColor пустой
     *      Заменить на выбранный цвет. Метод с Id, но newColor заполнен
     */
    fun deleteColor(color: Color): StateColor{
        return colorService.deleteColor(color)
    }
    fun deleteColor(color: Color, newColor: Color?): StateColor{
        return colorService.deleteColor(color,newColor)
    }
    /**
     * Other
     */
    fun hasRelations(color: Color): Boolean{
        return colorService.hasRelations(color)
    }

}