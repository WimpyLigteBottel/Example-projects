package com.example.demo.model


data class Book(val id: String = "", val name: String = "", val pageCount: Int = 0, val authorId: String = "")
data class Author(val id: String = "", val firstName: String = "", val lastName: String = "")