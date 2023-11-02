package com.example.demo.service

import com.example.demo.model.Author
import com.example.demo.model.Book
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService {

    val books = Arrays.asList(
        Book("book-1", "Effective Java", 416, "author-1"),
        Book("book-2", "Effective Java 2", 400, "author-1"),
        Book("book-3", "Effective Java 2", 400, "author-1"),
        Book("book-4", "Effective Java 2", 400, "author-1"),
        Book("book-5", "Effective Java 2", 400, "author-1"),
        Book("book-6", "Effective Java 2", 400, "author-1"),
        Book("book-7", "Hitchhiker's Guide to the Galaxy", 208, "author-2"),
        Book("book-8", "Down Under", 436, "author-3")
    )

    fun findAll(
        id: String? = null,
        name: String? = null,
        pageCount: Int? = null,
        authorId: String? = null
    ): List<Book> {
        return books
            .filter { x -> isEqual(id, x.id) }
            .filter { x -> isEqual(name, x.name) }
            .filter { x -> isEqual(pageCount, x.pageCount) }
            .filter { x -> isEqual(authorId, x.authorId) }
    }


    private fun isEqual(fieldA: Any?, x: Any) = fieldA?.let { fieldA == x } ?: true

}