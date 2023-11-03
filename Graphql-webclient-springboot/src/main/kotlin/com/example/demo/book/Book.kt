package com.example.demo.book

import com.example.demo.author.Author
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne


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
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (id != other.id) return false
        if (name != other.name) return false
        if (pageCount != other.pageCount) return false
        return author == other.author
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + pageCount
        result = 31 * result + author.hashCode()
        return result
    }

    override fun toString(): String {
        return "Book(id=$id, name='$name', pageCount=$pageCount)"
    }


}