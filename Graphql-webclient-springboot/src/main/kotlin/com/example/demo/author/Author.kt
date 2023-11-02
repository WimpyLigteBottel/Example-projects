package com.example.demo.author

import com.example.demo.book.Book
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
data class Author(
    @Id
    @GeneratedValue
    var id: Long = 0,
    @Column(nullable = false)
    var firstName: String = "",
    @Column(nullable = false)
    var lastName: String = "",
    @OneToMany
    @JsonBackReference
    var books: MutableList<Book> = mutableListOf(),
)