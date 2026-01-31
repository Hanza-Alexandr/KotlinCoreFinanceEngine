package org.example

data class Category(
    val title: String,
    val parentCategory: Category?
)