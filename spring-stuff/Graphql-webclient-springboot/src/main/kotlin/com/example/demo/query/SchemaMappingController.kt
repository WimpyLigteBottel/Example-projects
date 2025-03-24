package com.example.demo.query

import com.example.demo.author.Author
import com.example.demo.author.AuthorService
import com.example.demo.book.Book
import com.example.demo.book.BookService
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class SchemaMappingController(
    val authorService: AuthorService,
    val bookService: BookService
) {


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
    ): Author? = authorService.findAuthorById(book.author.id)


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
    ): List<Book> = bookService.findAllBooksByAuthor(author)

}