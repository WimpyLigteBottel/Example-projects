package com.example.demo.author

data class Author(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val bookIds: List<String> = emptyList()
)