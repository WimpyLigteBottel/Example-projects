package com.example.demo.query

import com.example.demo.model.Author
import com.example.demo.model.Book
import com.example.demo.service.AuthorService
import com.example.demo.service.BookService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller


@Controller
class AuthorGraphQLController(
    val authorService: AuthorService
) {

    @QueryMapping(
        name = "findAuthors" // This is not needed because if left out then take the function name as querymapping name
    )
    suspend fun findAuthors(
        @Argument page: Int? = 0,
        @Argument pageSize: Int? = 1
    ): List<Author> {
        println("findAuthors")

        return authorService.findAll(page = page ?: 0, pageSize = pageSize ?: 1)
    }

    @QueryMapping(
        name = "findAuthor" // This is not needed because if left out then take the function name as querymapping name
    )
    suspend fun findAuthor(
        // The same applies to the argument name
        @Argument(name = "id") id: String? = null,
        @Argument firstName: String? = null,
        @Argument lastName: String? = null
    ): Author? {
        println("findAuthor")
        return authorService.findAll(id, firstName, lastName).firstOrNull()
    }

}

@Controller
class BookGraphQLController(
    val bookService: BookService
) {

    @QueryMapping(
        name = "findBooks" // This is not needed because if left out then take the function name as querymapping name
    )
    suspend fun findBooks(): List<Book> {
        println("findBooks")
        return bookService.findAll()
    }

    @QueryMapping(
        name = "findBook" // This is not needed because if left out then take the function name as querymapping name
    )
    suspend fun findBook(
        // The same applies to the argument name
        @Argument(name = "id") id: String? = null,
        @Argument name: String? = null,
        @Argument pageCount: Int? = null,
        @Argument authorId: String? = null
    ): Book? {
        println("findBook")
        return bookService.findAll(id, name, pageCount, authorId).firstOrNull()
    }

}

