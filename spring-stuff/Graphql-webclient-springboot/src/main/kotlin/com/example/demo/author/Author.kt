package com.example.demo.author

import com.example.demo.book.Book
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class Author(
    @Id
    @GeneratedValue
    var id: Long = 0,
    @Column(nullable = false)
    var firstName: String = "",
    @Column(nullable = false)
    var lastName: String = "",
    @OneToMany(fetch = FetchType.LAZY)
    @JsonBackReference
    var books: MutableList<Book> = mutableListOf(),
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Author

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        return books == other.books
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + books.hashCode()
        return result
    }

    override fun toString(): String {
        return "Author(id=$id, firstName='$firstName', lastName='$lastName', books=$books)"
    }

}