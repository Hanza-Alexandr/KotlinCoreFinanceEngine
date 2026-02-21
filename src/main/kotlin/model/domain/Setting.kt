package org.example.model.domain

import kotlinx.serialization.Serializable

@Serializable
data class Setting(
    val userId: Long?,
    val theme: AppThem
)