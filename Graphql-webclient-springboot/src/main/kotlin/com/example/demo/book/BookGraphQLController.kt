package com.example.demo.book

import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller


@Controller
class BookGraphQLController(
    val bookService: BookService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @QueryMapping(
        name = "findBooks" // This is not needed because if left out then take the function name as querymapping name
    )
    fun findBooks(
        @Argument page: Int?,
        @Argument pageSize: Int?
    ): List<Book> {
        log.info("findBooks")
        return bookService.findAll(
            page = page ?: 0,
            pageSize = pageSize ?: 1
        )
    }

    @QueryMapping(
        name = "findBook" // This is not needed because if left out then take the function name as querymapping name
    )
    fun findBook(
        // The same applies to the argument name
        @Argument(name = "id") id: String? = null,
        @Argument name: String? = null,
        @Argument pageCount: Int? = null,
        @Argument authorId: String? = null
    ): Book? {
        log.info("findBook [id=$id;name=$name;pageCount=$pageCount;authorId=$authorId]")
        return bookService.findAll(id, name, pageCount, authorId).firstOrNull()
    }

}

