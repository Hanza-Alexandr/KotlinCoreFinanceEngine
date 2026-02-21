package org.example.model.repository.infile

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.model.domain.AppThem
import org.example.model.domain.Setting
import org.example.model.repository.ISettingRepository
import java.io.File

class SettingsRepositoryInFile(
    private val file: File,
    private val json: Json = Json { prettyPrint = true }
) : ISettingRepository {

    override fun load(): Setting {
        if (!file.exists()) {
            val default = defaultSettings()
            save(default)
            return default
        }
        return json.decodeFromString<Setting>(file.readText())
    }

    override fun save(setting: Setting) {
        file.writeText(json.encodeToString(setting))
    }

    override fun removeAll() {
        file.delete()
    }

    private fun defaultSettings() = Setting(
        userId = null,
        theme = AppThem.SYSTEM
    )
}