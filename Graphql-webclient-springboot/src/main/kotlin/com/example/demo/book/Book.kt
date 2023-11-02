package com.example.demo.book

import com.example.demo.author.Author
import jakarta.persistence.*


@Entity
data class Book(
    @Id
    @GeneratedValue
    var id: Long = 0,
    @Column(nullable = false)
    var name: String = "",
    @Column(nullable = false)
    var pageCount: Int = 0,

    @ManyToOne
    var author: Author = Author()
)