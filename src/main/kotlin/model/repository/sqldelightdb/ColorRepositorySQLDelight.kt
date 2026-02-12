package org.example.model.repository.sqldelightdb

import org.example.model.domain.Color
import org.example.model.repository.IColorRepository

class ColorRepositorySQLDelight: IColorRepository {
    override fun getById(id: Long): Color {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Color> {
        TODO("Not yet implemented")
    }

    override fun save(color: Color) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}