package com.example.demo.book

import com.example.demo.author.Author
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import java.util.*


@Entity
@Table
data class Book(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    var id: String = UUID.randomUUID().toString(),
    @Column(nullable = false)
    var name: String = "",
    @Column(nullable = false)
    var pageCount: Int = 0,

    @OneToOne
    @JsonBackReference
    var author: Author? = null
)