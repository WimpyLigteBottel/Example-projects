package com.example.demo.author

import com.example.demo.book.Book
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table
data class Author(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    var id: String = "0",
    @Column(nullable = false)
    var firstName: String = "",
    @Column(nullable = false)
    var lastName: String = "",

    @OneToMany
    @JsonBackReference
    var books: MutableList<Book> = mutableListOf(),
)