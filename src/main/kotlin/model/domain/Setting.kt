package org.example.model.domain

import kotlinx.serialization.Serializable
import org.example.AppThem

@Serializable
data class Setting(
    val userId: Long?,
    val theme: AppThem
)