package com.example.demo.advance

import com.example.demo.author.Author
import com.example.demo.author.AuthorService
import com.example.demo.book.BookService
import org.springframework.stereotype.Service

@Service
class AdvanceSearchService(
    private val authorService: AuthorService,
    private val bookService: BookService,
) {

    fun findAuthors(bookIds: List<String>): List<Author> {
        return bookIds
            .map { id -> bookService.findAll(id) }// find the books by id
            .flatMap { it.toList() } // flatten to list
            .map { it.authorId } // extra the author ids
            .map { authorService.findAll(it) } // find authors
            .flatMap { it.toList() } // flatten to list
    }

}