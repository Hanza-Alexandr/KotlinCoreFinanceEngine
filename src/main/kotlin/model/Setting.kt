package org.example.model

import kotlinx.serialization.Serializable
import org.example.AppThem

@Serializable
data class Setting(
    val userId: Long?,
    val theme: AppThem
)