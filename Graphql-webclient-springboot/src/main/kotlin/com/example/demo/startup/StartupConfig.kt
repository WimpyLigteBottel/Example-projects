package com.example.demo.startup

import com.example.demo.author.Author
import com.example.demo.author.AuthorRepo
import com.example.demo.book.Book
import com.example.demo.book.BookRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class StartupConfig : CommandLineRunner {
    @Autowired
    lateinit var authorRepo: AuthorRepo

    @Autowired
    lateinit var bookRepo: BookRepo
    override fun run(vararg args: String?) {
        var author1 = authorRepo.save(Author(firstName = "Joshua", lastName = "Bloch"))
        val book1 = bookRepo.save(Book(name = "Effective Java 1", pageCount = 416, author = author1))
        author1.books.add(book1)
        authorRepo.save(author1)

        var author2 = authorRepo.save(Author(firstName = "Douglas", lastName = "Adams"))
        var book2 = bookRepo.save(Book(name = "Effective Java 2", pageCount = 400, author = author2))
        author2.books.add(book2)
        authorRepo.save(author2)

        var author3 = authorRepo.save(Author(firstName = "Bill", lastName = "Bryson"))
        var book3 = bookRepo.save(Book(name = "Effective Java 3", pageCount = 400, author = author3))
        author3.books.add(book3)
        authorRepo.save(author3)

    }
}