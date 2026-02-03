package org.example

data class Category(
    val id: Long?,
    val title: String,
    val parentCategory: Category?
)