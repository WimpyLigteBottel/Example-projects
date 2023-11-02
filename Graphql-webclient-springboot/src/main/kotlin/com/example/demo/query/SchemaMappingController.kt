package com.example.demo.query

import com.example.demo.model.Author
import com.example.demo.model.Book
import com.example.demo.service.AuthorService
import com.example.demo.service.BookService
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class SchemaMappingController(
    val authorService: AuthorService,
    val bookService: BookService,
) {

    private val log = LoggerFactory.getLogger(this::class.java)


    /**
     * Example: The following will call this method
     * findBook(id: "book-1"){
     *     id,
     *     author{
     *       id
     *     }
     *   }
     *
     */

    @SchemaMapping(
        value = "author" // the field name in your query...
    )
    fun authors(
        // This the entity that contains the  @SchemaMapping value (aka book contains author) see the Type definition in book.graphqls
        book: Book
    ): Author? {
        log.info("author $book")
        return authorService.findAll(book.authorId).firstOrNull()
    }


    /**
     * Example: The following will call this method
     *  findAuthors{
     *     id,
     *     books{
     *       id
     *     }
     *   },
     */
    @SchemaMapping(
        value = "books" // the field name in your query...
    )
    fun book(
        // This the entity that contains the  @SchemaMapping value (aka book contains author) see the Type definition in book.graphqls
        author: Author
    ): List<Book> {
        log.info("book $author")
        return bookService.findAll(authorId = author.id)
    }

}