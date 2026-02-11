package org.example.model.service

import org.example.model.Setting
import org.example.model.repository.ISettingRepository

class SettingService(private val repo: ISettingRepository) {
    fun load(): Setting {
        return repo.load()
    }
    fun update(transform: (Setting) -> Setting) {
        val current = repo.load()
        val updated = transform(current)
        repo.save(updated)
    }
    fun removeSetting(){
        repo.removeAll()
    }
}