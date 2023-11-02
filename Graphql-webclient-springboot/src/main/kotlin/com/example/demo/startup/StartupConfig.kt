package com.example.demo.startup

import com.example.demo.author.Author
import com.example.demo.author.AuthorRepo
import com.example.demo.book.Book
import com.example.demo.book.BookRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import kotlin.random.Random

@Configuration
@EnableCaching
class StartupConfig : CommandLineRunner {
    @Autowired
    lateinit var authorRepo: AuthorRepo

    @Autowired
    lateinit var bookRepo: BookRepo
    override fun run(vararg args: String?) {
        var author1 = createAuthor()
        var author2 = createAuthor()
        var author3 = createAuthor()


        linkAndUpdate(author1, listOf(createBook(author1), createBook(author1)))
        linkAndUpdate(author2, createBook(author2))
        linkAndUpdate(author3, createBook(author3))

        repeat(1000){
            val author = createAuthor()
            linkAndUpdate(author,createBook(author))
        }

    }

    fun createBook(author: Author) =
        bookRepo.saveAndFlush(
            Book(
                name = "Effective Java ${Random.nextInt(10000)}",
                pageCount = Random.nextInt(1000),
                author = author
            )
        )

    fun createAuthor(
        firstName: String = "firstName-${Random.nextInt(100000)}",
        lastName: String = "lastname-${Random.nextInt(100000)}"
    ) =
        authorRepo.saveAndFlush(Author(firstName = firstName, lastName = lastName))


    fun linkAndUpdate(author: Author, book: Book) = linkAndUpdate(author, listOf(book))

    fun linkAndUpdate(author: Author, books: List<Book>) {
        author.books.addAll(books)
        authorRepo.saveAndFlush(author)
    }
}